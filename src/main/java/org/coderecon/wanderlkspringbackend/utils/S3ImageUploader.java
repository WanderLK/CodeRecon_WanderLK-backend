package org.coderecon.wanderlkspringbackend.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class S3ImageUploader {

    private AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private String bucketName = "wanderlk-buckets";
    private String folderName = "wanderlk-resources/";


    public String uploadToS3(String userEmail, String uploadType, InputStream fileContent, String fileName) {
        System.out.println("Inside upload to S3 method");
        // Generate a new file name based on user's email and original extension
        String newFileName = folderName + generateFileName(userEmail, uploadType, fileName);
        String format = FilenameUtils.getExtension(fileName);
        ByteArrayInputStream imageStream = convertToImageFormat(fileContent, format);

        // Prepare metadata for the uploaded file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageStream.available());
        metadata.setContentType(getContentType(format));


        try {
            // Upload the file to S3
            s3Client.putObject(new PutObjectRequest(bucketName, newFileName, imageStream, metadata));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Close the streams
            try {
                imageStream.close();
                fileContent.close();
                try {
                    System.out.println("Closing input streams");
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace(); // handle the exception appropriately
            }
            System.out.println("Closed input streams");
        }
        // Return the S3 path
        return "https://" + bucketName + ".s3.ap-south-1.amazonaws.com/" + newFileName;
    }

    private String generateFileName(String userEmail, String uploadType, String fileName) {
        // Get the file extension
        String extension = FilenameUtils.getExtension(fileName);

        // Generate a unique identifier for the file name
        String uniqueId = UUID.randomUUID().toString();

        // Create the new file name using user's email, upload type and extension
        return userEmail.split("@")[0] + "_" + uniqueId + uploadType + "." + extension;
    }

    private ByteArrayInputStream convertToImageFormat(InputStream fileContent, String format) {
        try {
            // Read the input stream into a BufferedImage
            BufferedImage image = ImageIO.read(fileContent);

            // Write the BufferedImage to a byte array in the specified format
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, format, byteArrayOutputStream);

            // Convert the ByteArrayOutputStream to a ByteArrayInputStream
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error converting image to " + format, e);
        }
    }

    private String getContentType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> "application/octet-stream"; // Default content type
        };
    }


}
