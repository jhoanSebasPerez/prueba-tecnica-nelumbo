package com.proyecto_nelumbo.pruebatecnica.controllers;

import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopParqueaderoResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopSocioResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.TopVehiculoResponse;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.VehiculoPrimeraVezResponse;
import com.proyecto_nelumbo.pruebatecnica.services.IndicadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indicadores")
public class IndicadoresController {

    private final IndicadorService indicadorService;

    public IndicadoresController(IndicadorService indicadorService) {
        this.indicadorService = indicadorService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    @GetMapping("/top-vehiculos")
    public ResponseEntity<List<TopVehiculoResponse>> obtenerTop10Vehiculos() {
        List<TopVehiculoResponse> topVehiculos = indicadorService.obtenerTop10Vehiculos();
        return ResponseEntity.ok(topVehiculos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    @GetMapping("/top-vehiculos/{parqueaderoId}")
    public ResponseEntity<List<TopVehiculoResponse>> obtenerTop10VehiculosPorParqueadero(
            @PathVariable Long parqueaderoId) {
        List<TopVehiculoResponse> topVehiculos = indicadorService.obtenerTop10VehiculosPorParqueadero(parqueaderoId);
        return ResponseEntity.ok(topVehiculos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    @GetMapping("/vehiculos-primera-vez/{parqueaderoId}")
    public ResponseEntity<List<VehiculoPrimeraVezResponse>> obtenerVehiculosPrimeraVez(
            @PathVariable Long parqueaderoId) {
        List<VehiculoPrimeraVezResponse> vehiculosPrimeraVez =
                indicadorService.obtenerVehiculosPrimeraVez(parqueaderoId);
        return ResponseEntity.ok(vehiculosPrimeraVez);
    }

    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/ganancias/{parqueaderoId}")
    public ResponseEntity<Double> obtenerGanancias(
            @PathVariable Long parqueaderoId,
            @RequestParam String periodo) {
        Double ganancias = indicadorService.obtenerGanancias(parqueaderoId, periodo);
        return ResponseEntity.ok(ganancias);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-socios-ingresos-semana")
    public ResponseEntity<List<TopSocioResponse>> obtenerTop3SociosConMasIngresosSemana() {
        List<TopSocioResponse> topSocios = indicadorService.obtenerTop3SociosConMasIngresosSemana();
        return ResponseEntity.ok(topSocios);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-parqueaderos-ganancia-semana")
    public ResponseEntity<List<TopParqueaderoResponse>> obtenerTop3ParqueaderosConMayorGananciaSemana() {
        List<TopParqueaderoResponse> topParqueaderos = indicadorService.obtenerTop3ParqueaderosConMayorGananciaSemana();
        return ResponseEntity.ok(topParqueaderos);
    }
}