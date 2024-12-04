package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    private CommentLike(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }

    public static CommentLike createCommentLike(Member member, Comment comment) {
        comment.setLikes(1);
        return CommentLike.builder().member(member).comment(comment).build();
    }

    public void cancelLike() {
        this.comment.setLikes(-1);
    }
}
