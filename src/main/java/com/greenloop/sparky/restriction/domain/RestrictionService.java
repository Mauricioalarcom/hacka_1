package com.greenloop.sparky.restriction.domain;

import com.greenloop.sparky.Empresa.domain.Empresa;
import com.greenloop.sparky.Empresa.domain.EmpresaService;
import com.greenloop.sparky.restriction.exceptions.InvalidRestrictionLimitException;
import com.greenloop.sparky.restriction.exceptions.RestrictionDuplicateModelException;
import com.greenloop.sparky.restriction.exceptions.RestrictionNotFoundException;
import com.greenloop.sparky.restriction.exceptions.RestrictionNotOwnerException;
import com.greenloop.sparky.restriction.infraestructure.RestrictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestrictionService {
    private final RestrictionRepository restrictionRepository;
    private final EmpresaService empresaService;

    public RestrictionService(RestrictionRepository restrictionRepository, EmpresaService empresaService) {
        this.restrictionRepository = restrictionRepository;
        this.empresaService = empresaService;
    }

    public Restriction createRestriction(Restriction restriction, Long empresaId) {
        Empresa empresa = empresaService.getEmpresaById(empresaId);
        if (empresa == null) {
            throw new RuntimeException("Empresa no encontrada con ID: " + empresaId);
        }

        // Verificar que el modelo no esté duplicado
        if (restrictionRepository.existsByModelAndEmpresaId(restriction.getModel(), empresaId)) {
            throw new RestrictionDuplicateModelException(restriction.getModel());
        }

        // Verificar que el límite sea válido
        if (restriction.getLimit() <= 0) {
            throw new InvalidRestrictionLimitException(restriction.getLimit());
        }

        restriction.setEmpresa(empresa);
        return restrictionRepository.save(restriction);
    }


    public List<Restriction> getRestrictionsByEmpresa(Long empresaId) {
        return restrictionRepository.findByEmpresaId(empresaId);
    }

    public List<Restriction> getAllRestrictions() {
        return restrictionRepository.findAll();
    }

    public Restriction updateRestriction(Long id, Restriction restriction, Long empresaId) {
        Restriction existingRestriction = restrictionRepository.findById(id)
                .orElseThrow(() -> new RestrictionNotFoundException(id));

        // Verificar que la restricción pertenezca a la empresa correcta
        if (!existingRestriction.getEmpresa().getId().equals(empresaId)) {
            throw new RestrictionNotOwnerException(id, empresaId);
        }

        // Verificar que si cambia el modelo, no esté duplicado
        if (!existingRestriction.getModel().equals(restriction.getModel()) &&
                restrictionRepository.existsByModelAndEmpresaId(restriction.getModel(), empresaId)) {
            throw new RestrictionDuplicateModelException(restriction.getModel());
        }

        // Verificar que el límite sea válido
        if (restriction.getLimit() <= 0) {
            throw new InvalidRestrictionLimitException(restriction.getLimit());
        }

        // Actualizar campos
        existingRestriction.setModel(restriction.getModel());
        existingRestriction.setLimit(restriction.getLimit());

        return restrictionRepository.save(existingRestriction);
    }


    public void deleteRestriction(Long id, Long empresaId) {
        Restriction restriction = restrictionRepository.findById(id)
                .orElseThrow(() -> new RestrictionNotFoundException(id));

        // Verificar que la restricción pertenezca a la empresa correcta
        if (!restriction.getEmpresa().getId().equals(empresaId)) {
            throw new RestrictionNotOwnerException(id, empresaId);
        }

        restrictionRepository.delete(restriction);
    }

}