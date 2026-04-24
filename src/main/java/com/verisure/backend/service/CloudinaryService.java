package com.verisure.backend.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.verisure.backend.exception.InvalidImageException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        
        if (file.isEmpty()) return null;

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageException("El archivo debe ser una imagen (JPG, PNG, etc.)");
        }
        
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new InvalidImageException("La imagen es demasiado grande. Máximo 10MB.");
        }


        try{
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("folder", "projects")); 
            return uploadResult.get("secure_url").toString();
        } catch (IOException e){
            throw new RuntimeException("Error al subir la imagen", e);
        }
        
    }

    public void deleteImage(String publicId) {
        try {
            if (publicId != null && !publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen de Cloudinary", e);
        }
    }
}