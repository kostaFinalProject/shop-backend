package com.example.shop.domain.instagram;

import com.example.shop.domain.shop.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    public void createArticle() {
        //given
        Member member = Member.createMember("test");
        ArticleImg articleImg1 = ArticleImg.createArticleImg("aaa", "aaaa", "aaaaa");
        ArticleImg articleImg2 = ArticleImg.createArticleImg("bbb", "bbbb", "bbbbb");
        ArticleImg articleImg3 = ArticleImg.createArticleImg("aaa", "cccc", "ccccc");

        Size xs = Size.createSize("XS");
        Size s = Size.createSize("S");
        Size m = Size.createSize("M");
        Size l = Size.createSize("L");
        Size xl = Size.createSize("XL");
        Size txl = Size.createSize("2XL");

        ItemSize xsSize = ItemSize.createItemSize(xs, 200);
        ItemSize sSize = ItemSize.createItemSize(s, 200);
        ItemSize mSize = ItemSize.createItemSize(m, 200);
        ItemSize lSize = ItemSize.createItemSize(l, 200);
        ItemSize xlSize = ItemSize.createItemSize(xl, 200);
        ItemSize txlSize = ItemSize.createItemSize(txl, 200);

        List<ItemSize> sizes = new ArrayList<>();
        sizes.add(xsSize);
        sizes.add(sSize);
        sizes.add(mSize);
        sizes.add(lSize);
        sizes.add(xlSize);
        sizes.add(txlSize);

        ItemImg itemImg = ItemImg.createItemImg("aaa","aaaa", "aaaaa", "Y");
        List<ItemImg> itemImgs = new ArrayList<>();
        itemImgs.add(itemImg);

        ItemCategory category = ItemCategory.createItemCategory("category", null, null);
        Item item1 = Item.createItem(category, "adidas", "testItem1", 2000, sizes, itemImgs);
        Item item2 = Item.createItem(category, "adidas", "testItem2", 2000, sizes, itemImgs);

        Tag tag1 = Tag.createTag("tag1");
        Tag tag2 = Tag.createTag("tag2");

        ArticleTag articleTag1 = ArticleTag.createArticleTag(tag1);
        ArticleTag articleTag2 = ArticleTag.createArticleTag(tag2);

        ArticleItem articleItem1 = ArticleItem.createArticleItem(item1);
        ArticleItem articleItem2 = ArticleItem.createArticleItem(item2);

        //when
        List<ArticleImg> articleImages = new ArrayList<>();
        articleImages.add(articleImg1);
        articleImages.add(articleImg2);

        List<ArticleTag> articleTags = new ArrayList<>();
        articleTags.add(articleTag1);
        articleTags.add(articleTag2);

        List<ArticleItem> articleItems = new ArrayList<>();
        articleItems.add(articleItem1);
        articleItems.add(articleItem2);

        Article article = Article.createArticle(member, articleImages, "test", articleTags, articleItems);

        //then
        assertThat(articleImg1.getArticle()).isEqualTo(article);
        assertThat(articleImg2.getArticle()).isEqualTo(article);
        assertThat(article.getArticleImages().size()).isEqualTo(2);

        List<ArticleImg> newArticleImages = new ArrayList<>();
        newArticleImages.add(articleImg1);
        newArticleImages.add(articleImg3);

        article.updateArticleImages(newArticleImages);
        assertThat(article.getArticleImages().size()).isEqualTo(2);
        assertThat(articleImg2.getArticle()).isNull();
        assertThat(articleImg3.getArticle()).isEqualTo(article);
        assertThat(article.getArticleImages().get(0)).isEqualTo(articleImg1);
        assertThat(article.getArticleImages().get(1)).isEqualTo(articleImg3);
    }
}