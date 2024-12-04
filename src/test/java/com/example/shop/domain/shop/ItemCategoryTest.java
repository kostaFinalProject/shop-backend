package com.example.shop.domain.shop;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Domain 기능 Test (DB 사용 X)
 */
class ItemCategoryTest {

    @Test
    void ItemCategoryTest1() {
        //given
        ItemCategoryImg itemCategoryImg1 = ItemCategoryImg.createItemCategoryImg("aaa", "aaaa", "aaaaa");
        ItemCategoryImg itemCategoryImg2 = ItemCategoryImg.createItemCategoryImg("bbb", "bbbb", "bbbbb");

        ItemCategory parent = ItemCategory.createItemCategory("parent", null, null);
        ItemCategory child1 = ItemCategory.createItemCategory("child1", null, itemCategoryImg1);
        ItemCategory child2 = ItemCategory.createItemCategory("child2", null, itemCategoryImg2);
        //when
        parent.addChildItemCategory(child1);
        parent.addChildItemCategory(child2);

        //then
        assertThat(parent.getChildrenItemCategories().size()).isEqualTo(2);
        assertThat(child1.getParentItemCategory()).isEqualTo(parent);

        assertThat(itemCategoryImg1.getItemCategory()).isEqualTo(child1);
        assertThat(itemCategoryImg2.getItemCategory()).isEqualTo(child2);
        assertThat(child1.getItemCategoryImg()).isEqualTo(itemCategoryImg1);
        assertThat(child2.getItemCategoryImg()).isEqualTo(itemCategoryImg2);
    }

    @Test
    void ItemCategoryTest2() {
        //given
        ItemCategory parent = ItemCategory.createItemCategory("parent", null, null);

        //when
        ItemCategory child1 = ItemCategory.createItemCategory("child1", parent, null);
        ItemCategory child2 = ItemCategory.createItemCategory("child2", parent, null);

        //then
        assertThat(parent.getChildrenItemCategories().size()).isEqualTo(2);
        assertThat(child1.getParentItemCategory()).isEqualTo(parent);
        assertThat(child2.getParentItemCategory()).isEqualTo(parent);
    }

}