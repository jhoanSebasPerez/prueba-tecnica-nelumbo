package com.proyecto_nelumbo.servicioemail.controllers;

import com.proyecto_nelumbo.servicioemail.dtos.EmailIngresoRequest;
import com.proyecto_nelumbo.servicioemail.dtos.EmailResponse;
import com.proyecto_nelumbo.servicioemail.dtos.RegistroSocioRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @PostMapping("/registro-ingreso")
    public ResponseEntity<EmailResponse> enviarRegistroIngreso(@RequestBody EmailIngresoRequest request) {
        logger.info("Simulación de envío de correo cuando se realiza un ingreso:");
        logger.info("email: {}", request.email());
        logger.info("placa: {}", request.placa());
        logger.info("mensaje: {}", request.mensaje());
        logger.info("parqueaderoId: {}", request.parqueaderoId());

        return ResponseEntity.ok(new EmailResponse("Correo Registro Ingreso enviado"));
    }

    @PostMapping("/registro-socio")
    public ResponseEntity<EmailResponse> enviarRegistroSocio(@RequestBody RegistroSocioRequest request) {
        logger.info("Simulación de envío de correo cuando se registra a un socio:");
        logger.info("email: {}", request.email());
        logger.info("mensaje: {}", request.mensaje());
        return ResponseEntity.ok(new EmailResponse("Correo Registro Socio enviado"));
    }
}