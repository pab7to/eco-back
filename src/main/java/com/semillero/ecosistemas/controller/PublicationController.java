package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.dto.PublicationDTO;
import com.semillero.ecosistemas.model.Publication;
import com.semillero.ecosistemas.service.PublicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/publications")
@Validated
public class PublicationController {
    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    // CREAR UNA NUEVA PUBLICACION
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Publication> createPublication(@Valid @ModelAttribute PublicationDTO publicationDTO, @RequestParam List<MultipartFile> files) {
        try {
            Publication savedPublication = publicationService.savePublication(publicationDTO, files);
            return new ResponseEntity<>(savedPublication, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ACTUALIZAR UNA PUBLICACION POR ID
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Publication> updatePublication(@Valid @PathVariable Long id, @ModelAttribute PublicationDTO publicationDTO, @RequestParam List<MultipartFile> files) {
        try {
            Publication updatedPublication = publicationService.updatePublication(id, publicationDTO, files);
            return new ResponseEntity<>(updatedPublication, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    // OBTENER LAS PUBLICACIONES ACTIVAS Y NO ACTIVAS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<Publication>> getAllPublications() {
        try {
            List<Publication> listPublications = publicationService.getAllPublications();
            return new ResponseEntity<>(listPublications, HttpStatus.OK);
        } catch (ErrorResponseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // OBTENER UNA PUBLICACION POR ID (ADMIN) SIN INCREMENTAR LAS VIEWS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable @Valid Long id) {
        try {
            return new ResponseEntity<>(publicationService.getPublicationById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // OBTENER LAS PUBLICACIONES ACTIVAS
    @GetMapping
    public ResponseEntity<List<Publication>> getAllActivePublications() {
        try {
            List<Publication> listActivePublications = publicationService.getAllActivePublications();
            return new ResponseEntity<>(listActivePublications, HttpStatus.OK);
        } catch (ErrorResponseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // INCREMENTAR EN UNO LAS VISUALIZACIONES DE UNA PUBLICACION
    @PreAuthorize("hasAnyRole('SUPPLIER', 'USER')")
    @GetMapping("/view/{id}")
    public ResponseEntity<Publication> incrementViewPublication(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(publicationService.incrementViewPublication(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // CAMBIAR EL ESTADO DE UNA PUBLICACACION A 'DELETED' (OCULTO)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Publication> markAsDeleted(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(publicationService.markAsDeleted(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
