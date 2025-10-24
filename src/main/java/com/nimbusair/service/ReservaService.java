package com.nimbusair.service;

import com.nimbusair.entity.Reserva;
import com.nimbusair.entity.User;
import com.nimbusair.entity.ReservaUpdateRequest;
import com.nimbusair.repository.ReservaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Operation(summary = "Guardar reserva", description = "Guarda o actualiza una reserva en la base de datos")
    public Reserva guardarReserva(
            @Parameter(description = "Reserva a guardar", required = true) Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Operation(summary = "Obtener reservas por usuario", description = "Obtiene todas las reservas de un usuario específico")
    public List<Reserva> obtenerReservasPorUsuario(
            @Parameter(description = "Usuario del cual obtener las reservas", required = true) User usuario) {
        return reservaRepository.findByUsuario(usuario);
    }


    @Operation(summary = "Actualizar reserva", description = "Actualiza los datos de una reserva existente")
    public Reserva actualizarReserva(
            @Parameter(description = "ID de la reserva a actualizar", required = true) Long reservaId,
            @Parameter(description = "Datos actualizados de la reserva", required = true) ReservaUpdateRequest updateRequest) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + reservaId));

        if (!"CONFIRMADA".equals(reserva.getEstado())) {
            throw new RuntimeException("Solo se pueden modificar reservas CONFIRMADAS");
        }

        if (updateRequest.getPaisOrigen() != null) reserva.setPaisOrigen(updateRequest.getPaisOrigen());
        if (updateRequest.getCiudadOrigen() != null) reserva.setCiudadOrigen(updateRequest.getCiudadOrigen());
        if (updateRequest.getPaisDestino() != null) reserva.setPaisDestino(updateRequest.getPaisDestino());
        if (updateRequest.getCiudadDestino() != null) reserva.setCiudadDestino(updateRequest.getCiudadDestino());
        if (updateRequest.getFechaVuelo() != null) reserva.setFechaVuelo(updateRequest.getFechaVuelo());
        if (updateRequest.getHoraSalida() != null) reserva.setHoraSalida(updateRequest.getHoraSalida());
        if (updateRequest.getAerolinea() != null) reserva.setAerolinea(updateRequest.getAerolinea());
        if (updateRequest.getClaseVuelo() != null) reserva.setClaseVuelo(updateRequest.getClaseVuelo());
        if (updateRequest.getAsientoPreferido() != null) reserva.setAsientoPreferido(updateRequest.getAsientoPreferido());

        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAllWithUsuario();
    }

    @Operation(summary = "Obtener reservas por ID de usuario", description = "Obtiene reservas por ID de usuario (para admin)")
    public List<Reserva> obtenerReservasPorUsuarioId(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

}