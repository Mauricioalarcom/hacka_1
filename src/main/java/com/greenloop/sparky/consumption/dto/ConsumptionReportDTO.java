package com.greenloop.sparky.consumption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionReportDTO {
    private Long empresaId;
    private String empresaNombre;
    private int creditosDisponibles;
    private int creditosGastados;
    private int creditosTotales;
    private Map<String, Integer> consumoPorModelo;
}
