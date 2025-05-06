package com.greenloop.sparky.Empresa.application;

import com.greenloop.sparky.Empresa.domain.UserManagmentService;
import com.greenloop.sparky.User.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/{empresaId}/users")
public class UserManagmentController {

    @Autowired
    private UserManagmentService userManagmentService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserAccount> createUser(
            @PathVariable Long empresaId,
            @RequestBody UserAccount newUser,
            @AuthenticationPrincipal UserAccount currentUser) {
        return ResponseEntity.ok(userManagmentService.createUser(empresaId, newUser, currentUser));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'USER')")
    public ResponseEntity<List<UserAccount>> getAllUsers(
            @PathVariable Long empresaId,
            @AuthenticationPrincipal UserAccount currentUser) {
        return ResponseEntity.ok(userManagmentService.getAllUsers(empresaId, currentUser));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'USER')")
    public ResponseEntity<UserAccount> getUserById(
            @PathVariable Long empresaId,
            @PathVariable Long id,
            @AuthenticationPrincipal UserAccount currentUser) {
        return ResponseEntity.ok(userManagmentService.getUserById(empresaId, id, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserAccount> updateUser(
            @PathVariable Long empresaId,
            @PathVariable Long id,
            @RequestBody UserAccount userUpdate,
            @AuthenticationPrincipal UserAccount currentUser) {
        return ResponseEntity.ok(userManagmentService.updateUser(empresaId, id, userUpdate, currentUser));
    }

    @PostMapping("/{id}/limits")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserAccount> assignUserLimit(
            @PathVariable Long empresaId,
            @PathVariable Long id,
            @RequestBody Integer limit,
            @AuthenticationPrincipal UserAccount currentUser) {
        return ResponseEntity.ok(userManagmentService.assignUserLimit(empresaId, id, limit, currentUser));
    }

    @GetMapping("/{id}/consumption")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'USER')")
    public ResponseEntity<String> getUserConsumption(
            @PathVariable Long empresaId,
            @PathVariable Long id,
            @AuthenticationPrincipal UserAccount currentUser) {
        return ResponseEntity.ok(userManagmentService.getUserConsumption(empresaId, id, currentUser));
    }

}
