package com.beyond.bycontract.user.infrastructure.repository;

import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import com.beyond.bycontract.user.infrastructure.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

	private final SpringDataUserRepository springDataUserRepository;
	private final UserPersistenceMapper mapper;

	public User createUser(User user) {
		UserEntity userEntity = mapper.domainToEntity(user);
		return mapper.entityToDomain(springDataUserRepository.save(userEntity));
	}

	@Override
	public Optional<User> getUserById(UUID id) {
		return springDataUserRepository.findById(id).map(mapper::entityToDomain);
	}

	@Override
	public void deleteUserById(UUID id) {
		springDataUserRepository.deleteById(id);
	}

	@Override
	public User updateUser(User user) {
		UserEntity userEntity =  springDataUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException(user.getId()));
		mapper.updateEntityFromDomain(user, userEntity);
		UserEntity savedEntity = springDataUserRepository.save(userEntity);
		return mapper.entityToDomain(savedEntity);
	}

	@Override
	public void changeUserPassword(User user) {
		UserEntity userEntity =  springDataUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException(user.getId()));
		userEntity.setPassword(user.getPassword());
		springDataUserRepository.save(userEntity);
	}


	@Override
	public List<User> getUsersByIds(Collection<UUID> ids) {
		return springDataUserRepository.findAllById(ids).stream().map(mapper::entityToDomain).toList();
	}

	public Optional<User> getUserByEmail(String email) {
		return springDataUserRepository.findByEmail(email).map(mapper::entityToDomain);
	}

	public Optional<User> findByStripeIdSubscription(String idSubscription) {
		return springDataUserRepository.findByStripeIdSubscription(idSubscription).map(mapper::entityToDomain);
	}

	public Optional<User> findByStripeIdCustomer(String idCustomer) {
		return springDataUserRepository.findByStripeIdCustomer(idCustomer).map(mapper::entityToDomain);
	}

	@Override
	public List<User> findUsersInTrialEndingBetween(LocalDateTime start, LocalDateTime end) {
		return springDataUserRepository.findByStripeSubscriptionStatusAndTrialEndDateBetween("trialing", start, end).stream().map(mapper::entityToDomain).toList();
	}

	@Override
	public List<User> findUsersWithExpiredTrials(LocalDateTime date) {
		return springDataUserRepository.findByStripeSubscriptionStatusAndTrialEndDateBefore("trialing", date).stream().map(mapper::entityToDomain).toList();
	}

	@Override
	public Optional<User> findByStripeIdCustomerForUpdate(String stripeIdCustomer) {
		return springDataUserRepository.findByStripeIdCustomerForUpdate(stripeIdCustomer).map(mapper::entityToDomain);
	}

	@Override
	public Optional<User> findByStripeIdSubscriptionForUpdate(String stripeIdSubscription) {
		return springDataUserRepository.findByStripeIdSubscriptionForUpdate(stripeIdSubscription).map(mapper::entityToDomain);
	}

	@Override
	public Optional<User> getUserByIdWithLock(UUID id) {
		return springDataUserRepository.findByIdWithLock(id).map(mapper::entityToDomain);
	}

}
