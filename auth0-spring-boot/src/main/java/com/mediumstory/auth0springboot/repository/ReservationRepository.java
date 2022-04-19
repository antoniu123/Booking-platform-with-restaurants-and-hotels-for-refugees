package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
