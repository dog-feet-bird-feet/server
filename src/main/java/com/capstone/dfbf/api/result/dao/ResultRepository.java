package com.capstone.dfbf.api.result.dao;

import com.capstone.dfbf.api.result.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<AnalysisResult, String> {

    @Query("SELECT r FROM AnalysisResult r WHERE r.member.id = :memberId")
    List<AnalysisResult> findByMember(@Param("memberId") Long memberId);

    @Query("SELECT r FROM AnalysisResult r WHERE r.member.id = :memberId ORDER BY r.createdAt DESC")
    List<AnalysisResult> findByMemberCreatedAtDesc(@Param("memberId") Long memberId);
}
