package com.beyond.bycontract.domain.repository;

import com.beyond.bycontract.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    Optional<User> getUserByid(Long id);

    void deleteUserById(Long id);

    User updateUser( Long idUserToUpdate ,User user);
}
