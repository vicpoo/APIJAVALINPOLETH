// ImagenCuartoPublica.java
package com.poleth.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_cuartos_publicas")
public class ImagenCuartoPublica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Integer idImagen;

    @Column(name = "id_cuarto", nullable = false)
    private Integer idCuarto;

    @Column(name = "url_imagen", nullable = false, length = 255)
    private String urlImagen;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "es_publica", nullable = false)
    private Boolean esPublica = true;

    // Constructor por defecto
    public ImagenCuartoPublica() {
    }

    // Constructor con parámetros
    public ImagenCuartoPublica(Integer idCuarto, String urlImagen, String descripcion, Boolean esPublica) {
        this.idCuarto = idCuarto;
        this.urlImagen = urlImagen;
        this.descripcion = descripcion;
        this.esPublica = esPublica;
    }

    // Getters y Setters
    public Integer getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Integer idImagen) {
        this.idImagen = idImagen;
    }

    public Integer getIdCuarto() {
        return idCuarto;
    }

    public void setIdCuarto(Integer idCuarto) {
        this.idCuarto = idCuarto;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEsPublica() {
        return esPublica;
    }

    public void setEsPublica(Boolean esPublica) {
        this.esPublica = esPublica;
    }

    // Método utilitario para verificar si es pública
    public boolean isPublica() {
        return esPublica != null && esPublica;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "ImagenCuartoPublica{" +
                "idImagen=" + idImagen +
                ", idCuarto=" + idCuarto +
                ", urlImagen='" + urlImagen + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", esPublica=" + esPublica +
                '}';
    }
}