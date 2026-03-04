package com.smart_service_booking.repository;

import com.smart_service_booking.entity.User;
import com.smart_service_booking.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByBannedTrue();

    long countByRole(Role role);

    long countByCreatedAtAfter(LocalDateTime date);

    @Query("SELECT u FROM User u WHERE u.fullName LIKE %?1% OR u.email LIKE %?1%")
    List<User> searchUsers(String keyword);
}
