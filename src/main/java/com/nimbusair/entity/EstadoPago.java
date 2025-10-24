package com.nimbusair.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados posibles de un pago")
public enum EstadoPago {
    @Schema(description = "Pago pendiente de procesar")
    PENDIENTE,

    @Schema(description = "Pago completado exitosamente")
    COMPLETADO,

    @Schema(description = "Pago rechazado o fallido")
    RECHAZADO,

    @Schema(description = "Pago cancelado por el usuario")
    CANCELADO
}