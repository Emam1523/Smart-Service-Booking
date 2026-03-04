package com.smart_service_booking.controller;

import com.smart_service_booking.dto.ApiResponse;
import com.smart_service_booking.dto.ServiceRequest;
import com.smart_service_booking.entity.ServiceEntity;
import com.smart_service_booking.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getAllServices() {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getAllServices()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceEntity>> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getServiceById(id)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getServicesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getServicesByCategory(category)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> searchServices(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(serviceService.searchServices(q)));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getFeaturedServices() {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getFeaturedServices()));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getTopRatedServices() {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getTopRatedServices()));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getServicesByPriceRange(
            @RequestParam Double min, @RequestParam Double max) {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getServicesByPriceRange(min, max)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<ServiceEntity>>> getServicesByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(serviceService.getServicesByProvider(providerId)));
    }
}
