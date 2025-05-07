package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.exceptions.ResourceNotFoundException;
import com.greenloop.sparky.Empresa.exceptions.UnauthorizedAccessException;
import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.domain.Role;
import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserManagmentService {

    @Autowired
    private  UserAccountRepository userRepository;

    @Autowired
    private  EmpresaRepository empresaRepository;

    public UserManagmentService(UserAccountRepository userRepository, EmpresaRepository empresaRepository) {
        this.userRepository = userRepository;
        this.empresaRepository = empresaRepository;
    }

    public UserAccount createUser(UserAccount userAccount, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        Empresa empresa = currentUser.getEmpresa(); // Get the empresa from the admin user
        userAccount.setEmpresa(empresa);
        return userRepository.save(userAccount);
    }

    public List<UserAccount> getAllUsers(UserAccount currentUser) {
        validateAdminAccess(currentUser);
        return userRepository.findByEmpresaId(currentUser.getEmpresa().getId());
    }

    public UserAccount getUserById(Long id, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        return userRepository.findByIdAndEmpresaId(id, currentUser.getEmpresa().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserAccount updateUser(Long id, UserAccount userAccount, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        UserAccount existingUser = getUserById(id, currentUser);
        existingUser.setName(userAccount.getName());
        existingUser.setEmail(userAccount.getEmail());
        existingUser.setRole(userAccount.getRole());
        return userRepository.save(existingUser);
    }

    public UserAccount assignUserLimit(Long id, Integer limit, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        UserAccount user = getUserById(id, currentUser);
        user.setLimit(limit);
        return userRepository.save(user);
    }

    public String getUserConsumption(Long id, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        getUserById(id, currentUser); // Validate user exists
        return "Consumption report for User ID: " + id + " in Empresa ID: " + currentUser.getEmpresa().getId();
    }

    private void validateAdminAccess(UserAccount user) {
        if (user == null || user.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Only COMPANY_ADMIN users can perform this operation");
        }
    }
}