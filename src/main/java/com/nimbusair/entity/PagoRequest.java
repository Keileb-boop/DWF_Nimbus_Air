package com.nimbusair.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Solicitud para procesar un pago")
public class PagoRequest {

    @Schema(description = "ID de la reserva a pagar", example = "1", required = true)
    private Long reservaId;

    @Schema(description = "Método de pago seleccionado", required = true)
    private MetodoPago metodoPago;

    @Schema(description = "Número de tarjeta (solo para método TARJETA)", example = "4111111111111111")
    private String numeroTarjeta;

    @Schema(description = "Fecha de vencimiento de la tarjeta (solo para método TARJETA)", example = "2025-12-31")
    private LocalDate fechaVencimiento;

    @Schema(description = "CVV de la tarjeta (solo para método TARJETA)", example = "123")
    private String cvv;

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}