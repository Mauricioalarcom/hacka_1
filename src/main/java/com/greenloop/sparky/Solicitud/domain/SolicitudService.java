package com.greenloop.sparky.Solicitud.domain;

import com.greenloop.sparky.Solicitud.infrastructure.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService {


    @Autowired
    private SolicitudRepository solicitudRepository;
}
