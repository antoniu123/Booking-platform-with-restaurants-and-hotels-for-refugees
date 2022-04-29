package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Optional<Profile> findByUserId(String userId);
}
