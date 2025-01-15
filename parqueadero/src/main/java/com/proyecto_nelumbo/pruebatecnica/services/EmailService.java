package com.proyecto_nelumbo.pruebatecnica.services;

import com.proyecto_nelumbo.servicioemail.dtos.EmailIngresoRequest;
import com.proyecto_nelumbo.servicioemail.dtos.EmailResponse;
import com.proyecto_nelumbo.servicioemail.dtos.RegistroSocioRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    private static final String URL_EMAIL = "http://localhost:8081/email";

    private final RestTemplate restTemplate;

    public EmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmailResponse enviarEmailIngreso(EmailIngresoRequest emailIngresoRequest){
        return restTemplate.postForEntity(URL_EMAIL + "/registro-ingreso", emailIngresoRequest, EmailResponse.class).getBody();
    }

    public EmailResponse enviarEmailRegistroSocio(RegistroSocioRequest registroSocioRequest){
        return restTemplate.postForEntity(URL_EMAIL + "/registro-socio", registroSocioRequest, EmailResponse.class).getBody();
    }

}
