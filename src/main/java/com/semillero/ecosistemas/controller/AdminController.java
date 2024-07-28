package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    @Autowired
    IAdminService adminService;

    @PostMapping
    public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin) {
        Admin newAdmin = adminService.saveAdmin(admin);
        return ResponseEntity.ok(newAdmin);
    }

    @GetMapping
    public List<Admin> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @GetMapping("/find/{email}")
    public ResponseEntity<Admin> getAdminByEmail(@PathVariable String email){
        Optional<Admin> optionalAdmin = adminService.findAdminByEmail(email);
        return optionalAdmin.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/changestate/{id}")
    public ResponseEntity<Admin> switchAdminState(@PathVariable Long id) {
        Optional<Admin> adminOptional = adminService.findAdminById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            adminService.switchState(admin);
            adminService.saveAdmin(admin);
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}