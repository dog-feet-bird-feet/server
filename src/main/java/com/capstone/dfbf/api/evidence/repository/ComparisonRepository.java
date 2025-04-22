package com.capstone.dfbf.api.evidence.repository;

import com.capstone.dfbf.api.evidence.domain.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComparisonRepository extends JpaRepository<Comparison, Long> {
}
