package com.isyll.password_manager.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.isyll.password_manager.models.AccountStatus;
import com.isyll.password_manager.models.Permission;
import com.isyll.password_manager.models.Role;
import com.isyll.password_manager.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    public static UserDetailsImpl build(User user) {
        Collection<GrantedAuthority> authorities = getGrantedAuthorities(user.getRoles());

        return new UserDetailsImpl(user.getId(),
                user.getPassword(), user.getEmail(), user.getPhone(), user.getDateOfBirth(),
                user.getFirstName(), user.getLastName(), user.getCreatedAt(), user.getUpdatedAt(),
                user.isEmailVerified(), user.getCountryCode(), authorities);
    }

    private static Collection<String> getPermissions(Collection<Role> roles) {
        Collection<String> permissions = new ArrayList<>();
        Collection<Permission> collection = new ArrayList<>();
        for (Role role : roles) {
            permissions.add(role.getName().name());
            collection.addAll(role.getPermissions());
        }
        for (Permission item : collection) {
            permissions.add(item.getName().name());
        }
        return permissions;
    }

    private static Collection<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        Collection<String> permissions = getPermissions(roles);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : permissions) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private Long id;

    private String password;

    private String email;

    private String phone;

    private ZonedDateTime dateOfBirth;

    private String firstName;

    private String lastName;

    private boolean emailVerified;

    private String countryCode;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private AccountStatus status = AccountStatus.ACTIVE;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            String password,
            String email,
            String phone,
            ZonedDateTime dateOfBirth,
            String firstName,
            String lastName,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            boolean emailVerified,
            String countryCode,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.emailVerified = emailVerified;
        this.countryCode = countryCode;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
