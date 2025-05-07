package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public Empresa createEmpresa(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    public Empresa getEmpresaById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa not found"));
    }

    public Empresa updateEmpresa(Long id, Empresa empresa) {
        Empresa existingEmpresa = getEmpresaById(id);
        existingEmpresa.setNombreEmpresa(empresa.getNombreEmpresa());
        existingEmpresa.setRuc(empresa.getRuc());
        existingEmpresa.setFechaAfiliacion(empresa.getFechaAfiliacion());
        existingEmpresa.setEstado(empresa.getEstado());
        return empresaRepository.save(existingEmpresa);
    }

    public Empresa updateEmpresaStatus(Long id, Estado estado) {
        Empresa empresa = getEmpresaById(id);
        empresa.setEstado(estado);
        return empresaRepository.save(empresa);
    }

    public String getEmpresaConsumption(Long id) {
        // Placeholder for consumption logic
        return "Consumption report for Empresa ID: " + id;
    }
}
