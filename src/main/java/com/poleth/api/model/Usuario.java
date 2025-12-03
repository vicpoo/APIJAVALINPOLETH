//Usuario.java
package com.poleth.api.model;

import com.poleth.api.util.PasswordUtil;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "estado_usuario", length = 20)
    private String estadoUsuario;

    // Constructor por defecto
    public Usuario() {
        this.estadoUsuario = "activo";
        this.createdAt = LocalDateTime.now();
    }

    // Constructor con parámetros
    public Usuario(String username, String password, String email, String telefono, Rol rol) {
        this();
        this.username = username;
        this.setPassword(password); // Usar setter para hashear la contraseña
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Hashear la contraseña solo si no está ya hasheada
        if (password != null && !password.startsWith("$2")) {
            this.password = PasswordUtil.hashPassword(password);
        } else {
            this.password = password;
        }
    }

    // Método para verificar contraseña
    public boolean verificarPassword(String plainPassword) {
        return PasswordUtil.checkPassword(plainPassword, this.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rol=" + (rol != null ? rol.getTitulo() : "null") +
                ", createdAt=" + createdAt +
                ", estadoUsuario='" + estadoUsuario + '\'' +
                '}';
    }
}