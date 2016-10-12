

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.mb;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Rol;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.RolFacade;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 * Bean para la gestión de los roles de usuarios para la gestión de aplicaciones
 * @author rincostante
 */
public class MbRol implements Serializable{

    private Rol current;
    private List<Rol> listado;
    private List<Rol> listFilter;
    private List<Usuario> listUsFilter;    
    
    @EJB
    private RolFacade rolFacade;
    
    public MbRol() {
    }
   
    
    /**********************
     * Métodos de acceso **
     **********************/
    public Rol getCurrent() {
        return current;
    }

    public void setCurrent(Rol current) {
        this.current = current;
    }

    public List<Rol> getListado() {
        if(listado == null){
            listado = getFacade().findAll();
        }        
        return listado;
    }

    public void setListado(List<Rol> listado) {
        this.listado = listado;
    }

    public List<Rol> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Rol> listFilter) {
        this.listFilter = listFilter;
    }

    public List<Usuario> getListUsFilter() {
        return listUsFilter;
    }

    public void setListUsFilter(List<Usuario> listUsFilter) {
        this.listUsFilter = listUsFilter;
    }

 
    /*********************************
     * Métodos de preparado y ruteo **
     *********************************/
    
    /**
     * Método para inicializar el listado de los Roles
     * @return acción para el listado de entidades
     */
    public String prepareList() {     
        recreateModel();
        return "list";
    } 
    
    /**
     * Método para preparar la creación y redireccionamiento a la página correspondiente
     * @return 
     */
    public String prepareCreate() {
        current = new Rol();
        return "new";
    }
    
    /**
     * Método que verifica que el Rol que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        boolean libre = getFacade().noTieneDependencias(current);

        if (libre){
            // Elimina
            performDestroy();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("RolNonDeletable"));
        }
        return "view";
    }    
    
    /**
     * @return acción para el detalle de la entidad
     */
    public String prepareView() {
        return "view";
    }
    
    /**
     * @return acción para la edición de la entidad
     */
    public String prepareEdit() {
        return "edit";
    }           
    
    
    /****************************
     * Métodos de validación
     ****************************/    
    
    /**
     * Método para validar que no exista ya una entidad con este nombre al momento de crearla
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarInsertNombre(FacesContext arg0, UIComponent arg1, Object arg2){
        validarNombreExistente(arg2);
    }
    
    /**
     * Método para validar que no exista una entidad con este nombre, siempre que dicho nombre no sea el que tenía originalmente
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     * @throws ValidatorException 
     */
    public void validarUpdateNombre(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getNombre().equals((String)arg2)){
            validarNombreExistente(arg2);
        }
    }      
    
    
    
    
    /*******************************
     ** Métodos de inicialización **
     *******************************/    
    
    /**
     * Método para inicializar el usuario y la limpieza de beans no usados
     */
    @PostConstruct
    public void init(){
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ctx.getSession(true);
        session.removeAttribute("mbAplicacion");
        session.removeAttribute("mbUsuario");
        session.removeAttribute("mbInicio");   
    }
    
 
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Rol getRol(java.lang.Long id) {
        return rolFacade.find(id);
    }    
    
    
   /**************************
    ** Métodos de operación **
    **************************/
    
    /**
     * Método que inserta la entidad en la bd
     * @return 
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RolCreado"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("RolErrorCreate"));
            return null;
        }        
    } 
    
    /**
     * Método que actualiza un Rol existente
     * @return 
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RolActualizado"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("RolErrorUpdate"));
            return null;
        }
    }  

    
    /**
     * @return La entidad gestionada
     */
    public Rol getSelected() {
        if (current == null) {
            current = new Rol();
        }
        return current;
    }    
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private RolFacade getFacade() {
        return rolFacade;
    }

    private void performDestroy() {
        try{
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RolEliminado"));
        }catch(Exception ex){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("RolErrorDelete") + ". " + ex.getMessage());
        }
    }    

    private void recreateModel() {
        listado.clear();
        listado = null;
        if(current != null){
            current = null;
        }
    }

    private void validarNombreExistente(Object arg2) throws ValidatorException {
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("RolExistente")));
        }
    }
    

    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Rol.class)
    public static class RolControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbRol controller = (MbRol) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbRol");
            return controller.getRol(getKey(value));
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
            if (object instanceof Rol) {
                Rol o = (Rol) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Rol.class.getName());
            }
        }
    }            
}
