package com.greenloop.sparky.User.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greenloop.sparky.Empresa.domain.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee extends UserAccount {

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
