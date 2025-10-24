package com.nimbusair.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
@Schema(description = "Entidad que representa una reserva de vuelo")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la reserva", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario que realizó la reserva")
    private User usuario;

    @NotBlank(message = "El país de origen es obligatorio")
    @Column(name = "pais_origen", nullable = false)
    @Schema(description = "País de origen del vuelo", example = "Colombia")
    private String paisOrigen;

    @NotBlank(message = "La ciudad de origen es obligatoria")
    @Column(name = "ciudad_origen", nullable = false)
    @Schema(description = "Ciudad de origen del vuelo", example = "Bogotá")
    private String ciudadOrigen;

    @NotBlank(message = "El país de destino es obligatorio")
    @Column(name = "pais_destino", nullable = false)
    @Schema(description = "País de destino del vuelo", example = "España")
    private String paisDestino;

    @NotBlank(message = "La ciudad de destino es obligatoria")
    @Column(name = "ciudad_destino", nullable = false)
    @Schema(description = "Ciudad de destino del vuelo", example = "Madrid")
    private String ciudadDestino;

    @NotNull(message = "La fecha del vuelo es obligatoria")
    @FutureOrPresent(message = "La fecha del vuelo no puede ser anterior a hoy")
    @Column(name = "fecha_vuelo", nullable = false)
    @Schema(description = "Fecha del vuelo", example = "2025-01-15")
    private LocalDate fechaVuelo;

    @NotNull(message = "La hora de salida es obligatoria")
    @Column(name = "hora_salida", nullable = false)
    @Schema(description = "Hora de salida del vuelo", example = "14:30:00")
    private LocalTime horaSalida;

    @NotBlank(message = "La aerolínea es obligatoria")
    @Schema(description = "Aerolínea del vuelo", example = "Nimbus Air")
    private String aerolinea;

    @NotBlank(message = "La clase de vuelo es obligatoria")
    @Column(name = "clase_vuelo")
    @Schema(description = "Clase del vuelo", example = "Económica")
    private String claseVuelo;

    @NotBlank(message = "El asiento preferido es obligatorio")
    @Column(name = "asiento_preferido")
    @Schema(description = "Asiento preferido", example = "14A")
    private String asientoPreferido;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(name = "nombre_completo")
    @Schema(description = "Nombre completo del pasajero", example = "Juan Pérez")
    private String nombreCompleto;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(name = "fecha_nacimiento")
    @Schema(description = "Fecha de nacimiento del pasajero", example = "1990-05-15")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El número de pasaporte es obligatorio")
    @Column(name = "numero_pasaporte")
    @Schema(description = "Número de pasaporte", example = "AB123456")
    private String numeroPasaporte;

    @Column(name = "fecha_reserva")
    @Schema(description = "Fecha y hora de la reserva")
    private LocalDateTime fechaReserva;

    @Schema(description = "Estado de la reserva", example = "CONFIRMADA")
    private String estado;

    public Reserva() {
        this.fechaReserva = LocalDateTime.now();
        this.estado = "CONFIRMADA";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

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

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNumeroPasaporte() { return numeroPasaporte; }
    public void setNumeroPasaporte(String numeroPasaporte) { this.numeroPasaporte = numeroPasaporte; }

    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}