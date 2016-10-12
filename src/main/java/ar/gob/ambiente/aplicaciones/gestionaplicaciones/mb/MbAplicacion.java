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
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author rincostante
 */
public class MbAplicacion implements Serializable{
    
    private String user;
    private Aplicacion current;
    private List<Aplicacion> listado;
    private List<Aplicacion> listadoFilter;   
    private MbLogin login;
    private boolean iniciado;
    
    // paquete para los usuarios
    private List<Usuario> listUsDisp;
    private List<Usuario> listUsuarios;
    private List<Usuario> listUsuariosFilter;    
    
    @EJB
    private AplicacionFacade appFacade;
    @EJB
    private UsuarioFacade usFacade;

    /**
     * Creates a new instance of MbAplicaciones
     */
    public MbAplicacion() {
    }

    public List<Aplicacion> getListado() {
        if(listado == null){
            listado = getFacade().findAll();
            iniciado = true;
        }
        return listado;
    }

    public List<Usuario> getListUsDisp() {
        return listUsDisp;
    }

    public void setListUsDisp(List<Usuario> listUsDisp) {
        this.listUsDisp = listUsDisp;
    }

    public List<Usuario> getListUsuarios() {
        return listUsuarios;
    }

    public void setListUsuarios(List<Usuario> listUsuarios) {
        this.listUsuarios = listUsuarios;
    }

    public List<Usuario> getListUsuariosFilter() {
        return listUsuariosFilter;
    }

    public void setListUsuariosFilter(List<Usuario> listUsuariosFilter) {
        this.listUsuariosFilter = listUsuariosFilter;
    }

    public void setListado(List<Aplicacion> listado) {
        this.listado = listado;
    }

    public Aplicacion getCurrent() {
        return current;
    }

    public void setCurrent(Aplicacion current) {
        this.current = current;
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
        // obtengo el usuario registrado en el mbLogin y seteo 'iniciado'
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        login = (MbLogin)ctx.getSessionMap().get("mbLogin");
        if(login != null){
            user = login.getUsuario();
            iniciado = login.isLogeado();
        }
    }
    
