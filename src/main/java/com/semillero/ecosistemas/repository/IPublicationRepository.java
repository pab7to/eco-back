package com.semillero.ecosistemas.repository;

import com.semillero.ecosistemas.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByDeletedFalse();
    Publication findByImagesURLs(String imageUrl);

}
