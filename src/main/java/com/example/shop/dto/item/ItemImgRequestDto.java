package com.example.shop.dto.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ItemImgRequestDto {
    private MultipartFile file;
    private boolean isRepImg;

    public boolean hasFile() {
        return file != null && !file.isEmpty();
    }
}
