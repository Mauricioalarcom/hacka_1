package com.greenloop.sparky.Empresa.infrastructure;

import com.greenloop.sparky.Empresa.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Long Id(Long id);
}
