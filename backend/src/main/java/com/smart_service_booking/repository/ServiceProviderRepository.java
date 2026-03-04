package com.smart_service_booking.repository;

import com.smart_service_booking.entity.ServiceProvider;
import com.smart_service_booking.enums.ProviderStatus;
import com.smart_service_booking.enums.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    Optional<ServiceProvider> findByUserId(Long userId);

    List<ServiceProvider> findByStatus(ProviderStatus status);

    List<ServiceProvider> findByPrimaryCategory(ServiceCategory category);

    List<ServiceProvider> findByCityIgnoreCase(String city);

    long countByStatus(ProviderStatus status);

    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.status = 'APPROVED' AND " +
           "(sp.businessName LIKE %?1% OR sp.city LIKE %?1%)")
    List<ServiceProvider> searchProviders(String keyword);

    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.status = 'APPROVED' ORDER BY sp.averageRating DESC")
    List<ServiceProvider> findTopRatedProviders();
}
