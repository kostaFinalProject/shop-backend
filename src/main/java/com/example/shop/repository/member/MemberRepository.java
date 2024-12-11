package com.example.shop.repository.member;

import com.example.shop.domain.instagram.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
}
