/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.servicios;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.AplicacionFacade;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades.UsuarioFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author rincostante
 */
@Stateless
@LocalBean
public class AccesoAppServicio {
    
    @EJB
    private AplicacionFacade appFacade;
    @EJB
    private UsuarioFacade usFacade;
    
    private static final Logger logger = Logger.getLogger(Usuario.class.getName());
    
    public List<Aplicacion> getAplicaciones(){
        List<Aplicacion> lstApp = new ArrayList();
        Date date;
        try{
            lstApp = appFacade.findAll();
            logger.log(Level.INFO, "Ejecutando el método getAplicaciones() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getAplicaciones() desde el servicio de Acceso a Aplicaciones. " + date + ". ", ex);
        }
        return lstApp;
    }
    
    public List<Usuario> getUsXApp(Long idApp){
        List<Usuario> lstUs = new ArrayList();
        Date date;
        try{
            lstUs = appFacade.getUsXApp(idApp);
            logger.log(Level.INFO, "Ejecutando el método getUsXApp() desde el servicio");
        }
        catch (Exception ex){
            date = new Date(System.currentTimeMillis());
            logger.log(Level.SEVERE, "Hubo un error al ejecutar el método getUsXApp() desde el servicio de Acceso a Aplicaciones. " + date + ". ", ex);
        }
        return lstUs;
    }
}
