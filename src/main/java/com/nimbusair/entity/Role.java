package com.nimbusair.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Roles disponibles en el sistema")
public enum Role {
    @Schema(description = "Usuario normal del sistema")
    USER,

    @Schema(description = "Administrador del sistema")
    ADMIN
}