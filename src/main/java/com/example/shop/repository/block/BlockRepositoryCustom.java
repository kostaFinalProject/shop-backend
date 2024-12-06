package com.example.shop.repository.block;

import com.example.shop.domain.instagram.Block;

import java.util.Optional;

public interface BlockRepositoryCustom {
    boolean existByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);
    Optional<Block> findBlockWithFromMemberAndToMemberById(Long blockId);
}
