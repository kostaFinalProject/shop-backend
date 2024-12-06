package com.example.shop.service;

import com.example.shop.domain.instagram.Block;
import com.example.shop.domain.instagram.Member;
import com.example.shop.repository.block.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final ValidationService validationService;

    public void saveBlock(Long fromMemberId, Long toMemberId) {
        if (fromMemberId.equals(toMemberId)) {
            throw new IllegalArgumentException("자신을 차단할 수 없습니다.");
        }

        if (validationService.existByFromMemberIdAndToMemberId(fromMemberId, toMemberId)) {
            throw new IllegalArgumentException("이미 차단된 사용자입니다.");
        }

        Member fromMember = validationService.validateMemberById(fromMemberId);
        Member toMember = validationService.validateMemberById(toMemberId);

        Block block = Block.createBlock(fromMember, toMember);
        blockRepository.save(block);
    }

    public void deleteBlock(Long blockId) {
        Block block = validationService.findBlockWithFromMemberAndToMemberById(blockId);
        blockRepository.delete(block);
    }
}
