/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rincostante
 */
@Entity
public class Aplicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (nullable=false, length=100, unique=true)
    @NotNull(message = "El campo Nombre no puede quedar nulo")
    @Size(message = "El campo Nombre debe tener entre 1 y 100 caracteres", min = 1, max = 100)    
    private String nombre;
    
    @Column (nullable=true, length=250)
    @Size(message = "El campo Descripción tiene un máximo de 250 caracteres", max = 250)      
    private String descripcion;
    
    @Column (nullable=true, length=100)
    @Size(message = "El campo Area Referente tiene un máximo de 100 caracteres", max = 100)      
    private String areaReferente;
    
    @Column (nullable=false, length=100, unique=true)
    @NotNull(message = "El campo Url no puede quedar nulo")
    @Size(message = "El campo Url debe tener entre 1 y 100 caracteres", min = 1, max = 100)       
    private String url;
    
    @ManyToMany
    @JoinTable(
            name = "aplicacionesXUsuarios",
            joinColumns = @JoinColumn(name = "aplicacion_fk"),
            inverseJoinColumns = @JoinColumn(name = "usuario_fk")
    )
    private List<Usuario> usuarios;    
    
    public Aplicacion(){
        usuarios = new ArrayList();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAreaReferente() {
        return areaReferente;
    }

    public void setAreaReferente(String areaReferente) {
        this.areaReferente = areaReferente;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aplicacion)) {
            return false;
        }
        Aplicacion other = (Aplicacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion[ id=" + id + " ]";
    }
    
}
