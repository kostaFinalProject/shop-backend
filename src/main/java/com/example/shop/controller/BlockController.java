package com.example.shop.controller;

import com.example.shop.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/{memberId}/{toMemberId}")
    public ResponseEntity<?> saveBlock(@PathVariable("memberId") Long memberId,
                                       @PathVariable("toMemberId") Long toMemberId) {

        blockService.saveBlock(memberId, toMemberId);
        return ResponseEntity.status(HttpStatus.OK).body("차단 목록에 추가했습니다.");
    }

    @DeleteMapping("/{blockId}")
    public ResponseEntity<?> deleteBlock(@PathVariable("blockId") Long blockId) {
        blockService.deleteBlock(blockId);
        return ResponseEntity.status(HttpStatus.OK).body("차단 목록에서 삭제했습니다.");
    }
}
