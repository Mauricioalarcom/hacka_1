package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UserAccountRepository userAccountRepository;

    public EmpresaService(EmpresaRepository empresaRepository, UserAccountRepository userAccountRepository) {
        this.empresaRepository = empresaRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public Empresa createEmpresa(Empresa empresa) {
        // Obtener el usuario autenticado actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserAccount currentUser = null;

            // Obtener el usuario según el tipo de principal
            if (authentication.getPrincipal() instanceof UserAccount) {
                currentUser = (UserAccount) authentication.getPrincipal();
            } else {
                String email = authentication.getName();
                currentUser = userAccountRepository.findByEmail(email);
            }

            // Asignar el nombre del administrador si se encontró el usuario
            if (currentUser != null) {
                empresa.setAdmin(currentUser.getName());
            }
        }

        // Resto de la lógica de creación
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
        existingEmpresa.setFechaAfiliacion(existingEmpresa.getFechaAfiliacion());
        existingEmpresa.setEstado(existingEmpresa.getEstado());
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
