package com.beyond.bycontract.user.infrastructure.entity;

import com.beyond.bycontract.user.domain.model.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private UUID id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "passwd", nullable = false)
	private String password;

	@CreationTimestamp //To tell to hibernate to generate the date automatically
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole userRole = UserRole.USER; //By default a new user is a simple user

	@Column(name="stripe_id_customer", unique = true)
	private String stripeIdCustomer;

	@Column(name="stripe_id_subscription", unique = true)
	private String stripeIdSubscription;

	@Column(name="stripe_subscription_status")
	private String stripeSubscriptionStatus;

	@Column(name = "trial_end_date")
	private LocalDateTime trialEndDate;

	@Column(name="card_brand")
	private String cardBrand;

	@Column(name="card_last_4")
	private String cardLast4;

	@Column(name="plan_name")
	private String planName;

	@Column(name="plan_price")
	private Double planPrice;

	@Column(name="current_period_end")
	private Long currentPeriodEnd;

	public UserEntity() {
	}

	@Override
	public String toString() {
		return "UserEntity{" +
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
