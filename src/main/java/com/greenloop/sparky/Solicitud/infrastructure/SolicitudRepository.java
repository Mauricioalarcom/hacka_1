package com.greenloop.sparky.Solicitud.infrastructure;

import com.greenloop.sparky.Solicitud.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
