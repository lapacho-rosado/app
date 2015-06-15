/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.ws;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.servicios.AccesoAppServicio;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author rincostante
 */
@WebService(serviceName = "AccesoAppWebService")
@Stateless()
public class AccesoAppWebService {
    
    @EJB
    private AccesoAppServicio ejbRef;
    
    /**
     * Web service operation
     * @return 
     */    
    @WebMethod(operationName = "verAplicaciones")
    public List<Aplicacion> getAplicaciones() {
        return ejbRef.getAplicaciones();
    }

    /**
     * Web service operation
     * @param idApp
     * @return 
     */      
    @WebMethod(operationName = "verUsuariosPorApp")
    public List<Usuario> getUsXApp(@WebParam(name = "idApp") Long idApp) {
        return ejbRef.getUsXApp(idApp);
    }
}
