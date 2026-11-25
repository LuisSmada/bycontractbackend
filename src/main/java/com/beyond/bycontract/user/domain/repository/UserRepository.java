package com.beyond.bycontract.user.domain.repository;

import com.beyond.bycontract.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	User createUser(User user);

	Optional<User> getUserById(UUID id);

	Optional<User> getUserByEmail(String email);

	void deleteUserById(UUID id);

	User updateUser(User user);

	void changeUserPassword(User user);

	List<User> getUsersByIds(Collection<UUID> ids);

	Optional<User> findByStripeIdSubscription(String idSubscription);

	Optional<User> findByStripeIdCustomer(String idCustomer);

	List<User> findUsersInTrialEndingBetween(LocalDateTime start, LocalDateTime end);

	List<User> findUsersWithExpiredTrials(LocalDateTime date);

	Optional<User> findByStripeIdCustomerForUpdate (String stripeIdCustomer);

	Optional<User> findByStripeIdSubscriptionForUpdate (String stripeIdSubscription);

	Optional<User> getUserByIdWithLock(UUID id);
}
