package com.nimbusair.service;

import com.nimbusair.entity.User;
import com.nimbusair.entity.Role;
import com.nimbusair.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    public User createUser(
            @Parameter(description = "Email del usuario", required = true) String email,
            @Parameter(description = "Contraseña del usuario", required = true) String password,
            @Parameter(description = "Nombre del usuario", required = true) String name,
            @Parameter(description = "Rol del usuario", required = true) Role role) {
        User user = new User(email, passwordEncoder.encode(password), name, role);
        return userRepository.save(user);
    }

    @Operation(summary = "Buscar usuario por email", description = "Busca un usuario por su dirección de email")
    public Optional<User> findByEmail(
            @Parameter(description = "Email del usuario", required = true) String email) {
        return userRepository.findByEmail(email);
    }

    @Operation(summary = "Verificar existencia de email", description = "Verifica si un email ya está registrado en el sistema")
    public boolean existsByEmail(
            @Parameter(description = "Email a verificar", required = true) String email) {
        return userRepository.existsByEmail(email);
    }

    @Operation(summary = "Guardar usuario", description = "Guarda o actualiza un usuario en la base de datos")
    public User save(
            @Parameter(description = "Usuario a guardar", required = true) User user) {
        return userRepository.save(user);
    }

    @Operation(summary = "Buscar usuario por ID", description = "Busca un usuario por su ID único")
    public Optional<User> findById(
            @Parameter(description = "ID del usuario", required = true) Long id) {
        return userRepository.findById(id);
    }
}