package com.beyond.bycontract.shared.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3StorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final S3Presigner s3Presigner;

    public S3StorageService(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.s3Presigner = s3Presigner;
    }

    public String uploadSignedContract(UUID idContract, MultipartFile file) {
        try {
            // Générer une clé unique pour éviter les collisions
            String extension = ".pdf";
            String fileKey= "contracts/" + idContract + "/signed_" + System.currentTimeMillis() + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileKey).contentType("application/pdf").build();

            // Upload direct depuis le flux (InputStream) pour ne pas saturer la RAM
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // On retourne la clé (ex: "contracts/123.../signed_169...pdf")
            return fileKey;
        }
        catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier PDF", e);
        }
    }

    public String getPresignedUrl(String objectKey) {
        if(objectKey == null || objectKey.isEmpty()) {
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest).build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
