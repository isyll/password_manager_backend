package com.isyll.password_manager.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.isyll.password_manager.config.i18n.I18nUtil;
import com.isyll.password_manager.config.security.jwt.JwtUtils;
import com.isyll.password_manager.dto.payload.request.UpdateUserRequest;
import com.isyll.password_manager.exceptions.BadRequestException;
import com.isyll.password_manager.exceptions.InvalidTokenException;
import com.isyll.password_manager.exceptions.ResourceNotFoundException;
import com.isyll.password_manager.exceptions.UniqueConstraintViolationException;
import com.isyll.password_manager.models.AccountStatus;
import com.isyll.password_manager.models.ERole;
import com.isyll.password_manager.models.Role;
import com.isyll.password_manager.models.User;
import com.isyll.password_manager.repos.RoleRepository;
import com.isyll.password_manager.repos.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    I18nUtil i18nUtil;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public User createUser(User user) throws UniqueConstraintViolationException {
        checkUniqueConstraintsViolation(user);

        Role role = roleRepository.findByName(ERole.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(User user, UpdateUserRequest dataRequest)
            throws UniqueConstraintViolationException {
        checkUniqueConstraintsViolation(user, dataRequest);

        if (dataRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return user.get();
    }

    public void deleteMyAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                () -> new RuntimeException());

        user.setStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    public String generateAccessToken(String username, String password) {
        Authentication authentication = generateAuthentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.generateAccessToken(authentication);
    }

    public String generateRefreshToken(String username, String password) {
        Authentication authentication = generateAuthentication(username, password);
        return jwtUtils.generateRefreshToken(authentication);
    }

    public String generateFromRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException();
        }

        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.generateAccessToken(authentication);
    }

    private Authentication generateAuthentication(String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, password);
        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void checkUniqueConstraintsViolation(User user) throws UniqueConstraintViolationException {
        String localizedMessage;
        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByEmail(user.getEmail())) {
            localizedMessage = i18nUtil.getMessage(
                    "validation.user.email_already_exists", user.getEmail());
            errors.put("email", localizedMessage);
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            localizedMessage = i18nUtil.getMessage(
                    "validation.user.phone_already_exists", user.getPhone());
            errors.put("phone", localizedMessage);
        }

        if (!errors.isEmpty()) {
            throw new UniqueConstraintViolationException(errors);
        }
    }

    private void checkUniqueConstraintsViolation(User user, UpdateUserRequest dataRequest)
            throws UniqueConstraintViolationException {
        String localizedMessage;
        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByEmailAndNotExcludedUserId(user.getEmail(), user.getId())) {
            localizedMessage = i18nUtil.getMessage(
                    "validation.user.email_already_exists", user.getEmail());
            errors.put("email", localizedMessage);
        }

        if (userRepository.existsByPhoneAndNotExcludedUserId(user.getPhone().trim().replaceAll("\\s", ""),
                user.getId())) {
            localizedMessage = i18nUtil.getMessage(
                    "validation.user.phone_already_exists", user.getPhone());
            errors.put("phone", localizedMessage);
        }

        if (!errors.isEmpty()) {
            throw new UniqueConstraintViolationException(errors);
        }
    }
}
