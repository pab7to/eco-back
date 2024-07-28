package com.semillero.ecosistemas.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "admins")
public class Admin extends User{

    @ElementCollection
    List<Publication> publicationList;

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
        setRole(Role.ADMIN);
        setDeleted(false);
        if (publicationList == null) {
            publicationList = new ArrayList<>();
        }
    }
}