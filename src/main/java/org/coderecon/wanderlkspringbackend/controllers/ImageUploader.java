package org.coderecon.wanderlkspringbackend.controllers;

import org.coderecon.wanderlkspringbackend.models.VisaDetails;
import org.coderecon.wanderlkspringbackend.services.VisaServices;
import org.coderecon.wanderlkspringbackend.utils.S3ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("api/image/")
public class ImageUploader {

    @PostMapping("/upload")
    public String uploadImage(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("uploadType") String uploadType,
            @RequestParam("fileName") String fileName,
            @RequestPart("fileContent") MultipartFile fileContent) throws IOException {

        // Convert MultipartFile to byte array or stream if needed
        byte[] content = fileContent.getBytes();

        // Create S3ImageUploader instance and call the uploadToS3 method
        S3ImageUploader s3ImageUploader = new S3ImageUploader();
        return s3ImageUploader.uploadToS3(userEmail, uploadType, content, fileName);
    }

}
