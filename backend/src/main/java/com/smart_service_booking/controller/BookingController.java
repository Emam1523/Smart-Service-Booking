package com.smart_service_booking.controller;

import com.smart_service_booking.dto.ApiResponse;
import com.smart_service_booking.dto.BookingRequest;
import com.smart_service_booking.dto.ReviewRequest;
import com.smart_service_booking.entity.Booking;
import com.smart_service_booking.entity.Review;
import com.smart_service_booking.entity.User;
import com.smart_service_booking.service.BookingService;
import com.smart_service_booking.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", booking));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Booking>>> getMyBookings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getUserBookings(user.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingById(id)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        Booking booking = bookingService.updateBookingStatus(id, "CANCELLED", reason);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled", booking));
    }

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<Review>> createReview(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ReviewRequest request) {
        Review review = reviewService.createReview(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Review submitted", review));
    }

    @GetMapping("/my/reviews")
    public ResponseEntity<ApiResponse<List<Review>>> getMyReviews(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByUser(user.getId())));
    }
}
