package com.greenloop.sparky.Solicitud.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SoliId;

    @Column(nullable = false)
    private String consulta;

    @Column(nullable = false)
    private String respuesta;

    @Column(nullable = false)
    private Integer TokensConsumidos;




}
