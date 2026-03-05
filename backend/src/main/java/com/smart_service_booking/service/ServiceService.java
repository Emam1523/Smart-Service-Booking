package com.smart_service_booking.service;

import com.smart_service_booking.dto.ServiceRequest;
import com.smart_service_booking.entity.ServiceEntity;
import com.smart_service_booking.entity.ServiceProvider;
import com.smart_service_booking.enums.ServiceCategory;
import com.smart_service_booking.exception.ResourceNotFoundException;
import com.smart_service_booking.repository.ServiceProviderRepository;
import com.smart_service_booking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceProviderRepository providerRepository;

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findByAvailableTrue();
    }

    public ServiceEntity getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
    }

    public List<ServiceEntity> getServicesByProvider(Long providerId) {
        return serviceRepository.findByProviderId(providerId);
    }

    public List<ServiceEntity> getServicesByCategory(String category) {
        ServiceCategory cat = ServiceCategory.valueOf(category.toUpperCase());
        return serviceRepository.findAvailableByCategory(cat);
    }

    public List<ServiceEntity> searchServices(String keyword) {
        return serviceRepository.searchServices(keyword);
    }

    public List<ServiceEntity> getFeaturedServices() {
        return serviceRepository.findByFeaturedTrue();
    }

    public List<ServiceEntity> getTopRatedServices() {
        return serviceRepository.findTopRatedServices();
    }

    public List<ServiceEntity> getServicesByPriceRange(Double min, Double max) {
        return serviceRepository.findByPriceRange(min, max);
    }

    public ServiceEntity createService(Long providerId, ServiceRequest request) {
        ServiceProvider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        ServiceCategory category = ServiceCategory.valueOf(request.getCategory().toUpperCase());

        ServiceEntity service = ServiceEntity.builder()
                .provider(provider)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .price(request.getPrice())
                .duration(request.getDuration())
                .serviceArea(request.getServiceArea())
                .image(request.getImage())
                .available(true)
                .averageRating(0.0)
                .totalBookings(0)
                .build();

        return serviceRepository.save(service);
    }

    public ServiceEntity updateService(Long id, ServiceRequest request) {
        ServiceEntity service = getServiceById(id);
        service.setTitle(request.getTitle());
        service.setDescription(request.getDescription());
        service.setCategory(ServiceCategory.valueOf(request.getCategory().toUpperCase()));
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setServiceArea(request.getServiceArea());
        if (request.getImage() != null) {
            service.setImage(request.getImage());
        }
        return serviceRepository.save(service);
    }

    public void toggleAvailability(Long id) {
        ServiceEntity service = getServiceById(id);
        service.setAvailable(!service.isAvailable());
        serviceRepository.save(service);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    public List<ServiceEntity> getAllServicesAdmin() {
        return serviceRepository.findAll();
    }
}
