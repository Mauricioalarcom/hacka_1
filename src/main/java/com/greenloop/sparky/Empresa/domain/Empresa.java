package com.greenloop.sparky.Empresa.domain;

import com.greenloop.sparky.Sparky.domain.Sparky;
import com.greenloop.sparky.User.domain.UserAccount;
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

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<UserAccount> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Restriction> restrictions;

    @PrePersist
    public void prePersist() {
        this.fechaAfiliacion = ZonedDateTime.now();
    }
}
