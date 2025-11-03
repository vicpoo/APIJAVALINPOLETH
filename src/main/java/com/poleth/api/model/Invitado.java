// Invitado.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Invitados")
public class Invitado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invitado")
    private Integer idInvitado;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "id_cuarto_acceso")
    private Integer idCuartoAcceso;

    @Column(name = "id_imagen_vista")
    private Integer idImagenVista;

    // Relaciones Many-to-One (opcionales, si quieres mapear las entidades completas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuarto_acceso", insertable = false, updatable = false)
    private Cuarto cuartoAcceso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_imagen_vista", insertable = false, updatable = false)
    private ImagenCuartoPublica imagenVista;

    // Constructor por defecto
    public Invitado() {
    }

    // Constructor con parámetros básicos
    public Invitado(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    // Constructor completo
    public Invitado(String nombre, String email, Integer idCuartoAcceso, Integer idImagenVista) {
        this.nombre = nombre;
        this.email = email;
        this.idCuartoAcceso = idCuartoAcceso;
        this.idImagenVista = idImagenVista;
    }

    // Getters y Setters
    public Integer getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(Integer idInvitado) {
        this.idInvitado = idInvitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIdCuartoAcceso() {
        return idCuartoAcceso;
    }

    public void setIdCuartoAcceso(Integer idCuartoAcceso) {
        this.idCuartoAcceso = idCuartoAcceso;
    }

    public Integer getIdImagenVista() {
        return idImagenVista;
    }

    public void setIdImagenVista(Integer idImagenVista) {
        this.idImagenVista = idImagenVista;
    }

    public Cuarto getCuartoAcceso() {
        return cuartoAcceso;
    }

    public void setCuartoAcceso(Cuarto cuartoAcceso) {
        this.cuartoAcceso = cuartoAcceso;
    }

    public ImagenCuartoPublica getImagenVista() {
        return imagenVista;
    }

    public void setImagenVista(ImagenCuartoPublica imagenVista) {
        this.imagenVista = imagenVista;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Invitado{" +
                "idInvitado=" + idInvitado +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", idCuartoAcceso=" + idCuartoAcceso +
                ", idImagenVista=" + idImagenVista +
                '}';
    }
}