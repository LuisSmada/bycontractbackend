package com.beyond.bycontract.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class User {
	private UUID id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private UserRole role;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String stripeIdCustomer;
	private String stripeIdSubscription;
	private String stripeSubscriptionStatus;
	private LocalDateTime trialEndDate;
	private String cardBrand;
	private String cardLast4;
	private String planName;
	private Double planPrice;
	private Long currentPeriodEnd;

	public User() {
	}

	//THE ID AND ROLE ARE NULL BECAUSE THE GENERATION OF THE ID IS HANDLED BY THE DATABASE
	public static User create(String firstName, String lastName, String email, String password) {
		return new User(
				null,
				firstName,
				lastName,
				email,
				password,
				UserRole.USER,
				LocalDateTime.now(),
				LocalDateTime.now(),
				null,
				null,
				"trialing",
				LocalDateTime.now().plusDays(30),
				null,
				null,
				null,
				null,
				null
		);
	}

	public void updateUser(String newFirstName, String newLastName, String newEmail) {
		if(!newFirstName.trim().isEmpty()) {
			this.firstName = newFirstName;
		}
		if(!newLastName.trim().isEmpty()) {
			this.lastName = newLastName;
		}
		if(!newEmail.trim().isEmpty()) {
			this.email = newEmail;
		}
	}

	public void changePassword(String newPassword) {
		if(!newPassword.trim().isEmpty()) {
			this.password = newPassword;
		}
	}

	public void activateSubscription(String idCustomer, String idSubscription) {
		if(idCustomer == null || idSubscription == null) {
			throw new IllegalArgumentException("Les identifiants Stripe sont requis pour activer l'abonnement.");
		}
		this.stripeIdCustomer = idCustomer;
		this.stripeIdSubscription = idSubscription;
		this.stripeSubscriptionStatus = "active";
	}

	public void updateSubscriptionStatus(String newStatus) {
		this.stripeSubscriptionStatus = newStatus;
	}

	public void updateIdSubscription(String idSubscription) {
		this.stripeIdSubscription = idSubscription;
	}

	public void updateIdCustomer(String idCustomer) {
		this.stripeIdCustomer = idCustomer;
	}

	public void cancelSubscription() {
		this.stripeSubscriptionStatus = "canceled";
		this.stripeIdSubscription = null;
	}

	public void markPaymentFailed() {
		this.stripeSubscriptionStatus = "past_due";
	}

	public boolean hasActiveSubscription() {
		if (this.role == UserRole.SUPER_ADMIN) {
			return true; // Le SUPER_ADMIN a toujours accès
		}
		return "active".equals(this.stripeSubscriptionStatus) ||
				"trialing".equals(this.stripeSubscriptionStatus);
	}

	public boolean isTrialExpired() {
		return "trialing".equals(this.stripeSubscriptionStatus) && this.trialEndDate != null && this.trialEndDate.isBefore(LocalDateTime.now());
	}

	public void expireTrial() {
		if(isTrialExpired()) {
			this.stripeSubscriptionStatus = "inactive";
		}
	}

	public void setCardData(String cardBrand, String cardLast4) {
		this.cardBrand = cardBrand;
		this.cardLast4 = cardLast4;
	}

	public void removeCardData() {
		this.cardBrand = null;
		this.cardLast4 = null;
	}

	public void setPlanData(String planName, Double planPrice, Long currentPeriodEnd) {
		this.planName = planName;
		this.planPrice = planPrice;
		this.currentPeriodEnd = currentPeriodEnd;
	}

	public void removePlanData() {
		this.planName = null;
		this.planPrice = null;
	}

	public void updatePeriodEnd(Long periodEnd) {
		this.currentPeriodEnd = periodEnd;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", createdAt=" + createdAt +
				", modifiedAt=" + modifiedAt +
				'}';
	}
}
