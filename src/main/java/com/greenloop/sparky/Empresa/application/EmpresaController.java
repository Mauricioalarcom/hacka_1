package com.greenloop.sparky.Empresa.application;

import com.greenloop.sparky.Empresa.domain.Empresa;
import com.greenloop.sparky.Empresa.domain.EmpresaService;
import com.greenloop.sparky.Empresa.domain.Estado;
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
        return ResponseEntity.ok(empresaService.createEmpresa(empresa));
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        return ResponseEntity.ok(empresaService.getAllEmpresas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getEmpresaById(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.getEmpresaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.updateEmpresa(id, empresa));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Empresa> updateEmpresaStatus(@PathVariable Long id, @RequestParam Estado estado) {
        return ResponseEntity.ok(empresaService.updateEmpresaStatus(id, estado));
    }

    @GetMapping("/{id}/consumption")
    public ResponseEntity<String> getEmpresaConsumption(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.getEmpresaConsumption(id));
    }
}
