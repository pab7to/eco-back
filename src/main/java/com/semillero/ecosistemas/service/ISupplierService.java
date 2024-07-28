package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface ISupplierService {
    //Create
    public Supplier saveSupplier(Supplier supplier);

    //Find
    public Optional<Supplier> findSupplierById(Long id);
    public Optional<Supplier> findSupplierByEmail(String email);

    //Read
    public List<Supplier> getAllSuppliers();

    //Update (Change State --> deleted)
    public void switchState(Supplier supplier);
}