package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.User;
import com.semillero.ecosistemas.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void switchState(User user) {
        if (!user.getDeleted()){
            user.setDeleted(true); // Set deleted TRUE --> Account deactivation
        }
        else{
            user.setDeleted(false); // Set deleted FALSE --> Account Reactivation
        }
    }
}