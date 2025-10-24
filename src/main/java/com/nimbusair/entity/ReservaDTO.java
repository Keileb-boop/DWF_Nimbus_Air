package com.nimbusair.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDTO {
    private Long id;
    private String nombreCompleto;
    private String numeroPasaporte;
    private LocalDate fechaNacimiento;
    private String paisOrigen;
    private String ciudadOrigen;
    private String paisDestino;
    private String ciudadDestino;
    private LocalDate fechaVuelo;
    private LocalTime horaSalida;
    private String aerolinea;
    private String claseVuelo;
    private String asientoPreferido;
    private String estado;
    private LocalDate fechaReserva;


    private String usuarioNombre;
    private String usuarioEmail;
    private String usuarioRol;


    public ReservaDTO(Reserva reserva) {
        this.id = reserva.getId();
        this.nombreCompleto = reserva.getNombreCompleto();
        this.numeroPasaporte = reserva.getNumeroPasaporte();
        this.fechaNacimiento = reserva.getFechaNacimiento();
        this.paisOrigen = reserva.getPaisOrigen();
        this.ciudadOrigen = reserva.getCiudadOrigen();
        this.paisDestino = reserva.getPaisDestino();
        this.ciudadDestino = reserva.getCiudadDestino();
        this.fechaVuelo = reserva.getFechaVuelo();
        this.horaSalida = reserva.getHoraSalida();
        this.aerolinea = reserva.getAerolinea();
        this.claseVuelo = reserva.getClaseVuelo();
        this.asientoPreferido = reserva.getAsientoPreferido();
        this.estado = reserva.getEstado();
        this.fechaReserva = LocalDate.from(reserva.getFechaReserva());


        if (reserva.getUsuario() != null) {
            this.usuarioNombre = reserva.getUsuario().getName();
            this.usuarioEmail = reserva.getUsuario().getEmail();
            this.usuarioRol = reserva.getUsuario().getRole().name();
        } else {

            this.usuarioNombre = "Usuario no encontrado";
            this.usuarioEmail = "N/A";
            this.usuarioRol = "N/A";
        }
    }


    public Long getId() { return id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getNumeroPasaporte() { return numeroPasaporte; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getPaisOrigen() { return paisOrigen; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getPaisDestino() { return paisDestino; }
    public String getCiudadDestino() { return ciudadDestino; }
    public LocalDate getFechaVuelo() { return fechaVuelo; }
    public LocalTime getHoraSalida() { return horaSalida; }
    public String getAerolinea() { return aerolinea; }
    public String getClaseVuelo() { return claseVuelo; }
    public String getAsientoPreferido() { return asientoPreferido; }
    public String getEstado() { return estado; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public String getUsuarioRol() { return usuarioRol; }
}