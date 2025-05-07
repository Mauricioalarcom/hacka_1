package com.greenloop.sparky.consumption.infraestructure;

import com.greenloop.sparky.consumption.domain.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    List<Consumption> findByEmpresaId(Long empresaId);

    List<Consumption> findByUserId(Long userId);
    

    List<Consumption> findByBillingPeriod(String billingPeriod);
    

    List<Consumption> findByModelName(String modelName);
    
    // Consultas combinadas
    List<Consumption> findByEmpresaIdAndModelName(Long empresaId, String modelName);
    List<Consumption> findByUserIdAndModelName(Long userId, String modelName);
    List<Consumption> findByEmpresaIdAndBillingPeriod(Long empresaId, String billingPeriod);

    @Modifying
    @Query("UPDATE Consumption c SET c.billingStatus = :newStatus " +
           "WHERE c.billingPeriod = :billingPeriod AND c.billingStatus = :currentStatus")
    int updateBillingStatusForPeriod(
            @Param("billingPeriod") String billingPeriod,
            @Param("currentStatus") Consumption.BillingStatus currentStatus,
            @Param("newStatus") Consumption.BillingStatus newStatus);
}