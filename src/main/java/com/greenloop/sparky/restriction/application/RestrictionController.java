package com.greenloop.sparky.restriction.application;

import com.greenloop.sparky.restriction.domain.Restriction;
import com.greenloop.sparky.restriction.domain.RestrictionService;
import com.greenloop.sparky.User.domain.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/api/company/restrictions")
public class RestrictionController {

    private final RestrictionService restrictionService;
    
    public RestrictionController(RestrictionService restrictionService) {
        this.restrictionService = restrictionService;
    }

    private Long getCurrentEmpresaId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        return userAccount.getEmpresa().getId();
    }
    
    @PostMapping
    public ResponseEntity<Restriction> createRestriction(@RequestBody Restriction restriction) {
        Long empresaId = getCurrentEmpresaId();
        return new ResponseEntity<>(
            restrictionService.createRestriction(restriction, empresaId),
            HttpStatus.CREATED
        );
    }
    
    @GetMapping
    public ResponseEntity<List<Restriction>> getAllRestrictions() {
        Long empresaId = getCurrentEmpresaId();
        return ResponseEntity.ok(restrictionService.getRestrictionsByEmpresa(empresaId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Restriction> updateRestriction(
            @PathVariable Long id, 
            @RequestBody Restriction restriction) {
        Long empresaId = getCurrentEmpresaId();
        return ResponseEntity.ok(
            restrictionService.updateRestriction(id, restriction, empresaId)
        );
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestriction(@PathVariable Long id) {
        Long empresaId = getCurrentEmpresaId();
        restrictionService.deleteRestriction(id, empresaId);
        return ResponseEntity.noContent().build();
    }
}