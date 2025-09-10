package br.com.wkallil.services;

import br.com.wkallil.data.dto.v1.security.AccountCredentialsDTO;
import br.com.wkallil.data.dto.v1.security.TokenDTO;
import br.com.wkallil.exceptions.RequiredObjectIsNullException;
import br.com.wkallil.mapper.UserMapper;
import br.com.wkallil.models.User;
import br.com.wkallil.repositories.UserRepository;
import br.com.wkallil.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServices {

    Logger logger = LoggerFactory.getLogger(AuthServices.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

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

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);
        TokenDTO tokenResponse;

        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user) {

        if (user == null) {
            logger.info("Person is null!");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating one new user!");

        var entity = new User();
        entity.setFullName(user.getFullName());
        entity.setUsername(user.getUsername());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);
        return userMapper.toDTO(repository.save(entity));
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder pbkdf2Enconder = new Pbkdf2PasswordEncoder(
                "", 8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> enconders = new HashMap<>();
        enconders.put("pbkdf2", pbkdf2Enconder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", enconders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Enconder);
        return passwordEncoder.encode(password);

    }
}
