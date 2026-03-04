package com.smart_service_booking.service;

import com.smart_service_booking.entity.ServiceProvider;
import com.smart_service_booking.enums.ProviderStatus;
import com.smart_service_booking.exception.ResourceNotFoundException;
import com.smart_service_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ServiceProviderRepository providerRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ServiceProvider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + id));
    }

    public ServiceProvider getProviderByUserId(Long userId) {
        return providerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found for user id: " + userId));
    }

    public List<ServiceProvider> getAllProviders() {
        return providerRepository.findAll();
    }

    public List<ServiceProvider> getApprovedProviders() {
        return providerRepository.findByStatus(ProviderStatus.APPROVED);
    }

    public List<ServiceProvider> getPendingProviders() {
        return providerRepository.findByStatus(ProviderStatus.PENDING);
    }

    public List<ServiceProvider> getTopRatedProviders() {
        return providerRepository.findTopRatedProviders();
    }

    public List<ServiceProvider> searchProviders(String keyword) {
        return providerRepository.searchProviders(keyword);
    }

    public ServiceProvider updateProvider(Long id, ServiceProvider updatedProvider) {
        ServiceProvider provider = getProviderById(id);
        provider.setBusinessName(updatedProvider.getBusinessName());
        provider.setDescription(updatedProvider.getDescription());
        provider.setPrimaryCategory(updatedProvider.getPrimaryCategory());
        provider.setExperienceYears(updatedProvider.getExperienceYears());
        provider.setServiceArea(updatedProvider.getServiceArea());
        provider.setCity(updatedProvider.getCity());
        return providerRepository.save(provider);
    }

    public ServiceProvider approveProvider(Long id) {
        ServiceProvider provider = getProviderById(id);
        provider.setStatus(ProviderStatus.APPROVED);
        providerRepository.save(provider);
        return provider;
    }

    public ServiceProvider rejectProvider(Long id) {
        ServiceProvider provider = getProviderById(id);
        provider.setStatus(ProviderStatus.REJECTED);
        providerRepository.save(provider);
        return provider;
    }

    public ServiceProvider banProvider(Long id) {
        ServiceProvider provider = getProviderById(id);
        provider.setStatus(ProviderStatus.BANNED);
        provider.getUser().setBanned(true);
        providerRepository.save(provider);
        return provider;
    }

    public long getProviderBookingCount(Long providerId) {
        return bookingRepository.countByProviderId(providerId);
    }

    public long getProviderServiceCount(Long providerId) {
        return serviceRepository.countByProviderId(providerId);
    }

    @Transactional
    public void deleteProvider(Long id) {
        ServiceProvider provider = getProviderById(id);
        // Delete reviews, bookings, services first due to FK constraints
        reviewRepository.deleteByProviderId(id);
        bookingRepository.deleteByProviderId(id);
        serviceRepository.deleteByProviderId(id);
        providerRepository.delete(provider);
        // Also delete the associated user account
        userRepository.delete(provider.getUser());
    }
}
