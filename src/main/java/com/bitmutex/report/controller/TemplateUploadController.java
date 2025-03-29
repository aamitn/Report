package com.bitmutex.report.controller;

import com.bitmutex.report.entity.Template;
import com.bitmutex.report.repository.TemplateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/templates")
public class TemplateUploadController {

    private final TemplateRepository templateRepository;

    // Allowed file extensions
    private static final String[] ALLOWED_EXTENSIONS = {".docx", ".html"};

    // Example adjectives and nouns for human-readable names
    private static final String[] ADJECTIVES = {"starky", "bright", "mighty", "swift", "golden"};
    private static final String[] NOUNS = {"light", "storm", "eagle", "falcon", "tiger"};

    public TemplateUploadController(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTemplate(@RequestParam("file") MultipartFile file) {
        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedExtension(originalFilename)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only DOCX and HTML files are accepted for upload.");
        }

        try (InputStream fileInputStream = file.getInputStream()) {
            // Compute the MD5 hash of the uploaded file
            String fileHash = computeMD5Hash(fileInputStream);

            // Check for duplicate files in the database
            Optional<Template> existingTemplate = templateRepository.findByFileHash(fileHash);
            if (existingTemplate.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("A template with the same file already exists.");
            }

            // Generate a unique human-readable filename
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String humanName = generateHumanReadableName();
            String uuidPart = UUID.randomUUID().toString().split("-")[4].toUpperCase(); // Use last UUID segment
            String uniqueFilename = humanName + "_" + uuidPart + extension;

            // Save template to database
            Template template = new Template(uniqueFilename, file.getBytes(), fileHash);
            templateRepository.save(template);

            return ResponseEntity.ok("File uploaded successfully: " + uniqueFilename);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading template: " + e.getMessage());
        }
    }

    private boolean isAllowedExtension(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        return Stream.of(ALLOWED_EXTENSIONS).anyMatch(lowerCaseFilename::endsWith);
    }

    // Generate a human-readable name using a random adjective and noun
    private String generateHumanReadableName() {
        Random random = new Random();
        return ADJECTIVES[random.nextInt(ADJECTIVES.length)] + "_" + NOUNS[random.nextInt(NOUNS.length)];
    }

    // Compute the MD5 hash of the file
    private String computeMD5Hash(InputStream is) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] byteArray = new byte[1024];
        int bytesCount;
        while ((bytesCount = is.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        byte[] bytes = digest.digest();

        // Convert bytes to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
