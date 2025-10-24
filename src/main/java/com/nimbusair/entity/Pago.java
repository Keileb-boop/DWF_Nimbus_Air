package com.nimbusair.entity;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Schema(description = "Entidad que representa un pago de reserva")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pago", example = "1")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    @Schema(description = "Reserva asociada al pago")
    private Reserva reserva;

    @Column(nullable = false)
    @Schema(description = "Monto total del pago", example = "300.00")
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    @Schema(description = "Método de pago utilizado")
    private MetodoPago metodoPago;

    @Column(name = "numero_tarjeta")
    @Schema(description = "Número de tarjeta (encriptado)", example = "************1234")
    private String numeroTarjeta;

    @Column(name = "fecha_vencimiento")
    @Schema(description = "Fecha de vencimiento de la tarjeta", example = "2025-12-31")
    private LocalDate fechaVencimiento;

    @Column(name = "cvv")
    @Schema(description = "Código de seguridad CVV", example = "123")
    private String cvv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual del pago")
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(name = "fecha_pago")
    @Schema(description = "Fecha y hora en que se realizó el pago")
    private LocalDateTime fechaPago;

    @Column(name = "codigo_transaccion")
    @Schema(description = "Código único de transacción", example = "TXN-123456789")
    private String codigoTransaccion;

    @Column(name = "created_at")
    @Schema(description = "Fecha de creación del registro de pago")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    public Pago() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.codigoTransaccion = generarCodigoTransaccion();
    }

    public Pago(Reserva reserva, Double monto, MetodoPago metodoPago) {
        this();
        this.reserva = reserva;
        this.monto = monto;
        this.metodoPago = metodoPago;
    }

    private String generarCodigoTransaccion() {
        return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public String getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}