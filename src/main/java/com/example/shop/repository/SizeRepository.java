package com.example.shop.repository;

import com.example.shop.domain.shop.Size;

import java.util.Optional;

public interface SizeRepository extends ReadOnlyRepository<Size, Long> {
    Optional<Size> findBySize(String size);
}
