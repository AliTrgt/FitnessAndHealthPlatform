package com.example.HealthAndFitnessPlatform.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/uploads")
public class ImageUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;


    @PostMapping("/images")
    public ResponseEntity<String> uploadImages(@RequestParam("file")MultipartFile file){
        try {
               String filePath = saveImage(file);
               return ResponseEntity.ok("Image Uploaded successfully : "+filePath);
           }catch (IOException e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
           }
    }


    public String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

}
