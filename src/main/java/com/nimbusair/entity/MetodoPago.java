package com.nimbusair.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Métodos de pago disponibles")
public enum MetodoPago {
    @Schema(description = "Pago con tarjeta de crédito/débito")
    TARJETA,

    @Schema(description = "Pago mediante PayPal")
    PAYPAL,

    @Schema(description = "Pago por transferencia bancaria")
    TRANSFERENCIA
}