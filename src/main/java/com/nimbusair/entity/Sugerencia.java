package com.nimbusair.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sugerencias")
public class Sugerencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 1000)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSugerencia tipo;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private boolean revisada = false;


    public Sugerencia() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Sugerencia(String nombre, String email, String mensaje, TipoSugerencia tipo) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public TipoSugerencia getTipo() { return tipo; }
    public void setTipo(TipoSugerencia tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public boolean isRevisada() { return revisada; }
    public void setRevisada(boolean revisada) { this.revisada = revisada; }
}

