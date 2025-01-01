package com.isyll.password_manager.models;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.isyll.password_manager.utils.DateTimeUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@DynamicUpdate
@Table(name = "users")
@SQLDelete(sql = "UPDATE User SET deleted = true WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @Column(unique = true, nullable = false, length = 250)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 120)
    private String password;

    @Column(unique = true, nullable = false, length = 30)
    private String phone;

    @JsonProperty("country_code")
    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;

    @JsonProperty("date_of_birth")
    @Column(nullable = false)
    private ZonedDateTime dateOfBirth;

    @JsonProperty("first_name")
    @Column(nullable = false, length = 250)
    private String firstName;

    @JsonProperty("last_name")
    @Column(nullable = false, length = 250)
    private String lastName;

    @JsonProperty(access = Access.READ_ONLY)
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @JsonProperty(access = Access.READ_ONLY)
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @JsonIgnore
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @PrePersist
    public void onCreate() {
        createdAt = updatedAt = DateTimeUtils.getCurrentTimestamp();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = DateTimeUtils.getCurrentTimestamp();
    }
}
