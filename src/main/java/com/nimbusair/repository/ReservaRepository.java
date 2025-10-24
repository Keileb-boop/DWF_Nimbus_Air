package com.nimbusair.repository;

import com.nimbusair.entity.Reserva;
import com.nimbusair.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Operation(summary = "Buscar reservas por usuario", description = "Obtiene todas las reservas de un usuario específico")
    List<Reserva> findByUsuario(
            @Parameter(description = "Usuario del cual obtener las reservas", required = true) User usuario);

    @Operation(summary = "Buscar reservas por email", description = "Obtiene todas las reservas de un usuario por su email")
    List<Reserva> findByUsuarioEmail(
            @Parameter(description = "Email del usuario", required = true) String email);

    List<Reserva> findByUsuarioId(Long usuarioId);

    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.usuario u")
    List<Reserva> findAllWithUsuario();
}