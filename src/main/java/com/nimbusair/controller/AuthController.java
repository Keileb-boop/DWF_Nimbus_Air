package com.nimbusair.controller;

import com.nimbusair.entity.User;
import com.nimbusair.entity.Role;
import com.nimbusair.repository.UserRepository;
import com.nimbusair.service.UserService;
import com.nimbusair.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea una nueva cuenta de usuario")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("El usuario con este email ya existe");
        }

        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                Role.USER
        );

        String jwt = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse(jwt, user.getName(), user.getEmail(), user.getRole(), user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica usuario y retorna JWT token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            String jwt = jwtService.generateToken(user);

            AuthResponse response = new AuthResponse(jwt, user.getName(), user.getEmail(), user.getRole(), user.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Test de conexión", description = "Verifica que el backend esté funcionando")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Backend funcionando correctamente");
    }

    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthResponse {
        private String token;
        private String name;
        private String email;
        private Role role;
        private Long userId;

        public AuthResponse(String token, String name, String email, Role role, Long userId) {
            this.token = token;
            this.name = name;
            this.email = email;
            this.role = role;
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public Role getRole() {
            return role;
        }

        public Long getUserId() {
            return userId;
        }
    }

    //PARA CREAR UN ADMINISTRADOR por CMD

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create-real-admin")
    @Operation(summary = "Crear admin con contraseña correcta")
    public ResponseEntity<?> createRealAdmin() {
        try {
            // Cambiar el email aquí para poder crear otro admin
            String adminEmail = "administrador@nimbusair.com";
            String adminPassword = "123456";
            String adminName = "Administrador";

            if (userService.existsByEmail(adminEmail)) {
                User existingAdmin = userRepository.findByEmail(adminEmail)
                        .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

                existingAdmin.setPassword(passwordEncoder.encode(adminPassword));
                userRepository.save(existingAdmin);
                return ResponseEntity.ok("Contraseña de admin actualizada correctamente");
            }


            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setName(adminName);
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);

            User savedAdmin = userRepository.save(admin);
            return ResponseEntity.ok("Admin creado correctamente: " + savedAdmin.getEmail());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}