/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.gob.ambiente.aplicaciones.gestionaplicaciones.util;

import java.io.IOException;
import java.util.Iterator;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.view.ViewDeclarationLanguage;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION_TYPE;
import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rincostante
 */
public class AjaxExceptionHandler extends ExceptionHandlerWrapper {
    /**
     * Logger
     */
    public static final Log LOG = (Log) LogFactory.getLog(AjaxExceptionHandler.class);
    
    /**
     * Exception handler encapsulado
     */
    private ExceptionHandler wrapped;

    /**
     * Constructor de un nuevo exception handler para peticiones
     * ajax encapsulando el exception handler indicado. 
     * @param wrapped
     */
    public AjaxExceptionHandler(ExceptionHandler wrapped){
        this.wrapped = wrapped;
    }
    
    /**
     * Maneja las excepciones en peticiones ajax de la siguiente manera,
     * sólo y sólo si la actual petición es una petición ajax cuya
     * respuesta aún no ha sido enviada y existe al menos una excepción que no ha sido tratada.
     * Las demás excepciones pendientes serán ignoradas, primero hay que corregir la primera.
     */    
    @Override
    public void handle(){
        handleAjaxException(getContext());
        wrapped.handle();
    }
    
    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }
    
    /**
     * Metodo que devuelve el contexto JSF
     * @return Contexto JSF actual
     */
    private static FacesContext getContext(){
        return FacesContext.getCurrentInstance();
    }
    
    /**
     * Método que se encarga de tratar las excepciones encontradas
     * durante una petición JSF. Sólo se van a tratar las excepciones en
     * peticiones ajax. Si la excepción es en una petición HTTP normal ya
     * se encarga el web.xml de redirigir a la página de error.
     * @param context Contexto JSF actual.
     */
    private void handleAjaxException(FacesContext context){
        if (context == null || !context.getPartialViewContext().isAjaxRequest()) {
            return; // No es una request ajax
        }
        
        Iterator<ExceptionQueuedEvent> unhandledExcQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();        
        if (!unhandledExcQueuedEvents.hasNext()){
            return; // No hay excepciones pendientes.
        }
        
        Throwable exception = unhandledExcQueuedEvents.next().getContext().getException();
        if (exception instanceof AbortProcessingException){
            return; // Dejamos que lo maneje JSF.
        }
        exception = findExceptionRootCause(exception);
      
        String errorPageLocation = "/error.xhtml";
        unhandledExcQueuedEvents.remove();
        ExternalContext externalContext = context.getExternalContext();
        LOG.error(String.format("Ocurrio un error no esperado, redirigiendo a %s", errorPageLocation), exception);
        
        // Añadimos información sobre la excepcion al request HTTP para que pueda ser mostrada en la pagina de error
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        request.setAttribute(ERROR_EXCEPTION, exception);
        request.setAttribute(ERROR_EXCEPTION_TYPE, exception.getClass());
        request.setAttribute(ERROR_MESSAGE, exception.getMessage());
        request.setAttribute(ERROR_REQUEST_URI, request.getRequestURI());
        request.setAttribute(ERROR_STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            renderErrorPageView(context, request, errorPageLocation);
        } catch (IOException e) {
            throw new FacesException(e);
        }
        while (unhandledExcQueuedEvents.hasNext()) {
            // No nos interesan el resto de excepciones, sólo la primera.
            unhandledExcQueuedEvents.next();
            unhandledExcQueuedEvents.remove();
        }
    }
    
    /**
     * Determina la raiz de la causa de una excepción.
     * @param exception La excepción de la que se quiere encontrar la raiz de la causa.
     */
    private Throwable findExceptionRootCause(Throwable exception){
        return unwrap(exception);
    }
    
    private static <T extends Throwable> Throwable unwrap(Throwable exception, Class<T> type){
        while (type.isInstance(exception) && exception.getCause() != null) {
            exception = exception.getCause();
        }
        return exception;
    }
    
    /**
     * Devuelve las causas anidadas de una excepción dada mientras no sean instancias de FacesException (Mojarra) o ELException (MyFaces).
     * @param exception La excepción de la que se quiere quitar el anidamiento con FacesException y ELException.
     * @return La causa de la excepción.
     */
    private static Throwable unwrap(Throwable exception){
        return unwrap(unwrap(exception, FacesException.class),ELException.class);
    }
    
    /**
     * Muestra la página de error indicada.
     * @param context Contexto JSF actual
     * @param request Request de la petición actual
     * @param errorPageLocation Localización de la página de error a mostrar.
     * @throws IOException En caso de que ocurra un error mostrando la página de error, y no se pueda mostrar la página de error de emergencia.
     */
    private void renderErrorPageView(FacesContext context, final HttpServletRequest request, String errorPageLocation) throws IOException{
        String viewId = errorPageLocation;
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, viewId);
        context.setViewRoot(viewRoot);
        context.getPartialViewContext().setRenderAll(true);
        try {
            ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(context, viewId);
            vdl.buildView(context, viewRoot);
            context.getApplication().publishEvent(context,PreRenderViewEvent.class, viewRoot);
            vdl.renderView(context, viewRoot);
            context.responseComplete();
        } catch (IOException e) {
            // Aqui podríamos mostrar una página de error estática si todo ha ido mal
            throw new FacesException(e);
        } finally {
            // Evitamos que el contenedor de la aplicación trate de manejar esta excepción.
            request.removeAttribute(ERROR_EXCEPTION);
        }
    }
}
