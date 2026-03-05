package com.smart_service_booking.service;

import com.smart_service_booking.config.JwtService;
import com.smart_service_booking.dto.AuthRequest;
import com.smart_service_booking.dto.AuthResponse;
import com.smart_service_booking.dto.RegisterRequest;
import com.smart_service_booking.entity.ServiceProvider;
import com.smart_service_booking.entity.User;
import com.smart_service_booking.enums.ProviderStatus;
import com.smart_service_booking.enums.Role;
import com.smart_service_booking.enums.ServiceCategory;
import com.smart_service_booking.repository.ServiceProviderRepository;
import com.smart_service_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ServiceProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Role role = Role.USER;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("PROVIDER")) {
            role = Role.PROVIDER;
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .role(role)
                .active(true)
                .build();
        userRepository.save(user);

        Long providerId = null;
        if (role == Role.PROVIDER) {
            ServiceCategory category = ServiceCategory.OTHER;
            try {
                if (request.getPrimaryCategory() != null) {
                    category = ServiceCategory.valueOf(request.getPrimaryCategory().toUpperCase());
                }
            } catch (IllegalArgumentException ignored) {}

            ServiceProvider provider = ServiceProvider.builder()
                    .user(user)
                    .businessName(request.getBusinessName() != null ? request.getBusinessName() : request.getFullName())
                    .description(request.getDescription())
                    .primaryCategory(category)
                    .experienceYears(request.getExperienceYears())
                    .serviceArea(request.getServiceArea())
                    .city(request.getCity())
                    .status(ProviderStatus.PENDING)
                    .averageRating(0.0)
                    .totalReviews(0)
                    .completedJobs(0)
                    .totalEarnings(0.0)
                    .build();
            providerRepository.save(provider);
            providerId = provider.getId();
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .role(role.name())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .providerId(providerId)
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isBanned()) {
            throw new IllegalArgumentException("Your account has been suspended");
        }

        String token = jwtService.generateToken(user);

        Long providerId = null;
        if (user.getRole() == Role.PROVIDER) {
            providerId = providerRepository.findByUserId(user.getId())
                    .map(ServiceProvider::getId)
                    .orElse(null);
        }

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .providerId(providerId)
                .build();
    }
}
