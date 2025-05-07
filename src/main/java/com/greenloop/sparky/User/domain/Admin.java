package com.greenloop.sparky.User.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greenloop.sparky.Empresa.domain.Empresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Admin extends UserAccount {

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "empresa_id", nullable = false, unique = true)
    private Empresa empresa;
}
