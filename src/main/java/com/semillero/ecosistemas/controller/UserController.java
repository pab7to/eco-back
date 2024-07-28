package com.semillero.ecosistemas.controller;

import com.semillero.ecosistemas.model.User;
import com.semillero.ecosistemas.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers(){
        List<User> allUsers = userService.findAllUsers();
        List<User> requestUsers = new ArrayList<>();
        for(User user : allUsers){
            if(user.getRole().toString().equals("USER")){
                requestUsers.add(user);
            }
        }

        return requestUsers;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/find/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        Optional<User> optionalUser = userService.findUserByEmail(email);
        return optionalUser.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/changestate/{id}")
    public ResponseEntity<User> switchUserState(@PathVariable Long id) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.switchState(user);
            userService.saveUser(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}