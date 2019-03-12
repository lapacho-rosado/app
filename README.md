# gestionAplicaciones
Esta aplicación tiene por objeto principal gestionar el acceso al conjunto
de aplicaciones que administran y exponen mediante servicios REST,
los datos comunes a consumir por las aplicaciones componentes del SACVeFor
y otras herramientas que los pudieran requerir.

Si bien en un primer contexto el acceso mediante un loggeo único se validaba
mediante LDAP contra el Active Directory de la Secretaría, actualmente lo
hace contra los datos registrados en la base.

Distingue dos roles de usuarios: el usuario común que solo puede validar sus 
credenciales y acceder al listado con todas las aplicaciones que tenga 
disponibles, y el administrador, que además de tener las funcionalidades del 
usuario común, también podrá gestionar las aplicciones existentes con la 
correspondiente gestión de usuarios y agregar nuevas.


Configuraciones:
----------------

Dado que todas las dependencias de las aplicaciones desarrolladas en el proyecto 
están gestionadas por Maven, una vez levantado el mismo, deberá actualizarse todas
las dependencias mediante el comando respectivo del IDE para que pueda compilar
sin inconvenientes.

Deberá crearse en el directorio `scvf-app/src/main/resources/META-INF/` el archivo
`persistence.xml` replicando el contenido del archivo `persistence.dist.xml` para
gestionar la unidad de persistencia de datos.

Deberá crearse en el directorio `scvf-app/src/main/resources/` el archivo 
`Config.properties` replicando el contenido del archivo `Config.properties.dist`,
si el puerto del servidor fuera otro alternativo al 8080, deberá modificarse 
la línea correspondiente.


Datos:
------

Deberá crearse en el servidor de base de datos la base `gestionAplicaciones`
con los parámetros de creación por defecto. Deberá restaurarse luego con el 
backup remitido por la coordinación del proyecto.

Se deberá crear en el servidor de aplicaciones el recurso `Datasource` con 
los siguientes parámetros (los que no se indiquen quedarán por defecto):
Nombre: `GestionAplicacionesDS`
JNDI:  `java:jboss/datasources/gestionAplicaciones`
Driver: `postgresql`
Connection URL: `la que corresponda a la configuración local`
Usuario y password de acceso: `los que correspondan a la configuración local`

Nota: El driver deberá estar previamente registrado en el servidor de aplciaciones.

