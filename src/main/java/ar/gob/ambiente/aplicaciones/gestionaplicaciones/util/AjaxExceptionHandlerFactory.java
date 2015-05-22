/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.util;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author rincostante
 */
public class AjaxExceptionHandlerFactory extends ExceptionHandlerFactory {
    
    /**
     * wrapped
     */
    private ExceptionHandlerFactory wrapped;
    
    /**
     * Constructor de una factoria para el manejo de excepciones AJAX.
     * @param wrapped La factoría que se encapsula.
     */
    public AjaxExceptionHandlerFactory(ExceptionHandlerFactory wrapped){
        this.wrapped = wrapped;
    }

    /**
     * Devuelve una nueva instancia de AjaxExceptionHandler que envuelve el exception handler original.
     * @return ExceptionHandler ExceptionHandler
     */    
    @Override
    public ExceptionHandler getExceptionHandler() {
        return new AjaxExceptionHandler(getWrapped().getExceptionHandler());
    }
    
    /**
     * Devuelve la factoría encapsulada.
     * @return ExceptionHandlerFactory ExceptionHandlerFactory
     */    
    @Override
    public ExceptionHandlerFactory getWrapped(){
        return wrapped;
    }    
}
