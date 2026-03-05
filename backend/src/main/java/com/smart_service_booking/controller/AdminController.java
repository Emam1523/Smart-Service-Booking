package com.smart_service_booking.controller;

import com.smart_service_booking.dto.ApiResponse;
import com.smart_service_booking.dto.DashboardStats;
import com.smart_service_booking.entity.*;
import com.smart_service_booking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final ProviderService providerService;
    private final ServiceService serviceService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboardStats()));
    }

    // User Management
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(userService.searchUsers(q)));
    }

    @PutMapping("/users/{id}/ban")
    public ResponseEntity<ApiResponse<Void>> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok(ApiResponse.success("User banned", null));
    }

    @PutMapping("/users/{id}/unban")
    public ResponseEntity<ApiResponse<Void>> unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return ResponseEntity.ok(ApiResponse.success("User unbanned", null));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    // Provider Management
    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<List<ServiceProvider>>> getAllProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getAllProviders()));
    }

    @GetMapping("/providers/pending")
    public ResponseEntity<ApiResponse<List<ServiceProvider>>> getPendingProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getPendingProviders()));
    }

    @PutMapping("/providers/{id}/approve")
    public ResponseEntity<ApiResponse<ServiceProvider>> approveProvider(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Provider approved", providerService.approveProvider(id)));
    }

    @PutMapping("/providers/{id}/reject")
    public ResponseEntity<ApiResponse<ServiceProvider>> rejectProvider(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Provider rejected", providerService.rejectProvider(id)));
    }

    @PutMapping("/providers/{id}/ban")
    public ResponseEntity<ApiResponse<ServiceProvider>> banProvider(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Provider banned", providerService.banProvider(id)));
    }

    @DeleteMapping("/providers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return ResponseEntity.ok(ApiResponse.success("Provider deleted", null));
    }

    // Service Management
    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getAllServices() {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getAllServicesAdmin()));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted", null));
    }

    // Booking Management
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getAllBookings()));
    }

    @GetMapping("/bookings/status/{status}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingsByStatus(status)));
    }

    // Review Management
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<List<Review>>> getAllReviews() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews()));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review deleted", null));
    }
}
