package com.example.shop.repository.block;

import com.example.shop.domain.instagram.Block;
import com.example.shop.domain.instagram.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.shop.domain.instagram.QBlock.block;

@RequiredArgsConstructor
public class BlockRepositoryImpl implements BlockRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId) {
        return queryFactory.selectOne()
                .from(block)
                .where(block.fromMember.id.eq(fromMemberId)
                        .and(block.toMember.id.eq(toMemberId)))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Block> findBlockWithFromMemberAndToMemberById(Long blockId) {

        QMember fromMember = new QMember("fromMember");
        QMember toMember = new QMember("toMember");

        Block result = queryFactory.selectFrom(block)
                .join(block.fromMember, fromMember).fetchJoin()
                .join(block.toMember, toMember).fetchJoin()
                .where(block.id.eq(blockId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
