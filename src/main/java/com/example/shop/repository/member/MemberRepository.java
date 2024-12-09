package com.example.shop.repository.member;

import com.example.shop.domain.instagram.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByUserId(String userId);
}
