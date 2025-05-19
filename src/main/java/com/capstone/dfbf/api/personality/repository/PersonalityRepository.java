package com.capstone.dfbf.api.personality.repository;

import com.capstone.dfbf.api.personality.domain.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalityRepository extends JpaRepository<Personality, Long> {

}
