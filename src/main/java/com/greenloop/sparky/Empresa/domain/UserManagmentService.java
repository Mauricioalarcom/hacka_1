package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.exceptions.ResourceNotFoundException;
import com.greenloop.sparky.Empresa.exceptions.UnauthorizedAccessException;
import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.domain.Role;
import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        
        // Verificar que el usuario actual tenga una empresa
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
        // Verificar que el límite sea válido
        if (limit != null && limit < 0) {
            throw new IllegalArgumentException("User limit cannot be negative");
        }

        // Obtener el administrador actual
        UserAccount currentUser = getCurrentAdminUser();

        // Verificar que sea un Admin

        // Buscar el usuario por ID y empresa
        UserAccount user = (UserAccount) userRepository.findByIdAndEmpresaId(id, currentUser.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Verificar que el usuario no sea otro administrador
        if (user.getRole() == Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Cannot set limits for another company administrator");
        }

        // Establecer el nuevo límite y guardar
        user.setLimit(limit);

        // Guardar y devolver el usuario actualizado
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
        
        // Si el principal es directamente un UserAccount
        if (principal instanceof UserAccount) {
            user = (UserAccount) principal;
            // Si el objeto ya está en el contexto de seguridad, podría no tener todos los datos cargados
            // Recargar desde la base de datos para asegurar que la empresa esté cargada
            user = userRepository.findById(user.getId())
                .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
        } else {
            // Si el principal es un nombre de usuario (email)
            String email = authentication.getName();
            user = userRepository.findByEmail(email);
            
            if (user == null) {
                throw new UnauthorizedAccessException("User not found");
            }
        }
        
        // Validar que el usuario tiene rol COMPANY_ADMIN
        if (user.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Only COMPANY_ADMIN users can perform this operation");
        }


        if (user.getEmpresa() == null) {
            throw new ResourceNotFoundException("The current user does not have an associated company");
        }
        
        return user;
    }
}