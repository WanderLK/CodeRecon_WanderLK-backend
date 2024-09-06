package org.coderecon.wanderlkspringbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private static final String UPLOADED_FOLDER = "uploaded-images/";

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try {
            // Generate a unique name for the file
            String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            File folder = new File(UPLOADED_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File serverFile = new File(folder.getAbsolutePath() + File.separator + uniqueFileName);
            try (OutputStream os = new FileOutputStream(serverFile)) {
                os.write(file.getBytes());
            }

            // Return the URL of the uploaded image
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/download/")
                    .path(uniqueFileName)
                    .toUriString();

            Map<String, String> response = new HashMap<>();
            response.put("url", fileDownloadUri);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public byte[] downloadImage(@PathVariable String filename) throws IOException {
        File file = new File(UPLOADED_FOLDER + filename);
        return java.nio.file.Files.readAllBytes(file.toPath());
    }
}



