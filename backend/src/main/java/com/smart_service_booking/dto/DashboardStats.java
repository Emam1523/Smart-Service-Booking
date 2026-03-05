package com.smart_service_booking.dto;

import lombok.*;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardStats {
    private long totalUsers;
    private long totalProviders;
    private long totalBookings;
    private long totalServices;
    private long pendingProviders;
    private long activeBookings;
    private long completedBookings;
    private long cancelledBookings;
    private long newUsersThisMonth;
    private long newBookingsThisMonth;
    private Map<String, Long> bookingsByMonth;
}
