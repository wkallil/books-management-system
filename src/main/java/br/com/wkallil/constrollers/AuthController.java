package br.com.wkallil.constrollers;

import br.com.wkallil.data.dto.v1.security.AccountCredentialsDTO;
import br.com.wkallil.data.dto.v1.security.TokenDTO;
import br.com.wkallil.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices service;

    @Operation(summary = "Authenticates a user and returns a token")
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentialsDTO) {
        if (credentialsIsInvalid(credentialsDTO)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = service.signin(credentialsDTO);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return ResponseEntity.ok().body(token);
    }

    private static boolean credentialsIsInvalid(AccountCredentialsDTO credentialsDTO) {
        return credentialsDTO == null || StringUtils.isBlank(credentialsDTO.getPassword()) || StringUtils.isBlank(credentialsDTO.getUsername());
    }
}
