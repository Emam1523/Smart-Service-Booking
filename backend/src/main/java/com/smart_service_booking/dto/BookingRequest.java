package com.smart_service_booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotBlank(message = "Booking date is required")
    private String bookingDate;

    private String bookingTime;

    @NotBlank(message = "Address is required")
    private String address;

    private String city;
    private String phone;

    @NotBlank(message = "Problem description is required")
    private String notes;
}
