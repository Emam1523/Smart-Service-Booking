package com.smart_service_booking.repository;

import com.smart_service_booking.entity.Booking;
import com.smart_service_booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByProviderId(Long providerId);

    List<Booking> findByServiceId(Long serviceId);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findByProviderIdAndStatus(Long providerId, BookingStatus status);

    List<Booking> findByStatus(BookingStatus status);

    long countByStatus(BookingStatus status);

    long countByProviderId(Long providerId);

    long countByProviderIdAndStatus(Long providerId, BookingStatus status);

    long countByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.bookingDate = ?1")
    List<Booking> findByBookingDate(LocalDate date);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.createdAt >= ?1 AND b.createdAt <= ?2")
    long countByDateRange(LocalDateTime start, LocalDateTime end);

    void deleteByProviderId(Long providerId);

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findByProviderIdOrderByCreatedAtDesc(Long providerId);
}
