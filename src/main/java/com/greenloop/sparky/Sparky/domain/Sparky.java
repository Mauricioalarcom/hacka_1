package com.greenloop.sparky.Sparky.domain;

import com.greenloop.sparky.Empresa.domain.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sparky {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "sparky", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empresa> empresas;
}
