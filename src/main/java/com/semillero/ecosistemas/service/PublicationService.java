package com.semillero.ecosistemas.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semillero.ecosistemas.dto.PublicationDTO;
import com.semillero.ecosistemas.model.Publication;
import com.semillero.ecosistemas.repository.IPublicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PublicationService implements IPublicationService {
    private final IPublicationRepository publicationRepository;
    private final Cloudinary cloudinary;

    public PublicationService(IPublicationRepository publicationRepository, Cloudinary cloudinary) {
        this.publicationRepository = publicationRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public Publication savePublication(PublicationDTO publicationDTO, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty() || files.stream().allMatch(file -> file.isEmpty())) {
            throw new IllegalArgumentException("Debe adjuntar al menos un archivo.");
        }
        if (files.size() > 3) {
            throw new IllegalArgumentException("No puede adjuntar más de 3 archivos.");
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadImagePublication(file, imageUrls);
        }

        publicationDTO.setImagesURLs(imageUrls);
        Publication publication = PublicationDTO.toEntity(publicationDTO);
        return publicationRepository.save(publication);
    }

    @Override
    public Publication updatePublication(Long id, PublicationDTO publicationDTO, List<MultipartFile> files) throws IOException {
        Publication foundPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));

        if (publicationDTO.getUrlsToDelete() != null && !publicationDTO.getUrlsToDelete().isEmpty()) {
            for (String url : publicationDTO.getUrlsToDelete()) {
                deleteImagePublication(url);
                foundPublication.getImagesURLs().remove(url);
            }
        }

        foundPublication.setTitle(publicationDTO.getTitle());
        foundPublication.setDescription(publicationDTO.getDescription());

        if (files != null && !files.isEmpty()) { // Solo si existen nuevos archivos
            List<String> existingImageUrls = foundPublication.getImagesURLs();
            List<String> newImageUrls = new ArrayList<>(existingImageUrls);
            for (MultipartFile file : files) {
                if (newImageUrls.size() >= 3) break;
                uploadImagePublication(file, newImageUrls);
            }
            foundPublication.setImagesURLs(newImageUrls);
        }

        return publicationRepository.save(foundPublication);
    }

    // PARA ROL ADMIN, SIN INCREMENTAR LAS VIEWS
    @Override
    public Publication getPublicationById(Long id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));
    }

    @Override
    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    @Override
    public List<Publication> getAllActivePublications() {
        return publicationRepository.findByDeletedFalse();
    }

    @Override
    public Publication incrementViewPublication(Long id) {
        Publication foundPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));

        foundPublication.incrementViewCount();
        publicationRepository.save(foundPublication);
        return foundPublication;
    }

    @Override
    public Publication markAsDeleted(Long id) {
        Publication foundPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));

        foundPublication.setDeleted(true);
        publicationRepository.save(foundPublication);
        return foundPublication;
    }

    @Override
    public void deleteImagePublication(String url) throws IOException {
        String publicId = extractPublicIdFromUrl(url);
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new IOException("Error al eliminar la imagen.");
        }
    }

    private String extractPublicIdFromUrl(String url) {
        int startIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf('.');
        return url.substring(startIndex, endIndex);
    }

    private void uploadImagePublication(MultipartFile file, List<String> imagesUrls) throws IOException {
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String newImageUrl = uploadResult.get("url").toString();
            imagesUrls.add(newImageUrl);
        }
    }

}
