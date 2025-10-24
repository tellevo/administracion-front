package cl.tellevo.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${app.images.upload-dir:/var/www/images}")
    private String uploadDir;

    @Value("${app.images.allowed-types:image/png,image/jpeg,image/jpg,image/svg+xml}")
    private String allowedTypes;

    @Value("${app.images.max-size:5242880}")
    private long maxFileSize;

    private List<String> allowedContentTypes;

    @PostConstruct
    private void initializeAllowedTypes() {
        this.allowedContentTypes = Arrays.asList(allowedTypes.split(","));
    }

    /**
     * Initialize the upload directory
     */
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created upload directory: {}", uploadDir);
            }

            // Create empresas subdirectory
            Path empresasPath = uploadPath.resolve("empresas");
            if (!Files.exists(empresasPath)) {
                Files.createDirectories(empresasPath);
                logger.info("Created empresas directory: {}", empresasPath);
            }

        } catch (Exception e) {
            logger.error("Could not initialize upload directory: {} - Error: {}", uploadDir, e.getMessage(), e);
            // Don't throw exception - allow application to start even if directory creation fails
            // The directory will be created when first file is uploaded
            logger.warn("Application will continue but file uploads may fail until directory permissions are fixed");
        }
    }

    /**
     * Store a file for an empresa
     * @param file the multipart file
     * @param empresaId the empresa ID
     * @return the stored file path relative to upload directory
     */
    public String storeFile(MultipartFile file, Long empresaId) {
        validateFile(file);

        try {
            String fileName = generateFileName(file, empresaId);
            Path empresaDir = Paths.get(uploadDir, "empresas", empresaId.toString());

            // Create empresa directory if it doesn't exist
            if (!Files.exists(empresaDir)) {
                try {
                    Files.createDirectories(empresaDir);
                    logger.info("Created empresa directory: {}", empresaDir);
                } catch (Exception e) {
                    logger.error("Failed to create empresa directory: {}", empresaDir, e);
                    throw new RuntimeException("Cannot create directory for empresa files. Check permissions.", e);
                }
            }

            Path targetLocation = empresaDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String relativePath = "empresas/" + empresaId + "/" + fileName;
            logger.info("File stored successfully: {}", relativePath);

            return relativePath;

        } catch (IOException e) {
            logger.error("Could not store file for empresa {}: {}", empresaId, file.getOriginalFilename(), e);
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". " + e.getMessage(), e);
        }
    }

    /**
     * Delete all files for an empresa
     * @param empresaId the empresa ID
     */
    public void deleteEmpresaFiles(Long empresaId) {
        try {
            Path empresaDir = Paths.get(uploadDir, "empresas", empresaId.toString());
            if (Files.exists(empresaDir)) {
                Files.walk(empresaDir)
                    .sorted((a, b) -> b.compareTo(a)) // Reverse order for deletion
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            logger.debug("Deleted file: {}", path);
                        } catch (IOException e) {
                            logger.warn("Could not delete file: {}", path, e);
                        }
                    });
                logger.info("Deleted all files for empresa: {}", empresaId);
            }
        } catch (IOException e) {
            logger.error("Error deleting files for empresa {}: {}", empresaId, e.getMessage(), e);
        }
    }

    /**
     * Generate a unique filename for the uploaded file
     * @param file the multipart file
     * @param empresaId the empresa ID
     * @return the generated filename
     */
    private String generateFileName(MultipartFile file, Long empresaId) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);

        // Generate unique filename: empresa_{id}_logo.{ext}
        return "empresa_" + empresaId + "_logo" + fileExtension;
    }

    /**
     * Get file extension from filename
     * @param filename the filename
     * @return the file extension with dot
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return ""; // No extension
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    /**
     * Validate the uploaded file
     * @param file the multipart file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size: " + (maxFileSize / 1024 / 1024) + "MB");
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !allowedContentTypes.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("File type not allowed. Allowed types: " + allowedTypes);
        }

        // Check filename for path traversal
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence");
        }
    }

    /**
     * Get the full file path for a stored file
     * @param relativePath the relative path from storeFile method
     * @return the full file path
     */
    public Path getFilePath(String relativePath) {
        return Paths.get(uploadDir, relativePath);
    }

    /**
     * Check if a file exists
     * @param relativePath the relative path
     * @return true if file exists
     */
    public boolean fileExists(String relativePath) {
        return Files.exists(getFilePath(relativePath));
    }

    /**
     * Get the upload directory path
     * @return the upload directory
     */
    public String getUploadDir() {
        return uploadDir;
    }
}