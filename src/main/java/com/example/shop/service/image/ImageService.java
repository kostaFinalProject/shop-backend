package com.example.shop.service.image;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemCategory;
import com.example.shop.domain.shop.ItemCategoryImg;
import com.example.shop.domain.shop.ItemImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 통합 이미지 서비스 (상품 카테고리, 상품, 게시글, 댓글)
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImgStorageService imgStorageService;

    /** 상품 카테고리 */

    public ItemCategoryImg saveItemCategoryImg(MultipartFile file) {
        String imgUrl = imgStorageService.store(file);
        String imgFileNameInDb = Paths.get(imgUrl).getFileName().toString();

        return ItemCategoryImg.createItemCategoryImg(imgFileNameInDb, file.getOriginalFilename(), imgUrl);
    }

    public ItemCategoryImg updateItemCategoryImg(ItemCategory itemCategory, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return itemCategory.getItemCategoryImg();
        }

        if (itemCategory.getItemCategoryImg() != null &&
                itemCategory.getItemCategoryImg().getOriginImgName().equals(file.getOriginalFilename())) {
            return itemCategory.getItemCategoryImg();
        }

        if (itemCategory.getItemCategoryImg() != null) {
            imgStorageService.delete(itemCategory.getItemCategoryImg().getImgUrl());
        }

        return saveItemCategoryImg(file);
    }

    public void deleteItemCategoryImg(ItemCategory itemCategory) {
        if (itemCategory.getItemCategoryImg() != null) {
            imgStorageService.delete(itemCategory.getItemCategoryImg().getImgUrl());
        }

        for (ItemCategory child : itemCategory.getChildrenItemCategories()) {
            deleteItemCategoryImg(child);
        }
    }

    /** 상품 */

    private ItemImg saveItemImg(MultipartFile file, int index) {
        String imgUrl = imgStorageService.store(file);
        String imgFileNameInDb = Paths.get(imgUrl).getFileName().toString();
        String repImgYn = (index == 0) ? "Y" : "N";

        return ItemImg.createItemImg(imgFileNameInDb, file.getOriginalFilename(), imgUrl, repImgYn);
    }

    public List<ItemImg> saveItemImgs(List<MultipartFile> itemImages) {
        List<ItemImg> itemImgs = new ArrayList<>();

        for (int i = 0; i < itemImages.size(); i++) {
            MultipartFile file = itemImages.get(i);
            itemImgs.add(saveItemImg(file, i));
        }

        return itemImgs;
    }

    public List<ItemImg> updateItemImgs(Item item, List<MultipartFile> newItemImages) {
        for (ItemImg existingItemImg : item.getItemImages()) {
            imgStorageService.delete(existingItemImg.getImgUrl());
        }

        List<ItemImg> updatedItemImgs = new ArrayList<>();
        for (int i = 0; i < newItemImages.size(); i++) {
            MultipartFile file = newItemImages.get(i);
            updatedItemImgs.add(saveItemImg(file, i));
        }

        return updatedItemImgs;
    }

    public void deleteItemImg(Item item) {
        for (ItemImg existingItemImg : item.getItemImages()) {
            imgStorageService.delete(existingItemImg.getImgUrl());
        }
    }

    /** 게시글 */

    private ArticleImg saveArticleImg(MultipartFile file) {
        String imgUrl = imgStorageService.store(file);
        String imgFileNameInDb = Paths.get(imgUrl).getFileName().toString();

        return ArticleImg.createArticleImg(imgFileNameInDb, file.getOriginalFilename(), imgUrl);
    }

    public List<ArticleImg> saveArticleImgs(List<MultipartFile> files) {
        List<ArticleImg> articleImgs = new ArrayList<>();

        for (MultipartFile file : files) {
            articleImgs.add(saveArticleImg(file));
        }

        return articleImgs;
    }

    public List<ArticleImg> updateArticleImgs(Article article, List<MultipartFile> files) {
        for (ArticleImg articleImg : article.getArticleImages()) {
            imgStorageService.delete(articleImg.getImgUrl());
        }

        List<ArticleImg> updateArticleImgs = new ArrayList<>();

        for (MultipartFile file : files) {
            updateArticleImgs.add(saveArticleImg(file));
        }

        return updateArticleImgs;
    }

    public void deleteArticleImg(Article article) {
        for (ArticleImg articleImg : article.getArticleImages()) {
            imgStorageService.delete(articleImg.getImgUrl());
        }
    }

    /** 댓글 & 대댓글 */

    public CommentImg saveCommentImg(MultipartFile file) {
        String imgUrl = imgStorageService.store(file);
        String imgFileNameInDb = Paths.get(imgUrl).getFileName().toString();

        return CommentImg.createImg(imgFileNameInDb, file.getOriginalFilename(), imgUrl);
    }

    public CommentImg updateCommentImg(Comment comment, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return comment.getCommentImg();
        }

        if (comment.getCommentImg() != null &&
                comment.getCommentImg().getOriginImgName().equals(file.getOriginalFilename())) {
            return comment.getCommentImg();
        }

        if (comment.getCommentImg() != null) {
            imgStorageService.delete(comment.getCommentImg().getImgUrl());
        }

        return saveCommentImg(file);
    }

    public void deleteCommentImg(Comment comment) {
        if (comment.getCommentImg() != null) {
            imgStorageService.delete(comment.getCommentImg().getImgUrl());
        }
    }

    /** 회원 프로필 이미지 */
    public MemberProfileImg saveMemberProfileImg(MultipartFile file) {
        String imgUrl = imgStorageService.store(file);
        String imgFileNameInDb = Paths.get(imgUrl).getFileName().toString();

        return MemberProfileImg.createMemberProfileImg(imgFileNameInDb, file.getOriginalFilename(), imgUrl);
    }

    public MemberProfileImg updateMemberProfileImg(Member member, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            deleteMemberProfileImg(member);
            return null;
        }

        if (member.getMemberProfileImg() != null &&
            member.getMemberProfileImg().getOriginImgName().equals(file.getOriginalFilename())) {
            return member.getMemberProfileImg();
        }

        if (member.getMemberProfileImg() != null) {
            imgStorageService.delete(member.getMemberProfileImg().getImgUrl());
        }

        return saveMemberProfileImg(file);
    }

    public void deleteMemberProfileImg(Member member) {
        if (member.getMemberProfileImg() != null) {
            imgStorageService.delete(member.getMemberProfileImg().getImgUrl());
        }
    }
}
