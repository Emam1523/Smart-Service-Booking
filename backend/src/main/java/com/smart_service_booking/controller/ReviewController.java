package com.smart_service_booking.controller;

import com.smart_service_booking.dto.ApiResponse;
import com.smart_service_booking.entity.Review;
import com.smart_service_booking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProvider(providerId)));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByService(serviceId)));
    }
}
