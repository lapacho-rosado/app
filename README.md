# gestionAplicaciones
Esta aplicación tiene por objeto principal gestionar las aplicaciones a las cuales tienen acceso los usuarios. Para realizarlo implementa un un logueo único contra el directorio activo del dominio mediante el protocolo LDAP.

Distingue dos roles de usuarios: el usuario común que solo puede validar sus credenciales y acceder al listado con todas las aplicaciones que tenga disponibles, y el administrador, que además de tener las funcionalidades del usuario común, también podrá gestionar las aplicciones existentes con la correspondiente gestión de usuarios y agregar nuevas.

Para más datos se recomienda acceder a la documentación de la aplicación en `doc\gestionAplicaciones`.


Arquitectura general:
---------------------

En `doc/gestionAplicaciones` se puede acceder a `arqGral.vsd` que ilustra la dependencia entre las aplicaciones desarrolladas en java. El mismo archivo se agrega en este repositorio.

Ambiente:
---------

Para conocer las características del ambiente para el despliegue de la aplicación se recomienda leer la documentación alojada en `doc/entornoLocal.txt` y `doc/frameworks_tecnologias.txt`


# Conector JDBC para PostGres

Copiar el driver `postgresql-9.X.XXX.jre7.jar` a la carpeta `glassfish/domains/domain1/lib/databases` para tener disponible en el servidor la integracion con PostGres.





Configuraciones:
----------------

Dado que todas las dependencias de las aplicaciones desarrolladas en java están gestionadas por Maven, una vez levantado el proyecto, deberá actualizarse todas las dependencias mediante el comando respectivo del IDE para que pueda compilar sin inconvenientes.

Deberá generarse el recurso JDBC correspondiente al acceso a datos, según los jndi-name que se especifican en el archivo `glassfish-resources.xml`. El jta-data-source de la unidad de persistencia que se encuentra en el archivo persistence.xml deberá ser gestAppLocalNDI.

Las credenciales de acceso al servidor de base de datos, por defecto serán: us=postgres, pass=root, en cualquier otro caso, estos datos deberán sobreescribirse en el archivo glassfish-resource.xml.

El archivo Bundle.properties, contiene los datos de server, credenciales de acceso al LDAP, y nombres de las cookies a leer.


Datos:
------

Deberá crearse la base de datos gestionAplicaciones en el servidor local de Postgres y los permisos según se especifica en el archivo glassfish-resource.xml. Luego modificar el script de carga de datos ubicado en `docs/scriptsBase/Script-gestionAplicaciones.sql` con el usuario y cargar dicho script en la base de datos creada.


Servicios:
----------
	
Los archivos xml correspondientes al contrato del servicio web que brinda la aplicación para gestionar el logeo de otras aplicaciones del mismo entorno, se encuentran en `docs\gestionAplicaciones\xml_ws`	

# Configuracion DB Glashfish
1. Copiar el conector (archivo `docs/db-connectors/postgresql-9.4.1209.jre7.jar` en la carpeta `glassfish/domains/domain1/lib`. Luego iniciar el servidor de Glashfish

* Entrar a la pagina de administracion e ir a `Resources/JDBC/Connection Pools`. Crear un nuevo connection pool con los datos que fueron configurados en `glassfish-resources.xml`, respetando el mismo nombre. Seleccionar `javax.sql.ConnectionPoolDataSource` como tipo y `PostgreSQL` como vendor. Presionar siguiente. 

* Seleccionar `org.postgresql.ds.PGConnectionPoolDataSource` como datasource classname y completar los siguientes datos:

```
DatabaseName=gestionAplicaciones
Password=******* 
PortNumber=5432 (this is the default port but make sure that you are using the correct one)
ServerName=127.0.0.1
User=<database-username>
```

* Presionar Finish para que grabe la configuracion. Se puede probar utilizando el comando "Ping" para verificar que todo funcione correctamente.

* Por ultimo, para poder utilizar este connection pool en la aplicaciones JEE, hay que crear el JNDI. Para ello, ir a  `Resources/JDBC/JDBC Resources`, hacer click en "New" y completar los datos, respetando la informacion de glassfish-resources.xml y seleccionando el connection pool establecido en el paso anterior. Click Ok para finalizar. Este es el JNDI name que usan las aplicaciones para acceder a la base PostgreSQL.






