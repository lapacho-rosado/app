/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.mb;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.UsuarioFacade;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.JsfUtil;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.Ldap;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbUsuario implements Serializable{

    private Usuario current;
    private List<Usuario> listado;
    private List<Usuario> listadoFilter;   
    private MbLogin login;
    private boolean iniciado;    
    private String usABuscar;
    private String usRetornadoAD;
    
    @EJB
    private UsuarioFacade usFacade;    
    
    /**
     * Creates a new instance of MbUsuario
     */
    public MbUsuario() {
    }

    public String getUsRetornadoAD() {
        return usRetornadoAD;
    }

    public void setUsRetornadoAD(String usRetornadoAD) {
        this.usRetornadoAD = usRetornadoAD;
    }

    public String getUsABuscar() {
        return usABuscar;
    }

    public void setUsABuscar(String usABuscar) {
        this.usABuscar = usABuscar;
    }

    public Usuario getCurrent() {
        return current;
    }

    public void setCurrent(Usuario current) {
        this.current = current;
    }

    public List<Usuario> getListado() {
        if(listado == null){
            listado = getFacade().findAll();
            iniciado = true;
        }
        return listado;
    }

    public void setListado(List<Usuario> listado) {
        this.listado = listado;
    }

    public List<Usuario> getListadoFilter() {
        return listadoFilter;
    }

    public void setListadoFilter(List<Usuario> listadoFilter) {
        this.listadoFilter = listadoFilter;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
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
                    if(!s.equals("mbUsuario") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }     
 
    /**
     * Método para inicializar el listado de los usuarios
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
        // actualizo el modelo
        recreateModel();
        // seteo la entidad actual
        current = new Usuario();
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
            recreateModel();
        }else{
            //No Elimina 
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UsNonDeletable"));
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
        // actualizo el modelo
        recreateModel();
        usABuscar = current.getNombre();
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
    public void validarInsert(FacesContext arg0, UIComponent arg1, Object arg2){
        validarExistente(arg2);
    }    
    
    /**
     * Método para validar que no exista una entidad con este nombre, siempre que dicho nombre no sea el que tenía originalmente
     * @param arg0: vista jsf que llama al validador
     * @param arg1: objeto de la vista que hace el llamado
     * @param arg2: contenido del campo de texto a validar 
     * @throws ValidatorException 
     */
    public void validarUpdate(FacesContext arg0, UIComponent arg1, Object arg2){
        if(!current.getNombre().equals((String)arg2)){
            validarExistente(arg2);
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
        current.setNombre(usABuscar);
        current.setNombreCompleto(usRetornadoAD);
        current.setPersona("NN");
        
        // valido acá porque en el formulario me validaría dos veces
        if(!getFacade().noExiste(current.getNombre())){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UsExistente"));
            return null;
        }
        
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsCreado"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsErrorCreate"));
            return null;
        }
    } 
    
    /**
     * Método que actualiza una Aplicación existente
     * @return 
     */
    public String update() {
        if(!current.getNombre().equals(usABuscar)){
            if(!getFacade().noExiste(current.getNombre())){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("UsExistente"));
                return null;
            }
        }        
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsActualizado"));
            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsErrorUpdate"));
            return null;
        }
    }        
    
    /**
     * Restea el listado después de eliminar un registro
     */
    private void recreateModel() {
        listado.clear();
        listado = null;
        usABuscar = "";
        usRetornadoAD = "";
    }        
    
    /**
     * @return La entidad gestionada
     */
 
    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
        }
        return current;
    }         
    
    /**
     * Método para obtener el usuario del Acitve Directory
     * @throws javax.naming.NamingException
     */
    public void getUsuarioAD() throws NamingException{
        // obtengo la clase para acceder al Active Directory
        Ldap ldap = new Ldap();
        
        // parámetros para la conexión al AD
        String searchBase = "OU=Usuarios de Dominio,DC=MEDIOAMBIENTE,DC=GOV,DC=AR‏";
        LdapContext ctxGC = null;

        // parámtetros para el filtrado y obtención de atributos
        String returnedAtts[] ={ "sn", "givenName", "name", "userPrincipalName", "displayName", "homedirectory" };
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + usABuscar + "))";
        
        // creación de search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);      
        
        // especificamos el alcance de la búsqueda
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);     
        
        // obtenemos el contexto si el usuario es autenticado
        ctxGC = ldap.getContextSerach();
        // leemos los atributos del usuario
        NamingEnumeration<SearchResult> answer = ctxGC.search(searchBase, searchFilter, searchCtls);       
        
        if(answer != null){
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                Map<String, Object> amap = null;
                if (attrs != null) {
                    amap = new HashMap<>();
                    NamingEnumeration<?> ne = attrs.getAll();
                    while (ne.hasMore()) {
                        Attribute attr = (Attribute) ne.next();
                        if (attr.size() == 1) {
                            amap.put(attr.getID(), attr.get());
                        } else {
                            HashSet<String> s = new HashSet<>();
                            NamingEnumeration n =  attr.getAll();
                            while (n.hasMoreElements()) {
                                s.add((String)n.nextElement());
                            }
                            amap.put(attr.getID(), s);
                        }
                    }
                    ne.close();
                }
                // seteamos el nombre completo del usuario
                usRetornadoAD = amap.get("displayName").toString();
                
                // cerramos el contexto
                ldap.closeContextSearch();
            }          
        }
        // si no encontré al usuario envío un mensaje
        if(usRetornadoAD.equals("") || usRetornadoAD.equals("")){
            usRetornadoAD = "<Usuario inexistente>";
            
            // cerramos la conexión
            ldap.closeContextSearch();
        }        
    }
    
    
    /*************************
    ** Métodos de selección **
    **************************/

    /**
     * @param id equivalente al id de la entidad persistida
     * @return la entidad correspondiente
     */
    public Usuario getUsuario(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    
    /*********************
    ** Métodos privados **
    **********************/
    /**
     * @return el Facade
     */
    private UsuarioFacade getFacade() {
        return usFacade;
    }        
    
    private void validarExistente(Object arg2) throws ValidatorException{
        if(!getFacade().noExiste((String)arg2)){
            throw new ValidatorException(new FacesMessage(ResourceBundle.getBundle("/Bundle").getString("UsExistente")));
        }
    }        
    
    /**
     * Opera el borrado de la entidad
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsEliminado"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("UsErrorDelete"));
        }
    }      
    
    /********************************************************************
    ** Converter. Se debe actualizar la entidad y el facade respectivo **
    *********************************************************************/
    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbUsuario controller = (MbUsuario) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbUsuario");
            return controller.getUsuario(getKey(value));
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }
    }               
}
