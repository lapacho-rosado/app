/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.facades;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Aplicacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}
