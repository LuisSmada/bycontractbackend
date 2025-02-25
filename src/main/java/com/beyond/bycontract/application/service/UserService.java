package com.beyond.bycontract.application.service;

import com.beyond.bycontract.domain.model.User;
import com.beyond.bycontract.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    public Optional<User> getUserById (Long id) {
        return userRepository.getUserByid(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    public User updateUser(Long idUserToUpdate, User user) {
        return userRepository.updateUser(idUserToUpdate, user);
    }
}
