package com.main.traveltour.service.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadResize {
    String uploadFileResize(MultipartFile multipartFile) throws IOException;
}
