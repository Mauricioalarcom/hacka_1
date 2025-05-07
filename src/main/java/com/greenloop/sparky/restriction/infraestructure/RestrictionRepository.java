package com.greenloop.sparky.restriction.infraestructure;

import com.greenloop.sparky.restriction.domain.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriction, Long> {

    List<Restriction> findByEmpresaId(Long id);

    boolean existsByModelAndEmpresaId(String model, Long empresaId);
}
