package com.greenloop.sparky.Empresa.application;

import com.greenloop.sparky.Empresa.domain.Empresa;
import com.greenloop.sparky.Empresa.domain.EmpresaService;
import com.greenloop.sparky.Empresa.domain.Estado;
import com.greenloop.sparky.Empresa.exceptions.EmpresaNotFoundException;
import com.greenloop.sparky.Empresa.exceptions.EmpresaValidationException;
import com.greenloop.sparky.consumption.dto.ConsumptionReportDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/companies")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        if (empresa == null) {
            throw new com.greenloop.sparky.Empresa.exceptions.EmpresaValidationException("El objeto empresa no puede ser nulo");
        }
        if (empresa.getNombreEmpresa() == null || empresa.getNombreEmpresa().trim().isEmpty()) {
            throw new com.greenloop.sparky.Empresa.exceptions.EmpresaValidationException("El nombre de la empresa no puede estar vacío");
        }
        
        return new ResponseEntity<>(empresaService.createEmpresa(empresa), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        List<Empresa> empresas = empresaService.getAllEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        Empresa empresa = empresaService.getEmpresaById(id);
        if (empresa == null) {
            throw new com.greenloop.sparky.Empresa.exceptions.EmpresaNotFoundException(id);
        }
        return ResponseEntity.ok(empresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        if (empresa == null) {
            throw new com.greenloop.sparky.Empresa.exceptions.EmpresaValidationException("El objeto empresa no puede ser nulo");
        }
        
        // Verificar primero si la empresa existe
        empresaService.getEmpresaById(id);
        
        return ResponseEntity.ok(empresaService.updateEmpresa(id, empresa));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Empresa> updateEmpresaStatus(@PathVariable Long id, @RequestParam Estado estado) {
        if (estado == null) {
            throw new com.greenloop.sparky.Empresa.exceptions.EmpresaValidationException("El estado no puede ser nulo");
        }
        
        // Verificar primero si la empresa existe
        empresaService.getEmpresaById(id);
        
        return ResponseEntity.ok(empresaService.updateEmpresaStatus(id, estado));
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<ConsumptionReportDTO> getEmpresaConsumption(@PathVariable Long id) {
        // Verificar primero si la empresa existe
        empresaService.getEmpresaById(id);

        // Obtener reporte de consumo
        return ResponseEntity.ok(empresaService.getEmpresaConsumptionReport(id));
    }

}