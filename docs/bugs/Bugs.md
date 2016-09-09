Bugs
=========

*Lista de bugs encontrados en la aplicación hasta ahora*

 

 1. **No se pueden eliminar usuarios.**
	 Al intentar borrar un usuario, se lanza la siguiente excepcion:

> Caused by: Exception [EclipseLink-0] (Eclipse Persistence Services - 2.5.0.v20130507-3faac2b): org.eclipse.persistence.exceptions.JPQLException
Exception Description: Syntax error parsing [SELECT app FROM Aplicacion app INNER JOIN ap.usuarios usWHERE us.id = :id]. 
[61, 62] The FROM clause has 'Aplicacion app INNER JOIN ap.usuarios usWHERE' and 'us.id =' that are not separated by a comma.
[69, 70] The FROM clause has 'us.id =' and ':id' that are not separated by a comma.
[68, 69] The identification variable '=' cannot be a reserved word.
[73, 73] An identification variable must be provided for a range variable declaration.

