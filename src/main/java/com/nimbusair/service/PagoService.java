package com.nimbusair.service;

import com.nimbusair.entity.*;
import com.nimbusair.repository.PagoRepository;
import com.nimbusair.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Operation(summary = "Procesar pago", description = "Procesa un pago para una reserva específica")
    public Pago procesarPago(
            @Parameter(description = "Solicitud de pago", required = true) PagoRequest pagoRequest) {

        Reserva reserva = reservaRepository.findById(pagoRequest.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + pagoRequest.getReservaId()));

        if (pagoRepository.existsByReserva(reserva)) {
            throw new RuntimeException("Ya existe un pago para esta reserva");
        }

        Double monto = calcularMontoReserva(reserva);

        Pago pago = new Pago(reserva, monto, pagoRequest.getMetodoPago());

        if (pagoRequest.getMetodoPago() == MetodoPago.TARJETA) {
            pago.setNumeroTarjeta(encriptarNumeroTarjeta(pagoRequest.getNumeroTarjeta()));
            pago.setFechaVencimiento(pagoRequest.getFechaVencimiento());
            pago.setCvv(pagoRequest.getCvv());
        }

        pago.setEstado(EstadoPago.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());

        return pagoRepository.save(pago);
    }

    @Operation(summary = "Obtener pago por reserva", description = "Obtiene el pago asociado a una reserva")
    public Optional<Pago> obtenerPagoPorReserva(
            @Parameter(description = "Reserva para buscar el pago", required = true) Reserva reserva) {
        return pagoRepository.findByReserva(reserva);
    }

    @Operation(summary = "Obtener pago por ID", description = "Obtiene un pago por su ID único")
    public Optional<Pago> obtenerPagoPorId(
            @Parameter(description = "ID del pago", required = true) Long id) {
        return pagoRepository.findById(id);
    }

    @Operation(summary = "Cancelar pago", description = "Cancela un pago existente")
    public Pago cancelarPago(
            @Parameter(description = "ID del pago a cancelar", required = true) Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + pagoId));

        pago.setEstado(EstadoPago.CANCELADO);
        pago.setUpdatedAt(LocalDateTime.now());

        return pagoRepository.save(pago);
    }

    private Double calcularMontoReserva(Reserva reserva) {
        return 300.00;
    }

    private String encriptarNumeroTarjeta(String numeroTarjeta) {
        if (numeroTarjeta == null || numeroTarjeta.length() < 4) {
            return "************";
        }
        return "************" + numeroTarjeta.substring(numeroTarjeta.length() - 4);
    }
}