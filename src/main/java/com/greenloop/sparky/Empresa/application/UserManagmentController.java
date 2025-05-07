package com.greenloop.sparky.Empresa.application;

import com.greenloop.sparky.Empresa.domain.UserManagmentService;

import com.greenloop.sparky.User.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/users")
public class UserManagmentController {

    @Autowired
    private UserManagmentService userManagmentService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserAccount> createUser(
            @RequestBody UserAccount newUser) {
        return ResponseEntity.ok(userManagmentService.createUser(newUser));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
    public ResponseEntity<List<UserAccount>> getAllUsers() {
        return ResponseEntity.ok(userManagmentService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'USER')")
    public ResponseEntity<UserAccount> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userManagmentService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserAccount> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccount userUpdate) {
        return ResponseEntity.ok(userManagmentService.updateUser(id, userUpdate));
    }

    @PostMapping("/{id}/limits")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<UserAccount> assignUserLimit(
            @PathVariable Long id,
            @RequestBody Integer limit) {
        return ResponseEntity.ok(userManagmentService.assignUserLimit(id, limit));
    }

//    @GetMapping("/{id}/consumption")
//    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'USER')")
//    public ResponseEntity<String> getUserConsumption(
//            @PathVariable Long id,
//            @AuthenticationPrincipal UserAccount currentUser) {
//        return ResponseEntity.ok(userManagmentService.getUserConsumption(id, currentUser));
//    }
}