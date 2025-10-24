package com.nimbusair.repository;

import com.nimbusair.entity.Pago;
import com.nimbusair.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    @Operation(summary = "Buscar pago por reserva", description = "Obtiene el pago asociado a una reserva específica")
    Optional<Pago> findByReserva(
            @Parameter(description = "Reserva para buscar el pago", required = true) Reserva reserva);

    @Operation(summary = "Buscar pago por código de transacción", description = "Obtiene un pago por su código único de transacción")
    Optional<Pago> findByCodigoTransaccion(
            @Parameter(description = "Código de transacción", required = true) String codigoTransaccion);

    @Operation(summary = "Verificar existencia de pago por reserva", description = "Verifica si ya existe un pago para una reserva")
    boolean existsByReserva(
            @Parameter(description = "Reserva a verificar", required = true) Reserva reserva);
}