package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.HelpPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpPointRepository extends JpaRepository<HelpPoint, Long> {
}
