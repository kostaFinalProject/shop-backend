package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.dto.MemberFormDto;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveMember(MemberFormDto dto) {
        Member member = Member.createMember(dto.getName());
        memberRepository.save(member);
    }
}
