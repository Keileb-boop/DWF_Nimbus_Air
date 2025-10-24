package com.nimbusair.controller;

import com.nimbusair.entity.*;
import com.nimbusair.service.PagoService;
import com.nimbusair.service.JwtService;
import com.nimbusair.repository.UserRepository;
import com.nimbusair.repository.ReservaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
@Tag(name = "Pagos", description = "Gestión de pagos de reservas")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @PostMapping
    @Operation(summary = "Procesar pago", description = "Procesa el pago para una reserva específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago procesado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos del pago"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<?> procesarPago(
            @Parameter(description = "Token JWT de autenticación", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "Datos del pago", required = true)
            @RequestBody PagoRequest pagoRequest) {

        try {
            String jwt = token.replace("Bearer ", "");
            Long userId = jwtService.extractUserId(jwt);

            User usuario = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Pago pago = pagoService.procesarPago(pagoRequest);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Pago procesado exitosamente");
            respuesta.put("pago", pago);
            respuesta.put("codigoTransaccion", pago.getCodigoTransaccion());
            respuesta.put("estado", pago.getEstado());

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al procesar pago: " + e.getMessage()));
        }
    }

    @GetMapping("/reserva/{reservaId}")
    @Operation(summary = "Obtener pago por reserva", description = "Obtiene el pago asociado a una reserva específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<?> obtenerPagoPorReserva(
            @Parameter(description = "Token JWT de autenticación", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long reservaId) {

        try {
            String jwt = token.replace("Bearer ", "");
            Long userId = jwtService.extractUserId(jwt);

            User usuario = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Reserva reserva = reservaRepository.findById(reservaId)
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            if (!reserva.getUsuario().getId().equals(userId)) {
                return ResponseEntity.status(403).body(Map.of("error", "No tienes permisos para ver esta reserva"));
            }

            var pago = pagoService.obtenerPagoPorReserva(reserva);

            if (pago.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "No se encontró pago para esta reserva"));
            }

            return ResponseEntity.ok(pago.get());

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al obtener pago: " + e.getMessage()));
        }
    }

    @PostMapping("/{pagoId}/cancelar")
    @Operation(summary = "Cancelar pago", description = "Cancela un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago cancelado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<?> cancelarPago(
            @Parameter(description = "Token JWT de autenticación", required = true)
            @RequestHeader("Authorization") String token,

            @Parameter(description = "ID del pago a cancelar", required = true)
            @PathVariable Long pagoId) {

        try {
            String jwt = token.replace("Bearer ", "");
            Long userId = jwtService.extractUserId(jwt);

            Pago pago = pagoService.obtenerPagoPorId(pagoId)
                    .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

            if (!pago.getReserva().getUsuario().getId().equals(userId)) {
                return ResponseEntity.status(403).body(Map.of("error", "No tienes permisos para cancelar este pago"));
            }

            Pago pagoCancelado = pagoService.cancelarPago(pagoId);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Pago cancelado exitosamente",
                    "pago", pagoCancelado
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al cancelar pago: " + e.getMessage()));
        }
    }
}