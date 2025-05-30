package com.greenloop.sparky.Empresa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greenloop.sparky.Sparky.domain.Sparky;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.restriction.domain.Restriction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sparky_id")
    @JsonIgnore
    private Sparky sparky;

    @Column(nullable = false)
    private String nombreEmpresa;

    private String admin;

    @Column(nullable = false)
    private String ruc;

    @Column(nullable = false)
    private ZonedDateTime fechaAfiliacion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToOne
    @JoinColumn(name = "admin_id", unique = true)
    @JsonManagedReference
    private UserAccount administrador;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserAccount> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Restriction> restrictions;



    @PrePersist
    public void prePersist() {
        this.fechaAfiliacion = ZonedDateTime.now();
    }
}
