/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.mb;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.UsuarioFacade;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.CriptPass;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.Ldap;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpSession;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;

/**
 *
 * @author rincostante
 */
public class MbLogin implements Serializable{
    
    private boolean logeado;
    private String usuario;
    private String pass;
    private String displayName;
    private Usuario usLogueado;
    
    @EJB
    private UsuarioFacade usuarioFacade;    

    /**
     * Creates a new instance of MbLogin
     */
    public MbLogin() {
    }

    public String iniciar(){
        return "login";
    }
    
    public void login(ActionEvent actionEvent) throws NamingException{
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String,Object> attrs;

        attrs = autenticar(usuario,pass);  
        if(attrs != null){
            logeado = true;
            displayName = attrs.get("displayName").toString();
            // guardo el usuario logueado
            usLogueado = usuarioFacade.getXNombre(usuario);
            context.addCallbackParam("view", "/gestionAplicaciones");
            context.addCallbackParam("estaLogeado", logeado);
            guardarCookie();    
        }else{
            logeado = false;
        }
    }
    
    /**
     * Método que autentica al usuario logeado, contra el Active Directory de la SAyDS
     * Devuelve un map con los atributos del usuario
     * @param user
     * @param pass
     * @return 
     */
    private Map<String, Object> autenticar(String user, String pass) throws NamingException{
        // obtengo la clase para acceder al Active Directory
        Ldap ldap = new Ldap();
        LdapContext ctxGC = null;

        // parámtetros para el filtrado y obtención de atributos
        String returnedAtts[] ={ "sn", "givenName", "name", "userPrincipalName", "displayName", "homedirectory" };
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";
        
        // creación de search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);      
        
        // especificamos el alcance de la búsqueda
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);     
        
        // obtenemos el contexto si el usuario es autenticado
        ctxGC = ldap.getContextAuth(user, pass);
        // leemos los atributos del usuario
        String searchBase = "OU=Usuarios de Dominio,DC=MEDIOAMBIENTE,DC=GOV,DC=AR‏";
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
                // cerramos el contexto
                ldap.closeContextAuth();
                return amap;
            }          
        }
        return null;
    }    
    
    public void logout(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        logeado = false;
        
        // elimino la cookie
        Faces.removeResponseCookie(ResourceBundle.getBundle("/Bundle").getString("nameCookieUser"), "/");
    }   
    
    public void logoutExterno(){
        if(logeado){
            logout();
        }
    }
    
    private void guardarCookie(){
        String value = usuario;
        int expiry = Integer.parseInt(ResourceBundle.getBundle("/Bundle").getString("expira"));
        
        String valueEnc = CriptPass.encriptar(value);
        
        Faces.addResponseCookie(ResourceBundle.getBundle("/Bundle").getString("nameCookieUser"), valueEnc, "/", expiry);
        
    }
    
    
    /**
     * Método para manejar los errores y enviar mensajes al usuario
     * @param exception 
     */
    public void handleException (Throwable exception){
        String message = "";
        if (exception instanceof ApplicationError){
            message = "Ha ocurrido un error auntenticando el usuario : " + exception.getMessage();
        }else{
            message = "¡Ha ocurrido un error inesperado!";
        }
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null,  facesMessage);
    }

    public Usuario getUsLogueado() {
        return usLogueado;
    }

    public void setUsLogueado(Usuario usLogueado) {
        this.usLogueado = usLogueado;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }
    
    // clase para manejar los mensajes de error
    class ApplicationError extends RuntimeException{

        private ApplicationError(String message) {
            super(message);
        }
    };          
}
