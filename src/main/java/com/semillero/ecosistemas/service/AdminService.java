package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService{
    @Autowired
    IAdminRepository adminRepository;

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> findAdminById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public void switchState(Admin admin) {
        if (!admin.getDeleted()){
            admin.setDeleted(true); // Set deleted TRUE --> Account deactivation
        }
        else{
            admin.setDeleted(false); // Set deleted FALSE --> Account Reactivation
        }
    }
}