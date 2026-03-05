package com.smart_service_booking.service;

import com.smart_service_booking.dto.DashboardStats;
import com.smart_service_booking.enums.BookingStatus;
import com.smart_service_booking.enums.ProviderStatus;
import com.smart_service_booking.enums.Role;
import com.smart_service_booking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ServiceProviderRepository providerRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;

    public DashboardStats getDashboardStats() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

        Map<String, Long> bookingsByMonth = new LinkedHashMap<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int currentMonth = LocalDateTime.now().getMonthValue();

        for (int i = 0; i < 6; i++) {
            int monthIdx = (currentMonth - 6 + i + 12) % 12;
            LocalDateTime start = LocalDateTime.now().withMonth(monthIdx + 1).withDayOfMonth(1).withHour(0).withMinute(0);
            LocalDateTime end = start.plusMonths(1);
            long count = bookingRepository.countByDateRange(start, end);
            bookingsByMonth.put(months[monthIdx], count);
        }

        return DashboardStats.builder()
                .totalUsers(userRepository.countByRole(Role.USER))
                .totalProviders(providerRepository.count())
                .totalBookings(bookingRepository.count())
                .totalServices(serviceRepository.count())
                .pendingProviders(providerRepository.countByStatus(ProviderStatus.PENDING))
                .activeBookings(bookingRepository.countByStatus(BookingStatus.PENDING)
                        + bookingRepository.countByStatus(BookingStatus.CONFIRMED))
                .completedBookings(bookingRepository.countByStatus(BookingStatus.COMPLETED))
                .cancelledBookings(bookingRepository.countByStatus(BookingStatus.CANCELLED))
                .newUsersThisMonth(userRepository.countByCreatedAtAfter(startOfMonth))
                .newBookingsThisMonth(bookingRepository.countByCreatedAtAfter(startOfMonth))
                .bookingsByMonth(bookingsByMonth)
                .build();
    }
}
