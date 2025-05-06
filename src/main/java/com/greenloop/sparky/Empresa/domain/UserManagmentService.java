package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Empresa.exceptions.ResourceNotFoundException;
import com.greenloop.sparky.Empresa.infrastructure.EmpresaRepository;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.domain.Role;
import com.greenloop.sparky.User.infraestructure.UserAccountRepository;
import com.greenloop.sparky.common.exception.UnauthorizedAccessException;
import com.greenloop.sparky.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserManagmentService {

    private final UserAccountRepository userRepository;
    private final EmpresaRepository empresaRepository;

    public UserManagmentService(UserAccountRepository userRepository, EmpresaRepository empresaRepository) {
        this.userRepository = userRepository;
        this.empresaRepository = empresaRepository;
    }

    public UserAccount createUser(Long empresaId, UserAccount userAccount, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa not found with id: " + empresaId));
        
        validateUserBelongsToEmpresa(currentUser, empresa.getId());
        userAccount.setEmpresa(empresa);
        return userRepository.save(userAccount);
    }

    public List<UserAccount> getAllUsers(Long empresaId, UserAccount currentUser) {
        validateUserBelongsToEmpresa(currentUser, empresaId);
        return userRepository.findByEmpresaId(empresaId);
    }

    public UserAccount getUserById(Long empresaId, Long id, UserAccount currentUser) {
        validateUserBelongsToEmpresa(currentUser, empresaId);
        return userRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserAccount updateUser(Long empresaId, Long id, UserAccount userAccount, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        validateUserBelongsToEmpresa(currentUser, empresaId);
        
        UserAccount existingUser = getUserById(empresaId, id, currentUser);
        existingUser.setName(userAccount.getName());
        existingUser.setEmail(userAccount.getEmail());
        existingUser.setRole(userAccount.getRole());
        return userRepository.save(existingUser);
    }

    public UserAccount assignUserLimit(Long empresaId, Long id, Integer limit, UserAccount currentUser) {
        validateAdminAccess(currentUser);
        validateUserBelongsToEmpresa(currentUser, empresaId);
        
        UserAccount user = getUserById(empresaId, id, currentUser);
        user.setLimit(limit);
        return userRepository.save(user);
    }

    public String getUserConsumption(Long empresaId, Long id, UserAccount currentUser) {
        validateUserBelongsToEmpresa(currentUser, empresaId);
        getUserById(empresaId, id, currentUser); // Validate user exists
        return "Consumption report for User ID: " + id + " in Empresa ID: " + empresaId;
    }

    private void validateAdminAccess(UserAccount user) {
        if (user == null || user.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedAccessException("Only COMPANY_ADMIN users can perform this operation");
        }
    }

    private void validateUserBelongsToEmpresa(UserAccount user, Long empresaId) {
        if (user == null || !user.getEmpresa().getId().equals(empresaId)) {
            throw new UnauthorizedAccessException("User does not have access to this empresa");
        }
    }
}