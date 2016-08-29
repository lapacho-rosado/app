Descripci�n de la aplicaci�n:
-----------------------------
	
	Esta aplicaci�n tiene por objeto principal gestionar las aplicaciones a las cuales tienen acceso los usuarios. Para realizarlo implementa un un logueo �nico contra el directorio activo del dominio mediante el protocolo LDAP.

	Distingue dos roles de usuarios: el usuario com�n que solo puede validar sus credenciales y acceder al listado con todas las aplicaciones que tenga disponibles, y el administrador, que adem�s de tener las funcionalidades del usuario com�n, tambi�n podr� gestionar las aplicciones existentes con la correspondiente gesti�n de usuarios y agregar nuevas.

	Para m�s datos se recomienda acceder a la documentaci�n de la aplicaci�n en \\vmfs\Desarrollo\Servicios\gestionAplicaciones.


Ambiente:
---------

	Para conocer las caracter�sticas del ambiente para el despliegue de la aplicaci�n se recomiendo leer la documentaci�n alojada en \\vmfs\Desarrollo\Aplicaciones\javaApp


Configuraciones:
----------------

	Dado que todas las dependencias de las aplicaciones desarrolladas en java est�n gestionadas por Maven, una vez levantado el proyecto, deber� actualizarse todas las dependencias mediante el comando respectivo del IDE para que pueda compilar sin inconvenientes.

	Deber� generarse el recurso JDBC correspondiente al acceso a datos, seg�n los jndi-name que se especifican en el archivo glassfish-resources.xml. El jta-data-source de la unidad de persistencia que se encuentra en el archivo persistence.xml deber� ser gestAppLocalNDI.

	Las credenciales de acceso al servidor de base de datos, por defecto ser�n: us=postgres, pass=root, en cualquier otro caso, estos datos deber�n sobreescribirse en el archivo glassfish-resource.xml.

	El archivo Bundle.properties, contiene los datos de server, credenciales de acceso al LDAP, y nombres de las cookies a leer.


Datos:
------

	Deber� crearse la base de datos gestionAplicaciones en el servidor local de Postgres y los permisos seg�n se especifica en el archivo glassfish-resource.xml
	Los backup de la base se encuentran en \\vmfs\Desarrollo\Servicios\gestionAplicaciones\base y los scripts en \\vmfs\Desarrollo\Servicios\gestionAplicaciones\base\scripts


Servicios:
----------
	
	Los archivos xml correspondientes al contrato del servicio web que brinda la aplicaci�n para gestionar el logeo de otras aplicaciones del mismo entorno, se encuentran en \\vmfs\Desarrollo\Servicios\gestionAplicaciones\xml_ws