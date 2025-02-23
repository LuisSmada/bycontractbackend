package com.beyond.bycontract.application.service;

import com.beyond.bycontract.domain.model.User;
import com.beyond.bycontract.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.createUser(user);
    }
}
