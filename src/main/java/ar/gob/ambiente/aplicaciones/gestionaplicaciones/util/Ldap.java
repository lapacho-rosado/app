
package ar.gob.ambiente.aplicaciones.gestionaplicaciones.util;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * Esta clase encapsula la funcionalidad para acceder al Active Directory del dominio
 * @author rincostante
 */
public class Ldap {
    public static String domain = "MEDIOAMBIENTE.GOV.AR";
    public static String ldapHost = "ldap://vmad2008.medioambiente.gov.ar";
    public static String user = "pruebapp";
    public static String pass = "pruebapp2015*";
    
    private LdapContext ctxGCAuth = null;
    private LdapContext ctxGCSearch = null;
    private Hashtable<String,String> env = new Hashtable<>();
    
    /**
     * Método que retorna un contexto auntenticado
     * @param usuario: el que recibe del formulario de login
     * @param contrasenia: la que recibe del formulario de login
     * @return 
     * @throws javax.naming.NamingException
     */
    public LdapContext getContextAuth(String usuario, String contrasenia) throws NamingException{
        if(ctxGCAuth == null){
            // seteo el ambiente para la conexión
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapHost);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, usuario + "@" + domain);
            env.put(Context.SECURITY_CREDENTIALS, contrasenia);
            
            // obtenemos el contexto si el usuario es autenticado
            ctxGCAuth = new InitialLdapContext(env, null);            
            return ctxGCAuth;
        }else{
            return ctxGCAuth;
        }
    }
    
    
    /**
     * Método que retorna el contexto para realizar búsquedas o autenticaciones
     * @return 
     * @throws javax.naming.NamingException 
     */
    public LdapContext getContextSerach() throws NamingException{
        
        if(ctxGCSearch == null){
            // seteo el ambiente para la conexión
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapHost);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
            env.put(Context.SECURITY_CREDENTIALS, pass);
            
            // obtenemos el contexto si el usuario es autenticado
            ctxGCSearch = new InitialLdapContext(env, null);            
            return ctxGCSearch;
        }else{
            return ctxGCSearch;
        }
    }
    
    public void closeContextSearch() throws NamingException{
        if(ctxGCSearch == null){
            ctxGCSearch.close();
        }
    }
    
    public void closeContextAuth() throws NamingException{
        if(ctxGCAuth != null){
            ctxGCAuth.close();
        }
    }    
}
