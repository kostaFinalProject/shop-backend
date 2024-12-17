package com.example.shop.repository.comment;

import com.example.shop.domain.instagram.Comment;
import com.example.shop.domain.instagram.CommentStatus;
import com.example.shop.domain.instagram.QComment;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.shop.domain.instagram.QArticle.article;
import static com.example.shop.domain.instagram.QBlock.block;
import static com.example.shop.domain.instagram.QComment.comment;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findReplyCommentsByCommentId(Long memberId, Long commentId, Pageable pageable) {

        List<Comment> replyComments;
        JPAQuery<Long> countQuery;

        if (memberId == null) {
            replyComments = queryFactory
                    .selectFrom(comment)
                    .where(comment.parentComment.id.eq(commentId)
                            .and(comment.commentStatus.ne(CommentStatus.DELETED)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory
                    .select(comment.count())
                    .from(comment)
                    .where(comment.parentComment.id.eq(commentId)
                            .and(comment.commentStatus.ne(CommentStatus.DELETED)));

        } else {
            List<Long> excludedMemberIds = getExcludedMemberIds(memberId);

            replyComments = queryFactory
                    .selectFrom(comment)
                    .where(comment.parentComment.id.eq(commentId)
                            .and(comment.commentStatus.ne(CommentStatus.DELETED))
                            .and(comment.member.id.notIn(excludedMemberIds)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory
                    .select(comment.count())
                    .from(comment)
                    .where(comment.parentComment.id.eq(commentId)
                            .and(comment.commentStatus.ne(CommentStatus.DELETED))
                            .and(comment.member.id.notIn(excludedMemberIds)));
        }

        return PageableExecutionUtils.getPage(replyComments, pageable, countQuery::fetchOne);
    }


    @Override
    public Optional<Comment> findParentCommentWithArticleByCommentId(Long commentId) {

        QComment parentComment = new QComment("parentComment");

        Comment parent = queryFactory.selectFrom(comment)
                .join(comment.parentComment, parentComment).fetchJoin()
                .join(parentComment.article, article).fetchJoin()
                .where(parentComment.id.eq(commentId))
                .fetchOne();

        return Optional.ofNullable(parent);
    }

    @Override
    public Optional<Comment> validateCommentAndMemberById(Long commentId, Long memberId) {
        Comment result = queryFactory.selectFrom(comment)
                .join(comment.member, member).fetchJoin()
                .where(comment.id.eq(commentId)
                        .and(comment.member.id.eq(memberId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    /** 차단 기능에 따라 필터링 (내가 차단한 사람, 나를 차단한 사람을 검색 결과에서 제외 시킴) */
    private List<Long> getExcludedMemberIds(Long memberId) {
        List<Long> blockedMemberIds = queryFactory
                .select(block.toMember.id)
                .from(block)
                .where(block.fromMember.id.eq(memberId))
                .fetch();

        List<Long> blockingMemberIds = queryFactory
                .select(block.fromMember.id)
                .from(block)
                .where(block.toMember.id.eq(memberId))
                .fetch();

        return Stream.concat(blockedMemberIds.stream(), blockingMemberIds.stream())
                .distinct()
                .toList();
    }
}