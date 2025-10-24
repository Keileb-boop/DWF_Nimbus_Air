package com.nimbusair.controller;

import com.nimbusair.entity.Sugerencia;
import com.nimbusair.entity.TipoSugerencia;
import com.nimbusair.entity.Role;
import com.nimbusair.entity.User;
import com.nimbusair.service.SugerenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sugerencias")
@Tag(name = "Sugerencias", description = "API para gestion de sugerencias y feedback")
public class SugerenciaController {

    @Autowired
    private SugerenciaService sugerenciaService;

    @PostMapping
    @Operation(summary = "Crear una nueva sugerencia")
    public ResponseEntity<?> crearSugerencia(@RequestBody SugerenciaRequest request) {
        try {
            Sugerencia sugerencia = new Sugerencia();
            sugerencia.setNombre(request.getNombre());
            sugerencia.setEmail(request.getEmail());
            sugerencia.setMensaje(request.getMensaje());
            sugerencia.setTipo(request.getTipo());

            Sugerencia saved = sugerenciaService.crearSugerencia(sugerencia);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear sugerencia: " + e.getMessage());
        }
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Obtener sugerencias no revisadas")
    public List<Sugerencia> obtenerSugerenciasPendientes() {
        return sugerenciaService.obtenerSugerenciasNoRevisadas();
    }

    @PutMapping("/{id}/revisar")
    @Operation(summary = "Marcar sugerencia como revisada")
    public ResponseEntity<?> marcarComoRevisada(@PathVariable Long id) {
        try {
            Sugerencia sugerencia = sugerenciaService.marcarComoRevisada(id);
            return ResponseEntity.ok(sugerencia);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al marcar sugerencia: " + e.getMessage());
        }
    }

    @GetMapping("/admin/todas")
    @Operation(summary = "Obtener TODAS las sugerencias", description = "Obtiene TODAS las sugerencias del sistema (solo ADMIN)")
    public ResponseEntity<?> obtenerTodasLasSugerenciasAdmin(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            if (user.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: Se requiere rol ADMIN");
            }

            List<Sugerencia> sugerencias = sugerenciaService.obtenerTodasLasSugerenciasAdmin();
            return ResponseEntity.ok(sugerencias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/admin/estadisticas")
    @Operation(summary = "Obtener estadisticas de sugerencias", description = "Obtiene estadisticas de sugerencias (solo ADMIN)")
    public ResponseEntity<?> obtenerEstadisticasAdmin(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            if (user.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: Se requiere rol ADMIN");
            }

            long totalSugerencias = sugerenciaService.obtenerTodasLasSugerencias().size();
            long pendientes = sugerenciaService.contarSugerenciasNoRevisadas();

            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalSugerencias", totalSugerencias);
            estadisticas.put("sugerenciasPendientes", pendientes);
            estadisticas.put("sugerenciasRevisadas", totalSugerencias - pendientes);

            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    public static class SugerenciaRequest {
        private String nombre;
        private String email;
        private String mensaje;
        private TipoSugerencia tipo;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }

        public TipoSugerencia getTipo() { return tipo; }
        public void setTipo(TipoSugerencia tipo) { this.tipo = tipo; }
    }
}