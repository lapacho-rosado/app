package ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities;

import ar.gob.ambiente.aplicaciones.gestionaplicaciones.entities.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2015-09-01T13:15:40")
@StaticMetamodel(Aplicacion.class)
public class Aplicacion_ { 

    public static volatile SingularAttribute<Aplicacion, Long> id;
    public static volatile SingularAttribute<Aplicacion, String> nombre;
    public static volatile SingularAttribute<Aplicacion, String> areaReferente;
    public static volatile SingularAttribute<Aplicacion, String> descripcion;
    public static volatile SingularAttribute<Aplicacion, String> rutaImagen;
    public static volatile SingularAttribute<Aplicacion, String> url;
    public static volatile ListAttribute<Aplicacion, Usuario> usuarios;

}