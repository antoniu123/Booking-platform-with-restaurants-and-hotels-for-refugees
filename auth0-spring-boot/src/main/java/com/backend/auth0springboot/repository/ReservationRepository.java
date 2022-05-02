package com.backend.auth0springboot.repository;

import com.backend.auth0springboot.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
