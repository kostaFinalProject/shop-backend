package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 해시 태그
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String tag; // 태그
    private int count; // 태그 횟수

    @Builder
    private Tag(String tag) {
        this.tag = tag;
        this.count = 1;
    }

    public static Tag createTag(String tag) {
        return Tag.builder().tag(tag).build();
    }

    public void incrementCount() {
        this.count++;
    }
}
