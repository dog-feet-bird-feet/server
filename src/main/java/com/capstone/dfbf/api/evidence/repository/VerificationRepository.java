package com.capstone.dfbf.api.evidence.repository;

import com.capstone.dfbf.api.evidence.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

}
