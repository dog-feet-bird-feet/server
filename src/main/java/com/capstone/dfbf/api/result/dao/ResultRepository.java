package com.capstone.dfbf.api.result.dao;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<AnalysisResult, Long> {

}