    /**
     * Método que borra de la memoria los MB innecesarios al cargar el listado 
     */
    public void iniciar(){
        if(iniciado){
            String s;
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(true);
            Enumeration enume = session.getAttributeNames();
            while(enume.hasMoreElements()){
                s = (String)enume.nextElement();
                if(s.substring(0, 2).equals("mb")){
                    if(!s.equals("mbAplicaciones") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }      
    
    /**
     * Método para inicializar el listado de las aplicaciones
     * @return acción para el listado de entidades
     */
    public String prepareList() {     
        if(listUsDisp != null){
            listUsDisp.clear();
        } 
        recreateModel();
        return "list";
    } 
    
    /**
     * Método para preparar la creación y redireccionamiento a la página correspondiente
     * @return 
     */
    public String prepareCreate() {
        // cargo la tabla de Usuarios
        listUsuarios = usFacade.findAll();
        // seteo la entidad actual
        current = new Aplicacion();
        return "new";
    }
    
    /**
     * Método que verifica que la Aplicación que se quiere eliminar no esté siento utilizado por otra entidad
     * @return 
     */
    public String prepareDestroy(){
        boolean libre = getFacade().noTieneDependencias(current.getId());

        if (libre){
            // Elimina
            performDestroy();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("AppNonDeletable"));
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
        listUsDisp = cargarUsuariosDisponibles();
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
     * Método para validar que no exista ya una entidad con esta url al momento de crearla
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     */
    public void validarInsertUrl(FacesContext arg0, UIComponent arg1, Object arg2){
        validarUrlExistente(arg2);
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
    
    /**
     * Método para validar que no exista una entidad con esta url, siempre que dicha url no sea la que tenía originalmente
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     * @throws ValidatorException 
     */
    public void validarUpdateUrl(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getUrl().equals((String)arg2)){
            validarUrlExistente(arg2);
        }
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppCreada"));
            listUsuarios.clear();
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AppErrorCreate"));
            return null;
        }
    } 
    
    /**
     * Método que actualiza una Aplicación existente
     * @return 
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppActualizada"));
            listUsDisp.clear();
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AppErrorUpdate"));
            return null;
        }
    }        
    
    /**
     * Restea el listado después de eliminar un registro
     */
    private void recreateModel() {
        listado.clear();
        listado = null;
    }        
    
    /**
     * @return La entidad gestionada
     */
 
    public Aplicacion getSelected() {
        if (current == null) {
            current = new Aplicacion();
        }
        return current;
    }     
    
    public void asignarUsuario(Usuario us){
        current.getUsuarios().add(us);
        listUsDisp.remove(us);
    }
    
    public void quitarUsuario(Usuario us){
        current.getUsuarios().remove(us);
        listUsDisp.add(us);
    }    
    
    /**
     * Método para gestionar la imagen asociada a las aplicaciones
     */
    public void verAgregarImagen(){
        Map<String,Object> options = new HashMap<>();
        options.put("contentWidth", 800);
        options.put("contentHeight", 250);
        RequestContext.getCurrentInstance().openDialog("dlgCargarImagen", options, null);
    }     
    
    /**
     * Método para subir las imágenes de las publicidades
     * @param event
     */
    public void subirImagenes(FileUploadEvent event){
        if(current.getRutaImagen() == null || current.getRutaImagen().equals("")){
            try{
                UploadedFile fileImg = event.getFile();
                String destino;

                destino = ResourceBundle.getBundle("/Bundle").getString("RutaArchivos");

                if(destino == null){
                    JsfUtil.addErrorMessage("No se pudo obtener el destino de la imagen.");
                }else{
                    // si logré subir el archivo, guardo la ruta
                    if(JsfUtil.copyFile(fileImg.getFileName(), fileImg.getInputstream(), destino)){
                        JsfUtil.addSuccessMessage("El archivo " + fileImg.getFileName() + " se ha subido al servidor. "
                                + "Por favor, actualice el campo de texto para ver la ruta completa");
                        current.setRutaImagen(fileImg.getFileName()); 
                    }
                }
            }catch(IOException e){
                JsfUtil.addErrorMessage("Hubo un error subiendo la imagen" + e.getLocalizedMessage());
            }
        }else{
            JsfUtil.addErrorMessage("Ya hay una imagen para esta Publicidad, por vavor elimine la existente y vuelva a intentarlo.");
        }
    }
    
    /**
     * Método para eliminar la imagen asociada a las aplicaciones
     */
    public void deleteImagen(){
        String destino;
        destino = ResourceBundle.getBundle("/Bundle").getString("RutaArchivos");        
        if(JsfUtil.deleteFile(destino + "/" + current.getRutaImagen())){
            current.setRutaImagen("");
            JsfUtil.addSuccessMessage("La imagen ha sido eliminada");
        }else{
            JsfUtil.addErrorMessage("No se pudo eliminar la imagen");
        }
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
    
    private void validarNombreExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExisteNombre((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("AppExistente")));
        }
    }    
    
    private void validarUrlExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExisteUrl((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("AppExistente")));
        }
    }      
    
    /**
     * 
     */
    private List<Usuario> cargarUsuariosDisponibles(){
        List<Usuario> usuarios = usFacade.findAll();
        List<Usuario> usSelect = new ArrayList();
        Iterator itUs = usuarios.listIterator();
        while(itUs.hasNext()){
            Usuario us = (Usuario)itUs.next();
            if(!current.getUsuarios().contains(us)){
                usSelect.add(us);
            }
        }
        return usSelect;
    }    
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AppEliminada"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("AppErrorDelete"));
        }
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
            MbAplicacion controller = (MbAplicacion) facesContext.getApplication().getELResolver().
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
