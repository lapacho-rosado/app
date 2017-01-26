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
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "gestionAplicaciones-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario getXNombre(String usNombre){
        em = getEntityManager();
        List<Usuario> lUs;
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.nombre = :usNombre";
        Query q = em.createQuery(queryString)
                .setParameter("usNombre", usNombre);
        lUs = q.getResultList();
        if(!lUs.isEmpty()){
            return lUs.get(0);
        }else{
            return null;
        }
    }
    
    public boolean noTieneDependencias(Long id){
        em = getEntityManager();       
        String queryString = "SELECT app FROM Aplicacion app "
                + "INNER JOIN app.usuarios us " 
                + "WHERE us.id = :id";     
        Query q = em.createQuery(queryString)
                .setParameter("id", id);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Metodo que verifica si ya existe la entidad con este nombre.
     * @param nombre: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean noExiste(String nombre){
        em = getEntityManager();       
        String queryString = "SELECT us.nombre FROM Usuario us "
                + "WHERE us.nombre = :nombre ";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return q.getResultList().isEmpty();
    }      
    
    /**
     * Metodo que verifica si ya existe la entidad con este nombre.
     * @param us
     * @return: devuelve True o False
     */
    public List<Aplicacion> getApliaciones(Usuario us){
        em = getEntityManager();       
        String queryString = "SELECT app FROM Aplicacion app "
                + "INNER JOIN app.usuarios us "
                + "WHERE us = :us";
        Query q = em.createQuery(queryString)
                .setParameter("us", us);
        return q.getResultList();
    }    
}
