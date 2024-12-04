package com.example.shop.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImgStorageService {
    String store(MultipartFile file);
    void delete(String fileName);
}
