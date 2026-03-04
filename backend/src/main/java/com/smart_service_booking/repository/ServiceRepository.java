package com.smart_service_booking.repository;

import com.smart_service_booking.entity.ServiceEntity;
import com.smart_service_booking.enums.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findByProviderId(Long providerId);

    List<ServiceEntity> findByCategory(ServiceCategory category);

    List<ServiceEntity> findByAvailableTrue();

    List<ServiceEntity> findByFeaturedTrue();

    @Query("SELECT s FROM ServiceEntity s WHERE s.available = true AND " +
           "(s.title LIKE %?1% OR s.description LIKE %?1% OR s.serviceArea LIKE %?1%)")
    List<ServiceEntity> searchServices(String keyword);

    @Query("SELECT s FROM ServiceEntity s WHERE s.available = true AND s.category = ?1")
    List<ServiceEntity> findAvailableByCategory(ServiceCategory category);

    @Query("SELECT s FROM ServiceEntity s WHERE s.available = true ORDER BY s.averageRating DESC")
    List<ServiceEntity> findTopRatedServices();

    @Query("SELECT s FROM ServiceEntity s WHERE s.available = true AND s.price BETWEEN ?1 AND ?2")
    List<ServiceEntity> findByPriceRange(Double minPrice, Double maxPrice);

    long countByProviderId(Long providerId);

    void deleteByProviderId(Long providerId);
}
