package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import com.example.shop.dto.instagram.article.ArticleDetailResponseDto;
import com.example.shop.dto.instagram.article.ArticleItemResponseDto;
import com.example.shop.dto.instagram.article.ArticleRequestDto;
import com.example.shop.exception.NotFoundException;
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

    @Transactional(readOnly = true)
    public ArticleDetailResponseDto getArticle(Long articleId) {
        Article article = articleRepository.findArticleWithWriterById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시글이 아닙니다."));

        if (article.getArticleStatus() != ArticleStatus.ACTIVE) {
            throw new NotFoundException("비공개 되거나 삭제된 게시글입니다.");
        }

        ArticleDetailResponseDto dto = new ArticleDetailResponseDto();
        dto.setMemberId(article.getMember().getId());
        dto.setMemberName(article.getMember().getName());

        List<String> articleImages = article.getArticleImages().stream()
                .map(ArticleImg::getImgUrl)
                .toList();
        dto.setImages(articleImages);

        List<String> hashTags = article.getArticleTags().stream()
                .map(articleTag -> articleTag.getTag().getTag())
                .toList();
        dto.setHashtags(hashTags);

        List<ArticleItemResponseDto> items = article.getArticleItems().stream()
                .map(articleItem -> {
                    ArticleItemResponseDto itemDto = new ArticleItemResponseDto();
                    itemDto.setItemId(articleItem.getItem().getId());
                    itemDto.setItemName(articleItem.getItem().getName());
                    itemDto.setPrice(articleItem.getItem().getPrice());
                    itemDto.setImageUrl(articleItem.getItem().getRepItemImage());

                    return itemDto;
                }).toList();
        dto.setItems(items);

        return dto;
    }

    @Transactional
    public void activeArticle(Long memberId, Long articleId) {
        Member member = validationService.validateMemberById(memberId);
        Article article = validationService.validateArticleById(articleId);

        if (member.getGrade() == Grade.USER) {
            throw new IllegalArgumentException("게시물을 활성화할 권한이 없습니다.");
        }

        article.activeArticle();
    }

    @Transactional
    public void inactiveArticle(Long memberId, Long articleId) {
        Member member = validationService.validateMemberById(memberId);
        Article article = validationService.validateArticleById(articleId);

        if (member.getGrade() == Grade.USER) {
            throw new IllegalArgumentException("게시물을 비활성화할 권한이 없습니다.");
        }

        article.inActiveArticle();
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
