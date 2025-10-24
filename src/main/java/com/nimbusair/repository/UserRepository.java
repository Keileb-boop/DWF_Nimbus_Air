package com.nimbusair.repository;

import com.nimbusair.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Operation(summary = "Buscar usuario por email", description = "Busca un usuario por su dirección de email")
    Optional<User> findByEmail(
            @Parameter(description = "Email del usuario", required = true) String email);

    @Operation(summary = "Verificar existencia de email", description = "Verifica si un email ya está registrado en el sistema")
    boolean existsByEmail(
            @Parameter(description = "Email a verificar", required = true) String email);
}