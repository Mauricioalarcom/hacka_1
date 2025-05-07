package com.greenloop.sparky.consumption.domain;

import com.greenloop.sparky.Empresa.domain.Empresa;
import com.greenloop.sparky.User.domain.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumptions")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Column(nullable = false)
    private String modelType;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private Integer tokensConsumed;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    @Column(nullable = false)
    private Long requestId;


    @Column(nullable = false)
    private Double cost;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingStatus billingStatus = BillingStatus.PENDING;


    @Column
    private String billingPeriod;

    @PrePersist
    public void prePersist() {
        this.timestamp = ZonedDateTime.now();
        this.billingPeriod = String.format("%d-%02d", 
                this.timestamp.getYear(), 
                this.timestamp.getMonthValue());
    }

    // Enum para el estado de facturación
    public enum BillingStatus {
        PENDING,      // Pendiente de facturar
        INVOICED,     // Incluido en una factura
        PAID          // Pagado por el cliente
    }
}