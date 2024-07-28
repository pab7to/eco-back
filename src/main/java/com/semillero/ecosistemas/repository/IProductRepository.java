package com.semillero.ecosistemas.repository;

import com.semillero.ecosistemas.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameStartingWithIgnoreCase(String name);
}

