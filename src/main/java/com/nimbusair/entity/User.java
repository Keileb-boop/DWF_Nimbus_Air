package com.nimbusair.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Schema(description = "Entidad que representa un usuario del sistema")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email único del usuario", example = "usuario@ejemplo.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Contraseña encriptada del usuario")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Rol del usuario en el sistema", example = "USER")
    private Role role = Role.USER;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Indica si el usuario está activo", example = "true")
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Schema(description = "Fecha y hora de creación del usuario")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Fecha y hora de última actualización")
    private LocalDateTime updatedAt;

    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(String email, String password, String name, Role role) {
        this();
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}