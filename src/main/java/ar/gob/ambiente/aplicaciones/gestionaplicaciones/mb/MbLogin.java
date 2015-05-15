/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.mb;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.CriptPass;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.util.JsfUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
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
    
    private String cuqui;

    /**
     * Creates a new instance of MbLogin
     */
    public MbLogin() {
    }

    public String iniciar(){
        return "login";
    }
    
    public void login(ActionEvent actionEvent){
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        Map<String,Object> attrs;

        try {
            // autenticamos el usuario y pass recibidos
            attrs = autenticar(usuario,pass);  
            logeado = true;
            displayName = attrs.get("displayName").toString();
            JsfUtil.addSuccessMessage("Bienvenid@ " + attrs.get("displayName").toString());
            //msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", attrs.get("displayName").toString());
        } catch (Exception ex) {
            System.out.println("Error Autenticando mediante LDAP, Error causado por : " + ex.toString());
            logeado = false;
            JsfUtil.addErrorMessage("Error de inicio de sesión, Usuario y/o contraseña invalidos");
            //msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de inicio de sesión", "Usuario y/o contraseña invalidos");
        } finally {
            //FacesContext.getCurrentInstance().addMessage(null, msg);
            if(logeado){
                context.addCallbackParam("view", "/gestionAplicaciones");
                context.addCallbackParam("estaLogeado", logeado);
                guardarCookie();
            }
        }
    }
    
    /**
     * Método que autentica al usuario logeado, contra el Active Directory de la SAyDS
     * Devuelve un map con los atributos del usuario
     * @param user
     * @param pass
     * @return 
     */
    private Map<String, Object> autenticar(String user, String pass){
        // parámetros para la conexión al AD
        String domain = "MEDIOAMBIENTE.GOV.AR";
        String ldapHost = "ldap://vmad2008.medioambiente.gov.ar";
        String searchBase = "OU=US,DC=MEDIOAMBIENTE,DC=GOV,DC=AR";
        LdapContext ctxGC = null;

        // parámtetros para el filtrado y obtención de atributos
        String returnedAtts[] ={ "sn", "givenName", "name", "userPrincipalName", "displayName", "memberOf", "homedirectory" };
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + user + "))";
        
        // creación de search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);      
        
        // especificamos el alcance de la búsqueda
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);     
        
        // seteamos el entorno para realizar la búsqueda
        Hashtable<String,String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapHost);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, pass);      
        
        try{
            // obtenemos el contexto si el usuario es autenticado
            ctxGC = new InitialLdapContext(env, null);
            
            // leemos los atributos del usuario
            NamingEnumeration<SearchResult> answer = ctxGC.search(searchBase, searchFilter, searchCtls);
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
                ctxGC.close(); 
                return amap;
            }
        }catch(NamingException nex){
             nex.printStackTrace();
        }
        return null;
    }    
    
    public void logout(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        logeado = false;
        
        // elimino la cookie
        Faces.removeResponseCookie("user", "/");
    }   
    
    public void logoutExterno(){
        if(logeado){
            logout();
        }
    }
    
    private void guardarCookie(){
        String name = "user";
        String value = usuario;
        int expiry = 60;
        
        String valueEnc = CriptPass.encriptar(value);
        
        Faces.addResponseCookie(name, valueEnc, "/", expiry);
        
    }
    
    public void leerCookie(){
        //CookieHelper cuquiGelper = new CookieHelper();
        String name = "user";
        String value;
        String valueEnc = Faces.getRequestCookie(name);
        try {
            value = CriptPass.desencriptar(valueEnc);
        } catch (Exception ex) {
            Logger.getLogger(MbLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCuqui() {
        return cuqui;
    }

    public void setCuqui(String cuqui) {
        this.cuqui = cuqui;
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
}
