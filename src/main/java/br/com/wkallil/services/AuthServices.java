package br.com.wkallil.services;

import br.com.wkallil.data.dto.v1.security.AccountCredentialsDTO;
import br.com.wkallil.data.dto.v1.security.TokenDTO;
import br.com.wkallil.repositories.UserRepository;
import br.com.wkallil.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    public ResponseEntity<TokenDTO> signin(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = repository.findByUsername(credentials.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("Username " + credentials.getUsername() + " not found!");
        }

        var tokenResponse = tokenProvider.createAccessToken(
                credentials.getUsername(),
                user.getRoles()
        );

        return ResponseEntity.ok(tokenResponse);
    }
}
