package com.smart_service_booking.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private String fullName;
    private String email;
    private Long providerId;
}
