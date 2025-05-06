package com.greenloop.sparky.authController;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/roles")
public class RolesController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> admin(Principal principal) {
        return ResponseEntity.ok("Soy un admin: " + principal.getName());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<String> user(Principal principal) {
        return ResponseEntity.ok("Soy un user: " + principal.getName());
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/both")
    public ResponseEntity<String> both(Principal principal) {
        return ResponseEntity.ok("Soy un user o un admin: " + principal.getName());
    }
}