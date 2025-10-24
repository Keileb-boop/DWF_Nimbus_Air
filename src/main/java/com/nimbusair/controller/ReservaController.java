package com.nimbusair.controller;

import com.nimbusair.entity.*;
import com.nimbusair.repository.UserRepository;
import com.nimbusair.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@Tag(name = "Reservas", description = "Gestion de reservas de vuelos")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Crear reserva", description = "Crea una nueva reserva de vuelo para el usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de reserva invalidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> crearReserva(
            @Parameter(description = "Datos de la reserva", required = true)
            @Valid @RequestBody Reserva reserva,
            BindingResult result,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User usuario = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

            if (result.hasErrors()) {
                Map<String, String> errores = new HashMap<>();
                result.getFieldErrors().forEach(err ->
                        errores.put(err.getField(), err.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errores);
            }

            if (reserva.getFechaVuelo().isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "La fecha del vuelo no puede ser anterior a hoy."
                ));
            }

            if (reserva.getFechaNacimiento().isAfter(LocalDate.now())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "La fecha de nacimiento no puede ser futura."
                ));
            }

            if (reserva.getHoraSalida().isBefore(LocalTime.of(5, 0)) ||
                    reserva.getHoraSalida().isAfter(LocalTime.of(23, 0))) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "La hora de salida debe estar entre las 05:00 y 23:00."
                ));
            }

            reserva.setUsuario(usuario);

            if (reserva.getEstado() == null) {
                reserva.setEstado("CONFIRMADA");
            }

            Reserva nuevaReserva = reservaService.guardarReserva(reserva);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Reserva creada con exito para " + usuario.getName());
            respuesta.put("reserva", nuevaReserva);
            respuesta.put("usuario", Map.of(
                    "id", usuario.getId(),
                    "nombre", usuario.getName(),
                    "email", usuario.getEmail()
            ));
            respuesta.put("codigoReserva", "NA-" + nuevaReserva.getId() + "-" + System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al crear reserva: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Obtener reservas del usuario", description = "Obtiene todas las reservas del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> obtenerReservasUsuario(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User usuario = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

            List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(usuario);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("usuario", Map.of(
                    "id", usuario.getId(),
                    "nombre", usuario.getName(),
                    "email", usuario.getEmail(),
                    "totalReservas", reservas.size()
            ));
            respuesta.put("reservas", reservas);

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reservas: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Obtiene una reserva especifica del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<?> obtenerReservaPorId(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User usuario = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

            List<Reserva> reservasUsuario = reservaService.obtenerReservasPorUsuario(usuario);

            Reserva reserva = reservasUsuario.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada o no tienes permisos para verla"));

            return ResponseEntity.ok(reserva);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reserva: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva", description = "Actualiza una reserva existente del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de actualizacion"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<?> actualizarReserva(
            @Parameter(description = "ID de la reserva a actualizar", required = true)
            @PathVariable Long id,

            @Parameter(description = "Datos actualizados de la reserva", required = true)
            @Valid @RequestBody ReservaUpdateRequest updateRequest,

            Authentication authentication) {

        try {
            String userEmail = authentication.getName();
            User usuario = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

            List<Reserva> reservasUsuario = reservaService.obtenerReservasPorUsuario(usuario);

            Reserva reservaExistente = reservasUsuario.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada o no tienes permisos para modificarla"));

            Reserva reservaActualizada = reservaService.actualizarReserva(id, updateRequest);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Reserva actualizada exitosamente");
            respuesta.put("reserva", reservaActualizada);

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar reserva: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva", description = "Cancela una reserva del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<?> cancelarReserva(
            @Parameter(description = "ID de la reserva a cancelar", required = true)
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User usuario = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

            List<Reserva> reservasUsuario = reservaService.obtenerReservasPorUsuario(usuario);

            Reserva reserva = reservasUsuario.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada o no tienes permisos para cancelarla"));

            reserva.setEstado("CANCELADA");
            reservaService.guardarReserva(reserva);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Reserva cancelada exitosamente",
                    "reservaId", reserva.getId(),
                    "estado", reserva.getEstado()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al cancelar reserva: " + e.getMessage()));
        }
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener reservas por estado", description = "Obtiene reservas del usuario filtradas por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> obtenerReservasPorEstado(
            @Parameter(description = "Estado de las reservas", required = true,
                    examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(value = "CONFIRMADA"),
                            @io.swagger.v3.oas.annotations.media.ExampleObject(value = "CANCELADA"),
                            @io.swagger.v3.oas.annotations.media.ExampleObject(value = "PENDIENTE")
                    })
            @PathVariable String estado,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User usuario = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

            List<Reserva> reservasUsuario = reservaService.obtenerReservasPorUsuario(usuario);

            List<Reserva> reservasFiltradas = reservasUsuario.stream()
                    .filter(r -> r.getEstado().equalsIgnoreCase(estado))
                    .toList();

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("estado", estado.toUpperCase());
            respuesta.put("total", reservasFiltradas.size());
            respuesta.put("reservas", reservasFiltradas);

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reservas por estado: " + e.getMessage()));
        }
    }

    @GetMapping("/admin/todas")
    public ResponseEntity<?> obtenerTodasLasReservasAdmin(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            if (user.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: Se requiere rol ADMIN");
            }

            List<Reserva> reservas = reservaService.obtenerTodasLasReservas();

            // Convertir a DTOs
            List<ReservaDTO> reservasDTO = reservas.stream()
                    .map(ReservaDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reservasDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}