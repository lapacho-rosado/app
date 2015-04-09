/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.mb;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.AplicacionFacade;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.UsuarioFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbAplicaciones implements Serializable{
    
    private String user;
    private Usuario usuario;
    private Aplicacion current;
    private List<Aplicacion> listado; 
    private List<Aplicacion> listadoFilter;      
    
    @EJB
    private AplicacionFacade appFacade;
    @EJB
    private UsuarioFacade usFacade;

    /**
     * Creates a new instance of MbAplicaciones
     */
    public MbAplicaciones() {
    }

    public Aplicacion getCurrent() {
        return current;
    }

    public void setCurrent(Aplicacion current) {
        this.current = current;
    }

    public List<Aplicacion> getListado() {
        if (listado == null) {
            usuario = usFacade.getXNombre(user);
            if(usuario != null){
                listado = usuario.getAplicaciones();
            }
        }
        return listado;
    }

    public void setListado(List<Aplicacion> listado) {
        this.listado = listado;
    }

    public List<Aplicacion> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<Aplicacion> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/
    
    /**
     * Método para inicializar el usuario
     */
    @PostConstruct
    public void init(){
        // lo puede levantar del xhml con #{request.remoteUser}
        user = FacesContext.getCurrentInstance()
            .getExternalContext().getRemoteUser();
    }
    
    /**
     * Método para inicializar el listado de las aplicaciones
     * @return acción para el listado de entidades
     */
    public String prepareList() {       
        return "list";
    } 
    
    /**
     * Método para preparar la creación y redireccionamiento a la página correspondiente
     * @return 
     */
    public String prepareCreate() {
        return "new";
    }
    
    
    /**
     * Métodos de operaciones
     * @return 
     */
    public void cerrarSesion(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
    }
    
    
    
    
    
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Aplicacion getAplicacion(java.lang.Long id) {
        return getFacade().find(id);
    }      
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private AplicacionFacade getFacade() {
        return appFacade;
    }       
    
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Aplicacion.class)
    public static class AplicacionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbAplicaciones controller = (MbAplicaciones) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbAplicaciones");
            return controller.getAplicacion(getKey(value));
        }

        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Aplicacion) {
                Aplicacion o = (Aplicacion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Aplicacion.class.getName());
            }
        }
    }            
}
