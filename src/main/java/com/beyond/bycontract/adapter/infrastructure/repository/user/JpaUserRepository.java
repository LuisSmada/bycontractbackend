package com.beyond.bycontract.adapter.infrastructure.repository.user;

import com.beyond.bycontract.adapter.infrastructure.entity.UserEntity;
import com.beyond.bycontract.adapter.infrastructure.mapper.UserMapper;
import com.beyond.bycontract.domain.model.User;
import com.beyond.bycontract.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    public User createUser(User user) {
        UserEntity userEntity = UserMapper.domainToEntity(user);
        return UserMapper.entityToDomain(springDataUserRepository.save(userEntity));
    }

    @Override
    public Optional<User> getUserByid(Long id) {
        return springDataUserRepository.findById(id).map(UserMapper::entityToDomain);
    }

    @Override
    public void deleteUserById(Long id) {
        springDataUserRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long idUserToUpdate, User user) {
        User userFound = springDataUserRepository.findById(idUserToUpdate).map(UserMapper::entityToDomain).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getFirstName() != null) { userFound.setFirstName(user.getFirstName()); }
        if(user.getLastName() != null) { userFound.setLastName(user.getLastName()); }
        if(user.getEmail() != null) { userFound.setEmail(user.getEmail()); }
        if(user.getPassword() != null) { userFound.setPassword(user.getPassword()); }

        UserEntity userEntity = UserMapper.domainToEntity(userFound);

        return UserMapper.entityToDomain(springDataUserRepository.save(userEntity));
    }
}
