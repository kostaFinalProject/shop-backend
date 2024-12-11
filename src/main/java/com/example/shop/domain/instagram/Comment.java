package com.example.shop.domain.instagram;

import com.example.shop.domain.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시물 댓글, 대댓글
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_img_id")
    private CommentImg commentImg;

    private int likes;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @Builder
    private Comment(Article article, Member member, String content, Comment parentComment, CommentImg commentImg) {
        this.article = article;
        this.member = member;
        this.content = content;
        this.parentComment = parentComment;
        this.likes = 0;
        this.commentStatus = CommentStatus.ACTIVE;
        this.commentImg = commentImg;
    }

    /** 댓글 생성 */
    public static Comment createComment(Article article, Member member,
                                 String content, Comment parentComment, CommentImg commentImg) {
        Comment comment = Comment.builder().article(article).member(member)
                .content(content).parentComment(parentComment).commentImg(commentImg).build();

        if (parentComment != null) {
            parentComment.addChildComment(comment);
        }

        return comment;
    }

    /** 부모-자식 관계 관리 메서드 */
    protected void addChildComment(Comment childComment) {
        this.comments.add(childComment);
        childComment.setParentComment(this);
    }

    protected void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    /** 댓글 수정 - 내용과 이미지 */
    public void updateComment(String newContent, CommentImg newCommentImg) {
        if (this.commentStatus != CommentStatus.ACTIVE) {
            throw new IllegalStateException("비활성화 된 댓글이나 삭제된 댓글입니다.");
        }

        this.content = newContent;
        this.commentImg = newCommentImg;
    }

    /** 댓글 좋아요 /취소 */
    protected void setLikes(int likes) {
        this.likes += likes;
    }

    /** 댓글 활성화 */
    public void activeComment() {
        this.commentStatus = CommentStatus.ACTIVE;
    }

    /** 댓글 비활성화 */
    public void inactiveComment() {
        this.commentStatus = CommentStatus.INACTIVE;
    }

    /** 댓글 삭제 */
    public void deleteComment() {
        this.commentStatus = CommentStatus.DELETED;
    }

    /** 대댓글 존재 여부 판단 */
    public boolean isReplyComments() {
        return !this.comments.isEmpty();
    }
}