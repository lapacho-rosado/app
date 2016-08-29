# gestionAplicaciones
Esta aplicación tiene por objeto principal gestionar las aplicaciones a las cuales tienen acceso los usuarios. Para realizarlo implementa un un logueo único contra el directorio activo del dominio mediante el protocolo LDAP.

Distingue dos roles de usuarios: el usuario común que solo puede validar sus credenciales y acceder al listado con todas las aplicaciones que tenga disponibles, y el administrador, que además de tener las funcionalidades del usuario común, también podrá gestionar las aplicciones existentes con la correspondiente gestión de usuarios y agregar nuevas.

Para más datos se recomienda acceder a la documentación de la aplicación en `doc\gestionAplicaciones`.


Arquitectura general:
---------------------

En \\vmfs\Desarrollo\Servicios\gestionAplicaciones se puede acceder a arqGral.vsd que ilustra la dependencia entre las aplicaciones desarrolladas en java. El mismo archivo se agrega en este repositorio.

Ambiente:
---------

Para conocer las características del ambiente para el despliegue de la aplicación se recomiendo leer la documentación alojada en \\vmfs\Desarrollo\Aplicaciones\javaApp


Configuraciones:
----------------

Dado que todas las dependencias de las aplicaciones desarrolladas en java están gestionadas por Maven, una vez levantado el proyecto, deberá actualizarse todas las dependencias mediante el comando respectivo del IDE para que pueda compilar sin inconvenientes.

Deberá generarse el recurso JDBC correspondiente al acceso a datos, según los jndi-name que se especifican en el archivo glassfish-resources.xml. El jta-data-source de la unidad de persistencia que se encuentra en el archivo persistence.xml deberá ser gestAppLocalNDI.

Las credenciales de acceso al servidor de base de datos, por defecto serán: us=postgres, pass=root, en cualquier otro caso, estos datos deberán sobreescribirse en el archivo glassfish-resource.xml.

El archivo Bundle.properties, contiene los datos de server, credenciales de acceso al LDAP, y nombres de las cookies a leer.


Datos:
------

Deberá crearse la base de datos gestionAplicaciones en el servidor local de Postgres y los permisos según se especifica en el archivo glassfish-resource.xml
Los backup de la base se encuentran en \\vmfs\Desarrollo\Servicios\gestionAplicaciones\base y los scripts en \\vmfs\Desarrollo\Servicios\gestionAplicaciones\base\scripts


Servicios:
----------
	
Los archivos xml correspondientes al contrato del servicio web que brinda la aplicación para gestionar el logeo de otras aplicaciones del mismo entorno, se encuentran en \\vmfs\Desarrollo\Servicios\gestionAplicaciones\xml_ws	
