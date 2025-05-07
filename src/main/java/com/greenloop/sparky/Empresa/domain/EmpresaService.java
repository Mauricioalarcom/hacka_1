package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.exceptions.UnauthorizedAccessException;
import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import com.greenloop.sparky.User.domain.Role;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
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

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User not authenticated");
        }

        UserAccount currentUser = null;

        // Obtener el usuario según el tipo de principal
        if (authentication.getPrincipal() instanceof UserAccount) {
            currentUser = (UserAccount) authentication.getPrincipal();
            // Recargar para asegurar datos actualizados
            currentUser = userAccountRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
        } else {
            String email = authentication.getName();
            currentUser = userAccountRepository.findByEmail(email);

            if (currentUser == null) {
                throw new UnauthorizedAccessException("User not found");
            }
        }

        // Verificar que el usuario tenga rol de COMPANY_ADMIN
        if (currentUser.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Only administrators can create companies");
        }

        // Verificar que el administrador no tenga ya una empresa asociada
        List<Empresa> existingCompanies = empresaRepository.findByAdministradorId(currentUser.getId());
        if (!existingCompanies.isEmpty()) {
            throw new IllegalStateException("Administrator already has a company associated. Cannot create more than one company per administrator.");
        }

        // Establecer el administrador en la empresa
        empresa.setAdministrador(currentUser);

        // Guardar la empresa
        Empresa savedEmpresa = empresaRepository.save(empresa);

        // Asignar la empresa al administrador para completar la relación bidireccional
        currentUser.setEmpresa(savedEmpresa);
        userAccountRepository.save(currentUser);

        return savedEmpresa;
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
