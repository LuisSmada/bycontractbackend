package com.beyond.bycontract.domain.repository;

import com.beyond.bycontract.domain.model.User;

public interface UserRepository {

    public User createUser(User user);
}
