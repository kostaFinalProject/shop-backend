package com.example.shop.domain;

import com.example.shop.domain.instagram.Member;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    public void createMember(){
        //given
        Member member = Member.createMember("test");

        //when

        //then
        assertThat(member.getArticles()).isEqualTo(0);
        assertThat(member.getFollowees()).isEqualTo(0);
        assertThat(member.getFollowers()).isEqualTo(0);
    }

}