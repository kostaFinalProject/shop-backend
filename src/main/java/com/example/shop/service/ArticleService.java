package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import com.example.shop.dto.instagram.ArticleRequestDto;
import com.example.shop.repository.ArticleRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    @Transactional
    public void saveArticle(Long memberId, ArticleRequestDto dto, List<MultipartFile> articleImages) {
        Member member = validationService.validateMemberById(memberId);

        List<ArticleImg> articleImgs = imageService.saveArticleImgs(articleImages);

        List<ArticleTag> articleTags = dto.getHashtags().stream()
                .map(tagName -> {
                    Tag tag = validationService.validateTagByName(tagName);
                    return ArticleTag.createArticleTag(tag);
                })
                .toList();

        List<ArticleItem> articleItems = dto.getItemIds().stream()
                .map(itemId -> {
                    Item item = validationService.validateItemById(itemId);
                    return ArticleItem.createArticleItem(item);
                })
                .toList();

        Article article = Article.createArticle(member, articleImgs, dto.getContent(), articleTags, articleItems);
        articleRepository.save(article);
    }

    @Transactional
    public void updateArticle(Long memberId, Long articleId, ArticleRequestDto dto, List<MultipartFile> articleImages) {

        Member member = validationService.validateMemberById(memberId);

        Article article = validationService.validateArticleById(articleId);

        if (!article.getMember().equals(member)) {
            throw new IllegalArgumentException("수정할 권한이 없습니다.");
        }

        List<ArticleImg> articleImgs = imageService.updateArticleImgs(article, articleImages);
        article.updateArticleImages(articleImgs);

        if (!dto.getContent().equals(article.getContent())) {
            article.updateArticle(dto.getContent());
        }

        List<ArticleTag> newTags = dto.getHashtags().stream()
                .map(tagName -> {
                    Tag tag = validationService.validateTagByName(tagName);
                    return ArticleTag.createArticleTag(tag);
                })
                .toList();
        article.updateArticleTags(newTags);

        List<ArticleItem> newItems = dto.getItemIds().stream()
                .map(itemId -> {
                    Item item = validationService.validateItemById(itemId);
                    return ArticleItem.createArticleItem(item);
                })
                .toList();
        article.updateArticleItems(newItems);
    }

    @Transactional
    public void deleteArticle(Long memberId, Long articleId) {
        Member member = validationService.validateMemberById(memberId);
        Article article = validationService.validateArticleById(articleId);

        if (member.getGrade() == Grade.USER && !article.getMember().equals(member)) {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }

        imageService.deleteArticleImg(article);
        article.deleteArticle();
    }
}
