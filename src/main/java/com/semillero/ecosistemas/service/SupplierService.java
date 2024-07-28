package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Supplier;
import com.semillero.ecosistemas.repository.ISupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements ISupplierService{
    @Autowired
    ISupplierRepository supplierRepository;

    @Override
    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Optional<Supplier> findSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Optional<Supplier> findSupplierByEmail(String email) {
        return supplierRepository.findSupplierByEmail(email);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public void switchState(Supplier supplier) {
        if (!supplier.getDeleted()){
            supplier.setDeleted(true); // Set deleted TRUE --> Account deactivation
        }
        else{
            supplier.setDeleted(false); // Set deleted FALSE --> Account Reactivation
        }
    }
}