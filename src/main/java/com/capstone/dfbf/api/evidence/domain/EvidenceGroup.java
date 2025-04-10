package com.capstone.dfbf.api.evidence.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class EvidenceGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Verification verification;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comparison> comparisons;

    public void addComparison(Comparison comparison) {
        comparisons.add(comparison);
    }
}
