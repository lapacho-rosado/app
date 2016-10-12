

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Rol;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 
 * @author rincostante
 */
@Stateless
public class RolFacade extends AbstractFacade<Rol> {
    @PersistenceContext(unitName = "ar.gob.ambiente.aplicaciones_gestionAplicaciones_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    /**
     * Método que devuelve true si el Rol tiene usuarios asociados
     * @param rol
     * @return 
     */
    public boolean noTieneDependencias(Rol rol) {
        em = getEntityManager();       
        
        String queryString = "SELECT us FROM Usuario us " 
                + "WHERE us.rol = :rol";      
        
        Query q = em.createQuery(queryString)
                .setParameter("rol", rol);
        
        return q.getResultList().isEmpty();
    }
    
    /**
     * Metodo que verifica si ya existe la entidad.
     * @param aBuscar: es la cadena que buscara para ver si ya existe en la BDD
     * @return: devuelve True o False
     */
    public boolean noExiste(String aBuscar){
        em = getEntityManager();       
        String queryString = "SELECT rol.nombre FROM Rol rol "
                + "WHERE rol.nombre = :aBuscar ";
        
        Query q = em.createQuery(queryString)
                .setParameter("aBuscar", aBuscar);
        return q.getResultList().isEmpty();
    }   
    
    public Rol getXNombre(String aBuscar){
        em = getEntityManager();       
        String queryString = "SELECT rol FROM Rol rol "
                + "WHERE rol.nombre = :aBuscar ";
        Query q = em.createQuery(queryString)
                .setParameter("aBuscar", aBuscar);
        return (Rol)q.getSingleResult();
    }
}
