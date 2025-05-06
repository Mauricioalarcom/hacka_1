package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Sparky.domain.Sparky;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sparky_id", nullable = false)
    private Sparky sparky;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer RUC;

    @Column(nullable = false)
    private ZonedDateTime fechaAfiliacion;

    @Column(nullable = false)
    private Estado estado;



}
