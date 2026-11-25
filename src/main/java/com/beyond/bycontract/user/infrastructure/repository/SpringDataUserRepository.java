package com.beyond.bycontract.user.infrastructure.repository;

import com.beyond.bycontract.user.infrastructure.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByStripeIdSubscription(String stripeIdSubscription);

	Optional<UserEntity> findByStripeIdCustomer(String stripeIdCustomer);

	List<UserEntity> findByStripeSubscriptionStatusAndTrialEndDateBetween(String status, LocalDateTime startOfDay, LocalDateTime endOfDay);

	List<UserEntity> findByStripeSubscriptionStatusAndTrialEndDateBefore(String status, LocalDateTime date);


	//CES FONCTION LOCK (PESSIMISTIC LOCKING) SONT LA POUR EVITER LES MISES A JOUR PERDUES
	// SI STRIPE ENVOIT 2 WEBSHOOKS A LA MEME SECONDE
	//CA VA DIRE A SPRING, QUAND TU LIS CETTE LIGNE, SI UN AUTRE THREAD ESSAIE DE LA LIRE POUR LA MODIFIER,
	//FAIS LE PATIENTER JUSQU A CE QUE CE SOIT TERMINE
	// Le PESSIMISTIC_WRITE va générer un "SELECT ... FOR UPDATE" en SQL
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM UserEntity u WHERE u.stripeIdCustomer = :stripeId")
	Optional<UserEntity> findByStripeIdCustomerForUpdate(@Param("stripeId") String stripeId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM UserEntity u WHERE u.stripeIdSubscription = :stripeIdsubscription")
	Optional<UserEntity> findByStripeIdSubscriptionForUpdate(@Param("subId") String stripeIdSubscription);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT u FROM UserEntity u WHERE u.id = :id")
	Optional<UserEntity> findByIdWithLock(@Param("id") UUID id);
}
