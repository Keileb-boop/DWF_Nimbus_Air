package com.nimbusair.service;

import com.nimbusair.entity.User;
import com.nimbusair.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Operation(summary = "Cargar usuario por email", description = "Carga los detalles del usuario por su dirección de email para autenticación")
    public UserDetails loadUserByUsername(
            @Parameter(description = "Email del usuario", required = true) String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("Usuario desactivado: " + email);
        }

        return user;
    }
}