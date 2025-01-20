package com.proyecto_nelumbo.pruebatecnica.controllers;

import com.proyecto_nelumbo.pruebatecnica.dtos.request.CreateParqueaderoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.request.RegistrarSalidaRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.request.RegistrarVehiculoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.request.UpdateParqueaderoRequest;
import com.proyecto_nelumbo.pruebatecnica.dtos.response.*;
import com.proyecto_nelumbo.pruebatecnica.services.ParqueaderoService;
import com.proyecto_nelumbo.pruebatecnica.services.RegistroService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parqueaderos")
public class ParqueaderoController {

    private final ParqueaderoService parqueaderoService;
    private final RegistroService registroService;

    public ParqueaderoController(ParqueaderoService parqueaderoService, RegistroService registroService) {
        this.parqueaderoService = parqueaderoService;
        this.registroService = registroService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ParqueaderoResponse> crearParqueadero(
            @Valid @RequestBody CreateParqueaderoRequest request
    ) {
        ParqueaderoResponse response = parqueaderoService.crearParqueadero(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{parqueaderoId}/asociar-socio/{socioId}")
    public ResponseEntity<ParqueaderoResponse> asociarSocio(
            @PathVariable Long parqueaderoId,
            @PathVariable Long socioId
    ) {
        ParqueaderoResponse response = parqueaderoService.asociarSocioAParqueadero(parqueaderoId, socioId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('SOCIO')")
    @GetMapping("/mis-parqueaderos")
    public ResponseEntity<List<ParqueaderoResponse>> listarMisParqueaderos() {
        List<ParqueaderoResponse> lista = parqueaderoService.listarParqueaderosSocio();
        return ResponseEntity.ok(lista);
    }

    @PreAuthorize("hasRole('SOCIO')")
    @PostMapping("/{parqueaderoId}/registrar-ingreso")
    public ResponseEntity<RegistroResponse> registrarVehiculo(
            @PathVariable Long parqueaderoId,
            @Valid @RequestBody RegistrarVehiculoRequest request
    ) {
        RegistroResponse response = registroService.registrarVehiculo(parqueaderoId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('SOCIO')")
    @PostMapping("/{parqueaderoId}/registrar-salida")
    public ResponseEntity<HistorialResponse> registrarSalida(
            @PathVariable Long parqueaderoId,
            @Valid @RequestBody RegistrarSalidaRequest request
    ) {
        HistorialResponse response = registroService.registrarSalida(parqueaderoId, request.placa());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    @GetMapping("/{parqueaderoId}/listar-registros")
    public ResponseEntity<List<RegistroResponse>> listarRegistrosPorParqueadero(
            @PathVariable Long parqueaderoId
    ) {
        List<RegistroResponse> registros = registroService.listarRegistrosPorParqueadero(parqueaderoId);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ParqueaderoResponse>> obtenerParqueaderos() {
        List<ParqueaderoResponse> parqueaderos = parqueaderoService.obtenerParqueaderos();
        return ResponseEntity.ok(parqueaderos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    public ParqueaderoResponse actualizarParqueadero(
            @PathVariable Long id,
            @RequestBody @Valid UpdateParqueaderoRequest request) {
        return parqueaderoService.actualizarParqueadero(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    public ParqueaderoDetalleResponse obtenerParqueadero(@PathVariable Long id) {
        return parqueaderoService.obtenerParqueadero(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarParqueadero(@PathVariable Long id) {
        parqueaderoService.eliminarParqueadero(id);
        return ResponseEntity.noContent().build();
    }
}