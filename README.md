# gestionAplicaciones
Esta aplicación tiene por objeto principal gestionar las aplicaciones a las 
cuales tienen acceso los usuarios. Para realizarlo implementa un un logueo 
único contra el directorio activo del dominio mediante el protocolo LDAP.

Distingue dos roles de usuarios: el usuario común que solo puede validar sus 
credenciales y acceder al listado con todas las aplicaciones que tenga 
disponibles, y el administrador, que además de tener las funcionalidades del 
usuario común, también podrá gestionar las aplicciones existentes con la 
correspondiente gestión de usuarios y agregar nuevas.

Para más datos se recomienda acceder a la documentación de la aplicación 
en `doc\gestionAplicaciones`.


Arquitectura general:
---------------------

En `doc/gestionAplicaciones` se puede acceder a `arqGral.vsd` que ilustra la 
dependencia entre las aplicaciones desarrolladas en java. El mismo archivo se 
agrega en este repositorio.

Ambiente:
---------

Para conocer las características del ambiente para el despliegue de la 
aplicación se recomienda leer la documentación alojada en `doc/entornoLocal.txt` 
y `doc/frameworks_tecnologias.txt`


# Conector JDBC para PostGres

Copiar el driver `lib/postgresql-9.X.XXX.jre7.jar` a la carpeta 
`wildfly-10.0.0.Final/standalone/deployments/` para tener disponible en el 
servidor la integracion con PostGres. Tambien se puede deployar mediante la 
opcion Deployment desde la consola de administracion 
(http://localhost:9990/console/App.html#standalone-deployments)



Configuraciones:
----------------

Dado que todas las dependencias de las aplicaciones desarrolladas en java están 
gestionadas por Maven, una vez levantado el proyecto, deberá actualizarse todas
 las dependencias mediante el comando respectivo del IDE para que pueda compilar
 sin inconvenientes.


Deberá generarse el recurso JDBC correspondiente al acceso a datos, según los 
jndi-name que se especifican. Para ello, crear un datasource de postgres. 
Donde en la seccion se datasources se agrega lo siguiente (suponiendo el que 
JNDI elegido sea `gestionaplicacionesDS`). 

Como driver, seleccionar (en la consola de administracion) detected driver y 
elegir el driver jdbc deployado en el servidor de wildfly.

El string de conexion es: jdbc:postgresql://localhost:5432/gestionaplicaciones


La unidad de persistencia que se encuentra en el archivo persistence.xml deberá 
ser gestionaplicacionesDS.

El archivo Bundle.properties, contiene los datos de server, credenciales de 
acceso al LDAP, y nombres de las cookies a leer.


Datos:
------

Deberá crearse la base de datos gestionAplicaciones en el servidor local de 
Postgres. Luego modificar el script de carga de datos ubicados en `docs/DB` 
con el usuario y cargar dicho script en la base de datos creada.


Servicios:
----------
	
Los archivos xml correspondientes al contrato del servicio web que brinda la 
aplicación para gestionar el logeo de otras aplicaciones del mismo entorno, 
se encuentran en `docs\gestionAplicaciones\xml_ws`	



#Configuraciones Adicionales de Wildfly
Estas son las configuraciones en el contenedor:

1. Copiar el jar de Eclipse Link que esta en 
`lib/` en wildfly-10/modules/system/layers/base/org/eclipse/persistence/main 
*. Modificar el archivo module.xml agreguando lo siguiente:

```
...

<resource-root path="eclipselink-2.6.4.jar">
	<filter>
		<exclude path="javax/**"/>
	</filter>
</resource-root>
...

<module name="javax.ws.rs.api"/>

```

*. Se modifica el standalone.xml con el siguiente comando:

jboss-cli.sh --connect '/system-property=eclipselink.archive.factory:add(value=org.jipijapa.eclipselink.JBossArchiveFactoryImpl)'

