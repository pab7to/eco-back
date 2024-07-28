package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    //Create
    public User saveUser(User user);

    //Find
    public Optional<User> findUserById(Long id);
    public Optional<User> findUserByEmail(String email);

    //Read
    public List<User> findAllUsers();

    //Update (Change State --> deleted)
    public void switchState(User user);
}