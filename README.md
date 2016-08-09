# gestionAplicaciones
	Esta aplicación tiene por objeto principal gestionar las aplicaciones a las cuales tienen acceso los usuarios. Para realizarlo implementa un un logueo único contra el directorio activo del dominio mediante el protocolo LDAP.

	Distingue dos roles de usuarios: el usuario común que solo puede validar sus credenciales y acceder al listado con todas las aplicaciones que tenga disponibles, y el administrador, que además de tener las funcionalidades del usuario común, también podrá gestionar las aplicciones existentes con la correspondiente gestión de usuarios y agregar nuevas.

	Para más datos se recomienda acceder a la documentación de la aplicación en \\vmfs\Desarrollo\Servicios\gestionAplicaciones.

Ambiente:
---------

	Para conocer las características del ambiente para el despliegue de la aplicación se recomiendo leer la documentación alojada en \\vmfs\Desarrollo\Aplicaciones\javaApp
