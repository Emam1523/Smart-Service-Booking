package com.smart_service_booking.repository;

import com.smart_service_booking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProviderId(Long providerId);

    List<Review> findByUserId(Long userId);

    List<Review> findByServiceId(Long serviceId);

    Optional<Review> findByBookingId(Long bookingId);

    boolean existsByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.provider.id = ?1")
    Double getAverageRatingByProviderId(Long providerId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.service.id = ?1")
    Double getAverageRatingByServiceId(Long serviceId);

    long countByProviderId(Long providerId);

    List<Review> findByProviderIdOrderByCreatedAtDesc(Long providerId);

    void deleteByProviderId(Long providerId);
}
