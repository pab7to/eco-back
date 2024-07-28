package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.dto.PublicationDTO;
import com.semillero.ecosistemas.model.Publication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPublicationService {
    Publication savePublication(PublicationDTO publicationDTO, List<MultipartFile> files) throws IOException;

    Publication updatePublication(Long idPublication, PublicationDTO publicationDTO, List<MultipartFile> files) throws IOException;

    Publication getPublicationById(Long id);

    List<Publication> getAllPublications();

    List<Publication> getAllActivePublications();

    Publication incrementViewPublication(Long id);

    Publication markAsDeleted(Long id);

    void deleteImagePublication(String url) throws IOException;
}
