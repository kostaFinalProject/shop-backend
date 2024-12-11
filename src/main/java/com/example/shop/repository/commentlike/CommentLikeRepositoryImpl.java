package com.example.shop.repository.commentlike;

import com.example.shop.domain.instagram.CommentLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.shop.domain.instagram.QComment.comment;
import static com.example.shop.domain.instagram.QCommentLike.commentLike;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByCommentAndMember(Long commentId, Long memberId) {
        return queryFactory.selectOne()
                .from(commentLike)
                .where(commentLike.comment.id.eq(commentId)
                        .and(commentLike.member.id.eq(memberId)))
                .fetchFirst() != null;
    }

    @Override
    public Long findCommentLikeIdByCommentAndMember(Long commentId, Long memberId) {
        if (memberId == null) {
            return null;
        }

        return queryFactory.select(commentLike.id)
                .from(commentLike)
                .where(commentLike.comment.id.eq(commentId)
                        .and(commentLike.member.id.eq(memberId)))
                .fetchFirst();
    }

    @Override
    public Optional<CommentLike> findCommentLikeWithCommentAndMemberById(Long commentLikeId) {
        CommentLike result = queryFactory.selectFrom(commentLike)
                .join(commentLike.comment, comment).fetchJoin()
                .join(commentLike.member, member).fetchJoin()
                .where(commentLike.id.eq(commentLikeId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
