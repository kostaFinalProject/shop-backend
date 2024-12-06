package com.example.shop.repository.block;

import com.example.shop.domain.instagram.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long>, BlockRepositoryCustom {
}
