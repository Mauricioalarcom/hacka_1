package com.greenloop.sparky.Empresa.application;

import com.greenloop.sparky.Empresa.domain.Restriction;
import com.greenloop.sparky.Empresa.domain.RestrictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/restrictions")
public class RestrictionController {

    private final RestrictionService restrictionService;

    public RestrictionController(RestrictionService restrictionService) {
        this.restrictionService = restrictionService;
    }

    @PostMapping
    public ResponseEntity<Restriction> createRestriction(@RequestBody Restriction restriction) {
        return ResponseEntity.ok(restrictionService.createRestriction(restriction));
    }

    @GetMapping
    public ResponseEntity<List<Restriction>> getAllRestrictions() {
        return ResponseEntity.ok(restrictionService.getAllRestrictions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restriction> updateRestriction(@PathVariable Long id, @RequestBody Restriction restriction) {
        return ResponseEntity.ok(restrictionService.updateRestriction(id, restriction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestriction(@PathVariable Long id) {
        restrictionService.deleteRestriction(id);
        return ResponseEntity.noContent().build();
    }
}
