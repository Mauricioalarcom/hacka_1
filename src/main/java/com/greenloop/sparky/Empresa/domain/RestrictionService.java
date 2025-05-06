package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.infrastructure.RestrictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestrictionService {
    private final RestrictionRepository restrictionRepository;

    public RestrictionService(RestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
    }

    public Restriction createRestriction(Restriction restriction) {
        return restrictionRepository.save(restriction);
    }

    public List<Restriction> getAllRestrictions() {
        return restrictionRepository.findAll();
    }

    public Restriction updateRestriction(Long id, Restriction restriction) {
        Restriction existingRestriction = restrictionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restriction not found"));
        existingRestriction.setModel(restriction.getModel());
        existingRestriction.setLimit(restriction.getLimit());
        return restrictionRepository.save(existingRestriction);
    }

    public void deleteRestriction(Long id) {
        restrictionRepository.deleteById(id);
    }
}
