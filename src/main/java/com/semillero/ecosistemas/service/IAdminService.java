package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.model.User;

import java.util.List;
import java.util.Optional;

public interface IAdminService {
    //Create
    public Admin saveAdmin(Admin admin);

    //Find
    public Optional<Admin> findAdminById(Long id);
    public Optional<Admin> findAdminByEmail(String email);

    //Read
    public List<Admin> getAllAdmins();

    //Update (Change State --> deleted)
    public void switchState(Admin admin);
}