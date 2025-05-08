package com.greenloop.sparky.consumption.domain;

import com.greenloop.sparky.Empresa.domain.Empresa;
import com.greenloop.sparky.Empresa.domain.EmpresaService;
import com.greenloop.sparky.User.domain.UserAccount;
import com.greenloop.sparky.User.domain.UserAccountService;

import com.greenloop.sparky.consumption.exceptions.ConsumptionException;
import com.greenloop.sparky.consumption.infraestructure.ConsumptionRepository;
import com.greenloop.sparky.consumption.exceptions.ConsumptionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {

    private static ConsumptionRepository consumptionRepository = null;
    private final EmpresaService empresaService;
    private static UserAccountService userService = null;

    public ConsumptionService(
            ConsumptionRepository consumptionRepository,
            EmpresaService empresaService,
            UserAccountService userService) {
        ConsumptionService.consumptionRepository = consumptionRepository;
        this.empresaService = empresaService;
        ConsumptionService.userService = userService;
    }

    @Transactional
    public Consumption registerConsumption(Consumption consumption) {
        validateConsumption(consumption);
        return consumptionRepository.save(consumption);
    }


    @Transactional
    public Consumption createConsumption(
            Long empresaId,
            Long userId,
            String modelType,
            String modelName,
            Integer tokensConsumed,
            Long requestId,
            Double cost) {
        
        Empresa empresa = empresaService.getEmpresaById(empresaId);
        if (empresa == null) {
            throw new ConsumptionException("Empresa no encontrada con ID: " + empresaId);
        }
        
        UserAccount user = userService.getUserById(userId);
        if (user == null) {
            throw new ConsumptionException("Usuario no encontrado con ID: " + userId);
        }
        
        Consumption consumption = new Consumption();
        consumption.setEmpresa(empresa);
        consumption.setUser(user);
        consumption.setModelType(modelType);
        consumption.setModelName(modelName);
        consumption.setTokensConsumed(tokensConsumed);
        consumption.setRequestId(requestId);
        consumption.setCost(cost);
        consumption.setTimestamp(ZonedDateTime.now());
        consumption.setBillingStatus(Consumption.BillingStatus.PENDING);
        consumption.setBillingPeriod(getCurrentBillingPeriod());
        
        return consumptionRepository.save(consumption);
    }


    public Consumption getConsumptionById(Long id) {
        return consumptionRepository.findById(id)
                .orElseThrow(() -> new ConsumptionNotFoundException(id));
    }

    public List<Consumption> getAllConsumptions() {
        return consumptionRepository.findAll();
    }


    public List<Consumption> getConsumptionsByEmpresa(Long empresaId) {
        Empresa empresa = empresaService.getEmpresaById(empresaId);
        if (empresa == null) {
            throw new ConsumptionException("Empresa no encontrada con ID: " + empresaId);
        }
        return consumptionRepository.findByEmpresaId(empresaId);
    }

    public List<Consumption> getConsumptionsByUser(Long userId) {
        UserAccount user = userService.getUserById(userId);
        if (user == null) {
            throw new ConsumptionException("Usuario no encontrado con ID: " + userId);
        }
        return consumptionRepository.findByUserId(userId);
    }

    public Map<String, Object> getEmpresaConsumptionReport(Long empresaId) {
        Empresa empresa = empresaService.getEmpresaById(empresaId);
        if (empresa == null) {
            throw new ConsumptionException("Empresa no encontrada con ID: " + empresaId);
        }
        
        List<Consumption> consumptions = consumptionRepository.findByEmpresaId(empresaId);

        int totalTokens = consumptions.stream()
                .mapToInt(Consumption::getTokensConsumed)
                .sum();
                
        double totalCost = consumptions.stream()
                .mapToDouble(Consumption::getCost)
                .sum();
                

        Map<String, Integer> tokensByModel = consumptions.stream()
                .collect(Collectors.groupingBy(
                    Consumption::getModelName,
                    Collectors.summingInt(Consumption::getTokensConsumed)
                ));
                
        Map<String, Double> costByModel = consumptions.stream()
                .collect(Collectors.groupingBy(
                    Consumption::getModelName,
                    Collectors.summingDouble(Consumption::getCost)
                ));

        Map<String, Double> costByPeriod = consumptions.stream()
                .collect(Collectors.groupingBy(
                    Consumption::getBillingPeriod,
                    Collectors.summingDouble(Consumption::getCost)
                ));
        

        Map<String, Object> report = new HashMap<>();
        report.put("empresaId", empresaId);
        report.put("empresaNombre", empresa.getNombreEmpresa());
        report.put("totalTokens", totalTokens);
        report.put("totalCost", totalCost);
        report.put("tokensByModel", tokensByModel);
        report.put("costByModel", costByModel);
        report.put("costByPeriod", costByPeriod);
        
        return report;
    }


    public static Map<String, Object> getUserConsumptionReport(Long userId) {
        UserAccount user = userService.getUserById(userId);
        if (user == null) {
            throw new ConsumptionException("Usuario no encontrado con ID: " + userId);
        }
        
        List<Consumption> consumptions = consumptionRepository.findByUserId(userId);
        

        int totalTokens = consumptions.stream()
                .mapToInt(Consumption::getTokensConsumed)
                .sum();
                
        double totalCost = consumptions.stream()
                .mapToDouble(Consumption::getCost)
                .sum();
                

        Map<String, Integer> tokensByModel = consumptions.stream()
                .collect(Collectors.groupingBy(
                    Consumption::getModelName,
                    Collectors.summingInt(Consumption::getTokensConsumed)
                ));
        
        // Construir reporte
        Map<String, Object> report = new HashMap<>();
        report.put("userId", userId);
        report.put("userName", user.getUsername());
        report.put("empresaId", user.getEmpresa().getId());
        report.put("empresaNombre", user.getEmpresa().getNombreEmpresa());
        report.put("totalTokens", totalTokens);
        report.put("totalCost", totalCost);
        report.put("tokensByModel", tokensByModel);
        
        return report;
    }


    public List<Consumption> getConsumptionsByBillingPeriod(String billingPeriod) {
        return consumptionRepository.findByBillingPeriod(billingPeriod);
    }
    public String getCurrentBillingPeriod() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    @Transactional
    public Consumption updateBillingStatus(Long id, Consumption.BillingStatus status) {
        Consumption consumption = getConsumptionById(id);
        consumption.setBillingStatus(status);
        return consumptionRepository.save(consumption);
    }


    @Transactional
    public int markPeriodAsInvoiced(String billingPeriod) {
        return consumptionRepository.updateBillingStatusForPeriod(
                billingPeriod, 
                Consumption.BillingStatus.PENDING,
                Consumption.BillingStatus.INVOICED
        );
    }


    private void validateConsumption(Consumption consumption) {
        if (consumption.getEmpresa() == null) {
            throw new ConsumptionException("La empresa no puede ser nula");
        }
        
        if (consumption.getUser() == null) {
            throw new ConsumptionException("El usuario no puede ser nulo");
        }
        
        if (consumption.getModelType() == null || consumption.getModelType().trim().isEmpty()) {
            throw new ConsumptionException("El tipo de modelo no puede estar vacío");
        }
        
        if (consumption.getModelName() == null || consumption.getModelName().trim().isEmpty()) {
            throw new ConsumptionException("El nombre del modelo no puede estar vacío");
        }
        
        if (consumption.getTokensConsumed() == null || consumption.getTokensConsumed() < 0) {
            throw new ConsumptionException("Los tokens consumidos deben ser un valor válido");
        }
        
        if (consumption.getCost() == null || consumption.getCost() < 0) {
            throw new ConsumptionException("El costo debe ser un valor válido");
        }
    }
}