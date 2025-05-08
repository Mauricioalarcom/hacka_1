package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.exceptions.ResourceNotFoundException;
import com.greenloop.sparky.Empresa.exceptions.UnauthorizedAccessException;
import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.domain.Role;
import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import com.greenloop.sparky.consumption.domain.ConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserManagmentService {

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public UserManagmentService(UserAccountRepository userRepository, EmpresaRepository empresaRepository) {
        this.userRepository = userRepository;
        this.empresaRepository = empresaRepository;
    }

    public UserAccount createUser(UserAccount newEmployee) {
        UserAccount currentUser = getCurrentAdminUser();
        
        if (currentUser.getEmpresa() == null) {
            throw new ResourceNotFoundException("Cannot create user: current admin does not have an associated company");
        }
        
        newEmployee.setEmpresa(currentUser.getEmpresa());
        newEmployee.setEnable(true);
        newEmployee.setExpired(false);
        newEmployee.setLocked(false);
        newEmployee.setCredentialsExpired(false);
        newEmployee.setRole(Role.USER);
        
        if (newEmployee.getPassword() != null) {
            newEmployee.setPassword(passwordEncoder.encode(newEmployee.getPassword()));
        }
        
        return userRepository.save(newEmployee);
    }


    public List<UserAccount> getAllUsers() {
        UserAccount currentUser = getCurrentAdminUser();

        return userRepository.findByEmpresaId(currentUser.getEmpresa().getId());
    }


    public UserAccount getUserById(Long id) {
        UserAccount currentUser = getCurrentAdminUser();

        return (UserAccount) userRepository.findByIdAndEmpresaId(id, currentUser.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserAccount updateUser(Long id, UserAccount userAccount) {

        UserAccount currentUser = getCurrentAdminUser();


        UserAccount existingUser = (UserAccount) userRepository.findByIdAndEmpresaId(id, currentUser.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setName(userAccount.getName());
        existingUser.setEmail(userAccount.getEmail());

        if (userAccount.getRole() == Role.COMPANY_ADMIN && existingUser.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Cannot upgrade a user to COMPANY_ADMIN");
        }

        existingUser.setRole(userAccount.getRole());

        if (userAccount.getExpired() != null) {
            existingUser.setExpired(userAccount.getExpired());
        }

        if (userAccount.getLocked() != null) {
            existingUser.setLocked(userAccount.getLocked());
        }

        if (userAccount.getEnable() != null) {
            existingUser.setEnable(userAccount.getEnable());
        }

        if (userAccount.getCredentialsExpired() != null) {
            existingUser.setCredentialsExpired(userAccount.getCredentialsExpired());
        }

        return userRepository.save(existingUser);
    }


    public UserAccount assignUserLimit(Long id, Integer limit) {
        if (limit != null && limit < 0) {
            throw new IllegalArgumentException("User limit cannot be negative");
        }

        UserAccount currentUser = getCurrentAdminUser();


        UserAccount user = (UserAccount) userRepository.findByIdAndEmpresaId(id, currentUser.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user.getRole() == Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Cannot set limits for another company administrator");
        }

        user.setLimit(limit);

        return userRepository.save(user);
    }

    //falta implementa getUserConsumption SERVICE /////

    /**
     * Gets the currently authenticated user and validates that they have a COMPANY_ADMIN role.
     * 
     * @return The authenticated user with the COMPANY_ADMIN role
     * @throws UnauthorizedAccessException if the user is not authenticated or doesn't have a COMPANY_ADMIN role
     */
    private UserAccount getCurrentAdminUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User not authenticated");
        }
        
        UserAccount user;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserAccount) {
            user = (UserAccount) principal;

            user = userRepository.findById(user.getId())
                .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
        } else {
            String email = authentication.getName();
            user = userRepository.findByEmail(email);
            
            if (user == null) {
                throw new UnauthorizedAccessException("User not found");
            }
        }

        if (user.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Only COMPANY_ADMIN users can perform this operation");
        }


        if (user.getEmpresa() == null) {
            throw new ResourceNotFoundException("The current user does not have an associated company");
        }
        
        return user;
    }
    /**
     * Obtiene información detallada sobre el consumo de IA del usuario
     * incluyendo tokens consumidos, costo, y detalle por modelo de IA
     *
     * @param id ID del usuario
     * @param currentUser Usuario autenticado realizando la consulta
     * @return String con información formateada del consumo o un objeto JSON según necesidad
     */
    public String getUserConsumption(Long id, UserAccount currentUser) {
        if (currentUser.getEmpresa() == null) {
            throw new ResourceNotFoundException("Cannot get user consumption: current user does not have an associated company");
        }

        UserAccount user = (UserAccount) userRepository.findByIdAndEmpresaId(id, currentUser.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!currentUser.getId().equals(user.getId()) && currentUser.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("You can only view your own consumption or you need COMPANY_ADMIN role");
        }

        Map<String, Object> consumptionReport = ConsumptionService.getUserConsumptionReport(id);

        StringBuilder response = new StringBuilder();
        response.append("Consumo de IA para ").append(user.getName()).append(":\n");
        response.append("- Límite asignado: ").append(user.getLimit() != null ? user.getLimit() : "Ilimitado").append(" tokens\n");
        response.append("- Tokens totales consumidos: ").append(consumptionReport.get("totalTokens")).append("\n");
        response.append("- Costo total: $").append(String.format("%.2f", consumptionReport.get("totalCost"))).append("\n");

        response.append("- Distribución por modelo:\n");

        @SuppressWarnings("unchecked")
        Map<String, Integer> tokensByModel = (Map<String, Integer>) consumptionReport.get("tokensByModel");
        tokensByModel.forEach((model, tokens) -> {
            response.append("  * ").append(model).append(": ").append(tokens).append(" tokens\n");
        });

        if (user.getLimit() != null) {
            int totalTokens = (int) consumptionReport.get("totalTokens");
            double usagePercentage = (double) totalTokens / user.getLimit() * 100;

            if (usagePercentage >= 100) {
                response.append("\n⚠️ El usuario ha excedido su límite asignado (")
                        .append(String.format("%.1f", usagePercentage))
                        .append("%)");
            } else if (usagePercentage >= 80) {
                response.append("\n⚠️ El usuario está cerca de alcanzar su límite (")
                        .append(String.format("%.1f", usagePercentage))
                        .append("%)");
            }
        }

        return response.toString();
    }

}