package com.smart_service_booking.controller;

import com.smart_service_booking.dto.ApiResponse;
import com.smart_service_booking.entity.ServiceProvider;
import com.smart_service_booking.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class PublicProviderController {

    private final ProviderService providerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceProvider>>> getApprovedProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getApprovedProviders()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceProvider>> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(providerService.getProviderById(id)));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<ServiceProvider>>> getTopRatedProviders() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getTopRatedProviders()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ServiceProvider>>> searchProviders(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(providerService.searchProviders(q)));
    }
}
