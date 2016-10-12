/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion;
import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rincostante
 */
@Stateless
public class AplicacionFacade extends AbstractFacade<Aplicacion> {
    @PersistenceContext(unitName = "ar.gob.ambiente.aplicaciones_gestionAplicaciones_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AplicacionFacade() {
        super(Aplicacion.class);
    }
    
    /**
     * Metodo que verifica si ya existe la entidad con este nombre.
     * @param nombre: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean noExisteNombre(String nombre){
        em = getEntityManager();       
        String queryString = "SELECT app.nombre FROM Aplicacion app "
                + "WHERE app.nombre = :nombre ";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }  
    
    /**
     * Metodo que verifica si ya existe la entidad con esta url.
     * @param url: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean noExisteUrl(String url){
        em = getEntityManager();       
        String queryString = "SELECT app.url FROM Aplicacion app "
                + "WHERE app.url = :url ";
        Query q = em.createQuery(queryString)
                .setParameter("url", url);
        return q.getResultList().isEmpty();
    }      
    
    /**
     * Método que verifica si la entidad tiene dependencias
     * @param id: ID de la entidad
     * @return: True o False
     */
    public boolean noTieneDependencias(Long id){
        em = getEntityManager();       
        String queryString = "SELECT us FROM Usuario us "
                + "INNER JOIN us.aplicaciones app " 
                + "WHERE app.id = :id";      
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    }      
    
    /**
     * Método que devuelve los usuarios de una aplicación
     * @param id
     * @return 
     */
    public List<Usuario> getUsXApp(Long id){
        em = getEntityManager();       
        String queryString = "SELECT app.usuarios FROM Aplicacion app "
                + "WHERE app.id = :id";
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList();
    }
}
