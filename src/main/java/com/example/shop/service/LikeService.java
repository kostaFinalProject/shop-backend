package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.dto.instagram.ArticleLikeRequestDto;
import com.example.shop.dto.instagram.CommentLikeRequestDto;
import com.example.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ValidationService validationService;

    @Transactional
    public boolean toggleArticleLike(Long memberId, ArticleLikeRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);

        Article article = validationService.validateArticleById(dto.getArticleId());

        Optional<ArticleLike> likeOptional =
                validationService.validateArticleLikeByArticleAndMember(article, member);

        if (likeOptional.isPresent()) {
            ArticleLike articleLike = likeOptional.get();
            articleLike.cancelLike();
            articleLikeRepository.delete(articleLike);
            return false;
        } else {
            ArticleLike articleLike = ArticleLike.createArticleLike(member, article);
            articleLikeRepository.save(articleLike);
            return true;
        }
    }

    @Transactional
    public boolean toggleCommentLike(Long memberId, CommentLikeRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);
        Comment comment = validationService.validateCommentById(dto.getCommentId());

        Optional<CommentLike> likeOptional = validationService.validateCommentLikeByCommentAndMember(comment, member);

        if (likeOptional.isPresent()) {
            CommentLike commentLike = likeOptional.get();
            commentLike.cancelLike();
            commentLikeRepository.delete(commentLike);
            return false;
        } else {
            CommentLike commentLike = CommentLike.createCommentLike(member, comment);
            commentLikeRepository.save(commentLike);
            return true;
        }
    }
}
