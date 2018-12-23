
Si el móvil se encuentra en vertical, la navegación se realizará invocando nuevas actividades desde la actividad principal.
Todas estás actividades solo tendrán un fragment que proporciona la funcionalidad.

Si el móvil se encuentra en posición horizontal, toda la funcionalidad se mostrará en una misma actividad principal dividida
en dos partes. En una de ellos se mostrará siempre el fragmento con la lista de contactos, y en el otro se irán remplazando
los fragmentos según las acciones del usuario.

Para añadir un contacto, se podrá pulsar el botón en la actividad principal o la opción "Añadir contacto" en la toolbar
de la actividad principal. 
Para eliminar un contacto, se deberá realizar un swipe hacia la derecha o hacia la izquierda.
También existe una funcionalidad para reordenar contactos, manteniendo pulsado uno de los contactos de la lista y moviéndolo
hacia arriba o abajo. 
Para modificar un contacto, se debe hacer un tap corto sobre un contacto para mostrar los detalles del contacto, y,
posteriormente, pulsar el botón "Modificar", y tras realizar los cambios, volver a pulsar el botón "Modificar",
tras lo que se volverá a mostrar los detalles del contacto. Si el móvil está en vertical, será necesario pulsar el botón
"Guardar cambios" en el toolbar para modificar los cambios en la base de datos, mientras que si está en horizontal, no hará
falta, realizándose la modificación en el mismo momento. 
Para llamar a un contacto, se debe pulsar el botón "Llamar" desde los detalles del contacto, con el que se
llamará a la aplicación de llamadas del móvil.
Para exportar o importar los contactos a una tarjeta microSD, existen estas dos opciones en el toolbar del fragmento que
contiene la lista de contactos. Los contactos se exportarán a un fichero con extensión .CNT en formato JSON.
