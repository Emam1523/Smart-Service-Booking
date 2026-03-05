package com.smart_service_booking.service;

import com.smart_service_booking.dto.BookingRequest;
import com.smart_service_booking.entity.*;
import com.smart_service_booking.enums.BookingStatus;
import com.smart_service_booking.exception.ResourceNotFoundException;
import com.smart_service_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final ServiceProviderRepository providerRepository;

    @Transactional
    public Booking createBooking(Long userId, BookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ServiceEntity service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        Booking booking = Booking.builder()
                .user(user)
                .service(service)
                .provider(service.getProvider())
                .bookingDate(LocalDate.parse(request.getBookingDate()))
                .bookingTime(request.getBookingTime())
                .address(request.getAddress())
                .city(request.getCity())
                .phone(request.getPhone())
                .notes(request.getNotes())
                .status(BookingStatus.PENDING)
                .totalAmount(service.getPrice())
                .build();

        booking = bookingRepository.save(booking);

        // Update service booking count (guard against null for legacy records)
        service.setTotalBookings((service.getTotalBookings() != null ? service.getTotalBookings() : 0) + 1);
        serviceRepository.save(service);

        return booking;
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Booking> getProviderBookings(Long providerId) {
        return bookingRepository.findByProviderIdOrderByCreatedAtDesc(providerId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    @Transactional
    public Booking updateBookingStatus(Long id, String status, String reason) {
        Booking booking = getBookingById(id);
        BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());
        booking.setStatus(newStatus);

        if (newStatus == BookingStatus.CANCELLED) {
            booking.setCancellationReason(reason);
        } else if (newStatus == BookingStatus.REJECTED) {
            booking.setRejectionReason(reason);
        } else if (newStatus == BookingStatus.COMPLETED) {
            booking.setCompletedAt(LocalDateTime.now());

            // Update provider stats
            ServiceProvider provider = booking.getProvider();
            provider.setCompletedJobs(provider.getCompletedJobs() + 1);
            provider.setTotalEarnings(provider.getTotalEarnings() + booking.getTotalAmount());
            providerRepository.save(provider);
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(BookingStatus.valueOf(status.toUpperCase()));
    }

    public long countByStatus(String status) {
        return bookingRepository.countByStatus(BookingStatus.valueOf(status.toUpperCase()));
    }
}
