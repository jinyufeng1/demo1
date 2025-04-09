package com.example.demo1.file.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空，请选择一个文件";
        }

        // 设置文件保存路径
        String uploadDir = "d:/uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();// 如果目录不存在，创建目录
        }

        try {
            // 保存文件
            String fileName = file.getOriginalFilename();
            file.transferTo(new File(uploadDir + fileName));
            return "文件上传成功: " + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败: " + e.getMessage();
        }
    }
}
