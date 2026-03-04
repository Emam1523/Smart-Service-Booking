package com.smart_service_booking.entity;

import com.smart_service_booking.enums.ProviderStatus;
import com.smart_service_booking.enums.ServiceCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "service_providers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"password", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "banned"})
    private User user;

    @Column(nullable = false)
    private String businessName;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceCategory primaryCategory;

    private String experienceYears;
    private String serviceArea;
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProviderStatus status = ProviderStatus.PENDING;

    @Builder.Default
    private Double averageRating = 0.0;
    @Builder.Default
    private Integer totalReviews = 0;
    @Builder.Default
    private Double totalEarnings = 0.0;
    @Builder.Default
    private Integer completedJobs = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceEntity> services;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
