package com.beyond.bycontract.adapter.infrastructure.repository.user;

import com.beyond.bycontract.adapter.infrastructure.entity.UserEntity;
import com.beyond.bycontract.adapter.infrastructure.mapper.UserMapper;
import com.beyond.bycontract.domain.model.User;
import com.beyond.bycontract.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements UserRepository {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    public User createUser(User user) {
        UserEntity userEntity = UserMapper.domainToEntity(user);
        return UserMapper.entityToDomain(springDataUserRepository.save(userEntity));
    }
}
