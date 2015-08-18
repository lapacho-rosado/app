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
    private List<Aplicacion> listado;
    private List<Aplicacion> listadoFilter;   
    private MbLogin login;
    private boolean iniciado;
    private List<String> imgApp;
    
    @EJB
    private AplicacionFacade appFacade;    
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

    public List<String> getImgApp() {
        return imgApp;
    }

    public void setImgApp(List<String> imgApp) {
        this.imgApp = imgApp;
    }
    
    public void setCurrent(Aplicacion current) {
        this.current = current;
    }

    public List<Aplicacion> getListado() {
        if (listado == null) {
            iniciado = login.isLogeado();
            usuario = usFacade.getXNombre(user);
            if(usuario != null){
                listado = usFacade.getApliaciones(usuario);
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
    
    /**
     * @return acción para el listado de entidades a mostrar en el list
     */
    public String prepareList() {
        return "/gestionTerritorial";
    }    
        
}
