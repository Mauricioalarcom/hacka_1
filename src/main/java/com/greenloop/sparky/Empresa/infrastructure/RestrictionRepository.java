package com.greenloop.sparky.Empresa.infrastructure;

import com.greenloop.sparky.Empresa.domain.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
}
