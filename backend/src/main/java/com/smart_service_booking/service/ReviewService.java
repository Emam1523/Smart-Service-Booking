package com.smart_service_booking.service;

import com.smart_service_booking.dto.ReviewRequest;
import com.smart_service_booking.entity.*;
import com.smart_service_booking.enums.BookingStatus;
import com.smart_service_booking.exception.ResourceNotFoundException;
import com.smart_service_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ServiceProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Transactional
    public Review createReview(Long userId, ReviewRequest request) {
        if (reviewRepository.existsByBookingId(request.getBookingId())) {
            throw new IllegalArgumentException("Review already exists for this booking");
        }

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalArgumentException("Can only review completed bookings");
        }

        if (!booking.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only review your own bookings");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = Review.builder()
                .user(user)
                .provider(booking.getProvider())
                .booking(booking)
                .service(booking.getService())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        review = reviewRepository.save(review);

        // Update provider rating
        updateProviderRating(booking.getProvider().getId());

        // Update service rating
        updateServiceRating(booking.getService().getId());

        return review;
    }

    private void updateProviderRating(Long providerId) {
        Double avgRating = reviewRepository.getAverageRatingByProviderId(providerId);
        long totalReviews = reviewRepository.countByProviderId(providerId);

        ServiceProvider provider = providerRepository.findById(providerId).orElse(null);
        if (provider != null) {
            provider.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
            provider.setTotalReviews((int) totalReviews);
            providerRepository.save(provider);
        }
    }

    private void updateServiceRating(Long serviceId) {
        Double avgRating = reviewRepository.getAverageRatingByServiceId(serviceId);
        ServiceEntity service = serviceRepository.findById(serviceId).orElse(null);
        if (service != null) {
            service.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
            serviceRepository.save(service);
        }
    }

    public List<Review> getReviewsByProvider(Long providerId) {
        return reviewRepository.findByProviderIdOrderByCreatedAtDesc(providerId);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<Review> getReviewsByService(Long serviceId) {
        return reviewRepository.findByServiceId(serviceId);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
