package com.backend.auth0springboot.repository;

import com.backend.auth0springboot.model.HelpPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpPointRepository extends JpaRepository<HelpPoint, Long> {
}
