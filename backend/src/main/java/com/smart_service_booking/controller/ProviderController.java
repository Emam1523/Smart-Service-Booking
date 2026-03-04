package com.smart_service_booking.controller;

import com.smart_service_booking.dto.ApiResponse;
import com.smart_service_booking.dto.ServiceRequest;
import com.smart_service_booking.entity.*;
import com.smart_service_booking.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;
    private final ServiceService serviceService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ServiceProvider>> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(providerService.getProviderByUserId(user.getId())));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ServiceProvider>> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody ServiceProvider updatedProvider) {
        ServiceProvider provider = providerService.getProviderByUserId(user.getId());
        return ResponseEntity
                .ok(ApiResponse.success(providerService.updateProvider(provider.getId(), updatedProvider)));
    }

    // Service Management
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getMyServices(@AuthenticationPrincipal User user) {
        ServiceProvider provider = providerService.getProviderByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(serviceService.getServicesByProvider(provider.getId())));
    }

    @PostMapping("/services")
    public ResponseEntity<ApiResponse<ServiceEntity>> createService(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ServiceRequest request) {
        ServiceProvider provider = providerService.getProviderByUserId(user.getId());
        return ResponseEntity
                .ok(ApiResponse.success("Service created", serviceService.createService(provider.getId(), request)));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ApiResponse<ServiceEntity>> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Service updated", serviceService.updateService(id, request)));
    }

    @PutMapping("/services/{id}/toggle")
    public ResponseEntity<ApiResponse<Void>> toggleServiceAvailability(@PathVariable Long id) {
        serviceService.toggleAvailability(id);
        return ResponseEntity.ok(ApiResponse.success("Service availability toggled", null));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted", null));
    }

    // Booking Management 
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyBookings(@AuthenticationPrincipal User user) {
        ServiceProvider provider = providerService.getProviderByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(bookingService.getProviderBookings(provider.getId())));
    }

    @PutMapping("/bookings/{id}/accept")
    public ResponseEntity<ApiResponse<Booking>> acceptBooking(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Booking confirmed", bookingService.updateBookingStatus(id, "CONFIRMED", null)));
    }

    @PutMapping("/bookings/{id}/reject")
    public ResponseEntity<ApiResponse<Booking>> rejectBooking(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        return ResponseEntity.ok(
                ApiResponse.success("Booking rejected", bookingService.updateBookingStatus(id, "REJECTED", reason)));
    }

    @PutMapping("/bookings/{id}/complete")
    public ResponseEntity<ApiResponse<Booking>> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Service completed", bookingService.updateBookingStatus(id, "COMPLETED", null)));
    }

    // Reviews
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<List<Review>>> getMyReviews(@AuthenticationPrincipal User user) {
        ServiceProvider provider = providerService.getProviderByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProvider(provider.getId())));
    }

    // Dashboard Stats
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats(@AuthenticationPrincipal User user) {
        ServiceProvider provider = providerService.getProviderByUserId(user.getId());
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalServices", providerService.getProviderServiceCount(provider.getId()));
        stats.put("totalBookings", providerService.getProviderBookingCount(provider.getId()));
        stats.put("completedJobs", provider.getCompletedJobs() != null ? provider.getCompletedJobs() : 0);
        stats.put("averageRating", provider.getAverageRating() != null ? provider.getAverageRating() : 0.0);
        stats.put("totalReviews", provider.getTotalReviews() != null ? provider.getTotalReviews() : 0);
        stats.put("status", provider.getStatus() != null ? provider.getStatus().name() : "PENDING");
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
