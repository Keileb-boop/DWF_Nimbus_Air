package com.nimbusair.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Solicitud para actualizar una reserva existente")
public class ReservaUpdateRequest {

    @Schema(description = "País de origen", example = "Colombia")
    private String paisOrigen;

    @Schema(description = "Ciudad de origen", example = "Bogotá")
    private String ciudadOrigen;

    @Schema(description = "País de destino", example = "España")
    private String paisDestino;

    @Schema(description = "Ciudad de destino", example = "Madrid")
    private String ciudadDestino;

    @Schema(description = "Fecha del vuelo", example = "2025-01-20")
    private LocalDate fechaVuelo;

    @Schema(description = "Hora de salida", example = "14:30:00")
    private LocalTime horaSalida;

    @Schema(description = "Aerolínea", example = "Nimbus Air")
    private String aerolinea;

    @Schema(description = "Clase de vuelo", example = "Económica")
    private String claseVuelo;

    @Schema(description = "Asiento preferido", example = "14A")
    private String asientoPreferido;

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    public String getCiudadOrigen() { return ciudadOrigen; }
    public void setCiudadOrigen(String ciudadOrigen) { this.ciudadOrigen = ciudadOrigen; }

    public String getPaisDestino() { return paisDestino; }
    public void setPaisDestino(String paisDestino) { this.paisDestino = paisDestino; }

    public String getCiudadDestino() { return ciudadDestino; }
    public void setCiudadDestino(String ciudadDestino) { this.ciudadDestino = ciudadDestino; }

    public LocalDate getFechaVuelo() { return fechaVuelo; }
    public void setFechaVuelo(LocalDate fechaVuelo) { this.fechaVuelo = fechaVuelo; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public String getAerolinea() { return aerolinea; }
    public void setAerolinea(String aerolinea) { this.aerolinea = aerolinea; }

    public String getClaseVuelo() { return claseVuelo; }
    public void setClaseVuelo(String claseVuelo) { this.claseVuelo = claseVuelo; }

    public String getAsientoPreferido() { return asientoPreferido; }
    public void setAsientoPreferido(String asientoPreferido) { this.asientoPreferido = asientoPreferido; }
}