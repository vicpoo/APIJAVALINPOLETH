// Login.java
package com.poleth.api.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "login")
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    private Integer idLogin;

    @Column(name = "usuario", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    private Propietario propietario;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "id_invitado")
    private Invitado invitado;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    // Constructor por defecto
    public Login() {
    }

    // Constructor con parámetros básicos
    public Login(String usuario, String contrasena, Rol rol) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Constructor completo
    public Login(String usuario, String contrasena, Rol rol, Propietario propietario, Inquilino inquilino, Invitado invitado) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.propietario = propietario;
        this.inquilino = inquilino;
        this.invitado = invitado;
    }

    // Getters y Setters
    public Integer getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(Integer idLogin) {
        this.idLogin = idLogin;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public Invitado getInvitado() {
        return invitado;
    }

    public void setInvitado(Invitado invitado) {
        this.invitado = invitado;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Métodos utilitarios
    public boolean isPropietario() {
        return propietario != null;
    }

    public boolean isInquilino() {
        return inquilino != null;
    }

    public boolean isInvitado() {
        return invitado != null;
    }

    public String getTipoUsuario() {
        if (isPropietario()) return "PROPIETARIO";
        if (isInquilino()) return "INQUILINO";
        if (isInvitado()) return "INVITADO";
        return "SISTEMA";
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Login{" +
                "idLogin=" + idLogin +
                ", usuario='" + usuario + '\'' +
                ", rol=" + (rol != null ? rol.getNombreRol() : "null") +
                ", propietario=" + (propietario != null ? propietario.getIdPropietario() : "null") +
                ", inquilino=" + (inquilino != null ? inquilino.getIdInquilino() : "null") +
                ", invitado=" + (invitado != null ? "presente" : "null") +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}