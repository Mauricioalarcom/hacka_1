package com.greenloop.sparky.Sparky.infrastructure;

import com.greenloop.sparky.Sparky.domain.Sparky;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SparkyRepository extends JpaRepository<Sparky, Long> {
}
