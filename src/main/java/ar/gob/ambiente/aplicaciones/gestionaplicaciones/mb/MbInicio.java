/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.mb;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.UsuarioFacade;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rincostante
 */
public class MbInicio implements Serializable {
    
    private String user;
    private Usuario usuario;
    private Aplicacion current;
    private List<Aplicacion> listApp;
    private MbLogin login;
    private boolean iniciado;
 
    @EJB
    private UsuarioFacade usFacade;    

    /**
     * Creates a new instance of MbIniciio
     */
    public MbInicio() {
        
    }

    public Aplicacion getCurrent() {
        return current;
    }

    /**
     * Método para inicializar el usuario
     */
    @PostConstruct
    public void init(){
        // obtengo el usuario registrado en el mbLogin y seteo 'iniciado'
        if(!iniciado){
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            login = (MbLogin)ctx.getSessionMap().get("mbLogin");
            user = login.getUsuario();
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
                    if(!s.equals("mbInicio") && !s.equals("mbLogin")){
                        session.removeAttribute(s);
                    }
                }
            }
        }
    }     

    public List<Aplicacion> getListApp() {
        if (listApp == null) {
            iniciado = login.isLogeado();
            usuario = usFacade.getXNombre(user);
            if(usuario != null){
                listApp = usFacade.getApliaciones(usuario);
            }
        }     
        return listApp;
    }

    public void setListApp(List<Aplicacion> listApp) {
        this.listApp = listApp;
    }
    
    public void setCurrent(Aplicacion current) {
        this.current = current;
    }
}
