# Informe Práctica 4

En este documento se especifica todo lo referente a la práctica 4. NO se han actualizado los manual de usuario ni la documentación técnica para esta práctica. En su lugar, toda la documentación generada se detalla aquí.

### - Release v1.2

* ##### Historias de usuario originales vs implementadas

Al tamaño L se le asignan 4 puntos, al tamaño M, 2 y al tamaño S, 1.
En los tamaños L y M es necesraio descomponer la historia en varias tareas (tickets).

Así es como quedó nuestro tablero con el título y las especificaciones originales:

* **Tamaño grande (L)**

    - ***Dashboard***
        
        **Descripción**
        El usuario quiere un dashboard para poder ver las tareas, notificaciones             y proyectos de un vistazo al inicio y saber el estado de todo rápidamente.
        
        **COS**:
        Se debe haber creado una vista que muestre proyectos, tareas y notificaciones de un usuario.

* **Tamaño mediana (M)**

    - ***Implementar seguridad***
        
        **Descripción**:
        Como usuario quiero que solo yo o gente autorizada pueda entrar a mi lista          para proteger mis datos.
        
        **COS**: 
        Controlar la seguridad y el acceso por URL.
        
    - ***Fecha de tarea***
    
        **Descripción**:
        Como usuario quiero fijar una fecha en cada tarea para organizar entregas y         el tablero. Fecha inicio y final.
        
        **COS**:
        Quiero una fecha límite que cuando la tarea esté cerca de la fecha pueda            cambiar de color.
        Al llegar la fecha límite también cambia de color.
        
    - ***Asignar colores a una tarea***
    
        **Descripción**:
        Como usuario quiero poder asignar colores a una tarea para poder visualizar         lógicamente el proyecto.
        
        **COS**:
        Añadir un color a una tarea y poder visualizar qué color tiene la misma.
        
    - ***Compartir proyecto***
    
        **Descripción**:
        Como usuario quiero compartir un proyecto con otro usuario para poder               trabajar conjuntamente.
        
        **COS**:
        El usuario que ha creado el proyecto puede añadir usuarios colaboradores al         proyecto.
        Los colaboradores no pueden editar el proyecto.
        
    - ***Añadir comentarios***
        
        **Descripción**:
        Como usuario quiero poder añadir comentarios a una tarea o proyecto en              el que participo para poder expresar una idea u opinión.
        
        **COS**:
        Se debe poder poner un texto que sea visto por todos los colaboradores de           las tareas o proyectos.
        Un comentario se puede editar o borrar por el usuario que lo ha escrito o          el adminstrador.

* **Tamaño pequeño (S)**
    
    - ***Estado de la tarea***
    
        **Descripción**:
        El usuario quiere tener un seguimiento del estado de su tarea (finalizado,         iniciado) para tener una idea de la evolución.

        **COS**:
        El usuario debe poder marcar un estado (iniciado, en proceso, finalizado) en la tarea y cambiarlo.
    
    - ***Buscar tareas***
        
        **Descripción**:
        Como usuario quiero buscar tareas para poder encontrarlas más fácilmente.
        
        **COS**:
        A partir de un texto poder filtrar tareas por su descripción.
    
    - ***Filtrado tareas***
    
        **Descripción**:
        Como usuario quiero filtrar las tareas por tamaño o fecha para organizar o         buscar tareas.
        
        **COS**:
        Tener un filtro que muestre solamente las tareas con la estimación puesta en el     filtro.
    
    - ***Exportar tareas***
    
        **Descripción**:
        El usuario quiere poder exportar sus tareas para tenerlas offline.

        **COS**:
        El usuario debe poder expertarlo en un TXT.
    
    - ***Borrar colaboradores proyecto***
    
        **Descripción**: 
        Como usuario porpietario de un proyecto quiero poder eliminar a otros miembros.
        
        **COS**:
        Eliminar a un colaborador y verificar que su nombre no aparece en                  colaboradores.

Para organizar nuestro tablero, primero tuvimos que realizar una estimación cada uno de los componentes del grupo para definir definitivamente el tamaño de las tareas. Se crearon las funcionalidades (Feature - FT) y cada una de ellas se descompuso en tareas o tickets (TC-X).

Antes de nada comentar que el tablero es básicamente el mismo salvo en una sola funcionalidad que cambió de tamaño. **Compartir proyecto** era tamaño mediano (M) y se ha pasado a (L). También, a parte de modificar la descripción como es lógico para especificar más, se han cambiado algunos títulos para hacerlos más claros.

A continuación se mostrará el tablero con su descripción, sus condiciones y sus tickets.

* **Features (FT)**

   - ***FT-3 Implementar seguridad (M)***
   
        **Descripción**:
        Como usuario quiero que solo yo pueda ver o modificar mis datos.
        Como administrador quiero tener acceso a todo el sistema y que un usuario solo pueda ver o modificar sus propios datos.

        **COS**:
        Los datos de un usuario solo podrán ser vistos o modificados por él mismo o         por el administrador del sistema.
        Si un usuario intenta acceder a una URL que no le pertenece, será         redireccionado a su dashboard principal.
        Las secciones visuales (menús, botones, etc.) para un usuario serán diferentes que para las del administrador. 
        El usuario no tendrá de momento ningún menú izquierdo, navegará a través de enlaces o botones.
        El administrador podrá tener acceso a un menú en la izquierda.
    
    - ***FT-4 Añadir comentarios (S)***
    
        **Descripción**:
    Como usuario quiero poder añadir comentarios a un proyecto en el que participo para poder expresar una idea u opinión.

        **COS**:
        Los comentarios de un proyecto se podrán añadir y visualizar en los detalles del proyecto.
        Un comentario se puede editar o borrar por el usuario que lo ha escrito o por el adminstrador del proyecto.

        **Documentación**:
        https://docs.google.com/document/d/1AVhqb8lcuFg3ZX7O15XYHHyKNjKrdctcSBgECCA3S4g/edit
        
    - ***FT-5 Compartir proyecto (L)***
    
        **Descripción**:
    Como usuario quiero compartir un proyecto con otro usuario para poder visualizar las tareas del proyecto.

        **COS**:
        El usuario que ha creado el proyecto puede añadir usuarios colaboradores al proyecto, seleccionándolos de una lista de usuarios registrados.
        Los colaboradores no pueden editar la descripción del proyecto.
        En la tabla de proyectos debe aparecer tanto sus proyectos como los compartidos.
        
    - ***FT-6 Estado tarea (S)***
    
        **Descripción**:
    Como usuario quiero poder marcar una tarea como iniciada o terminada para organizarme mejor.

        **COS**:
        Las tareas al crearse tendrán un estado por defecto que será "sin empezar".
        El estado de la tarea podrá cambiarse en el modificar mediante un selector.
        Los estados pueden ser "iniciada", "terminada" o "sin empezar".
        El estado de la tarea  se visualizará tanto en los detalles de cada tarea como en el listado.
        
    - ***FT-7 Buscar tareas (S)***
    
        **Descripción**:
        Como usuario quiero buscar tareas para poder encontrarlas más fácilmente.

        **COS**:
        A partir de un texto escrito en un buscador se filtrarán las tareas por su descripción.
        La descripción de las tareas resultantes deben contener el texto del buscador, no se estrictamente el mismo.

    - ***FT-8 Filtrado tareas (S)***
    
        **Descripción**:
        Como usuario quiero filtrar las tareas por estado o fecha para organizar las tareas.

        **COS**:
        Disponer de un filtro de tareas que filtre tanto por estado y color como por fecha de finalización.
        El filtrado se hará a través del mismo campo buscador para no complicar la tarea.
        Debe permitir buscar por todos los campos que tiene la tarea. Si se agregan nuevos campos, hay que añadirlos al filtrado.
        
    - ***FT-9 Dashboard (L)***
    
        **Descripción**:
    Como usuario quiero un dashboard para poder ver las tareas, proyectos y datos estadísticos de un vistazo al inicio y saber el estado actual de todo rápidamente.

        **COS**:
        Al conectarse el usuario su página principal será el dashboard.
        En él aparecerán los proyectos asociados, sus tareas y ciertos datos informativos.
        
    - ***FT-10 Fecha tareas (M)***
    
        **Descripción**:
    Como usuario de una tarea quiero añadir fecha de finalización.

        **COS**:
        La fecha de finalización puede no indicarse (opcional).
        Poder indicar la fecha de finalización en la inserción y en la modificación de la tarea.
        La fecha de finalización se visualizará en los detalles de la tarea, teniendo un texto con letra más grande que los demás datos.
        En el listado de tareas también se visualizará la fecha de finalización.
        
    - ***FT-11 Asignar colores tarea (M)***
    
        **Descripción**:
        Como usuario quiero poder asignar colores a una tarea para visualizarla más rápidamente.
        
        **COS**:
        Poder elegir colores de entre un listado estático y asignarlos a la tarea. Esta opción podrá darse en la inserción y en la modificación de la tarea.
        Los colores de cada tarea se visualizarán tanto en los detalles de cada tarea como en el listado de todas las tareas, en pequeño.
        
    - ***FT-12 Exportar tareas (S)***
    
        **Descripción**:
        Como usuario quiero poder exportar mis tareas para tenerlas offline, en mi ordenador.

        **COS**:
        El usuario debe poder exportar sus tareas en un archivo con extensión .txt.
        En el archivo aparecerán todos los datos de cada tarea. Cada tarea estará separada por un salto de línea.

        **Enlace a documento explicativo Google Docs**:
        https://docs.google.com/document/d/16se-t22KXajcM0Z68KIZk7WhwQq7usw-pSANILsgBBI/edit?usp=sharing
        
    - ***FT-13 Borrar colaboradores proyecto (S)***
    
        **Descripción**: 
        Como usuario propietario de un proyecto quiero poder eliminar a otros miembros colaboradores.

        **COS**:
        Eliminar a un colaborador y verificar que su nombre no aparece en colaboradores.
        Al eliminar un proyecto, antes también se tienen que eliminar sus colaboradores. Si no, falla la integridad referencial.
        
    - ***FT-14 Release v1.2***
    
        **Descripción**: 
        Una vez terminadas todas las FT-2, crear la Release v1.1 del proyecto.

* **Tickets (TC-X)**

    - ***TIC-3.1 Investigar funcionalidad nativa control acceso sesión***
        
        Investigar sobre funcionalidad  sobre controlar acceso por sesión.

        **Pruebas funcionales**
        
        Enlace a un googledocs sobre la funcionalidad de la sesión en la parte del controller: https://docs.google.com/document/d/1FDOoZAZJZaAyM_Bw80Z1xm_wj7bfND8_FqmD1oijKxE/edit?usp=sharing
    
    - ***TIC-3.2 Aplicar por todo el sistema la seguridad***
        
        Aplicar en todo el código del sistema la investigación realizada en el ticket anterior.

        Asegurarse que bloquea el acceso a todo aquel que intente acceder a una sección del programa no permitida.

        Enlace a un googledocs sobre la funcionalidad de la aplicacion de la seguridad en todo el sistema:
        https://docs.google.com/document/d/1-l6Fx9nY2CO9IZJWO9L0nTotm1ZoG9NwIyY_PMnqPwI/edit#heading=h.afdwlbl8wbx8
        
    - ***TIC-4.1 Añadir comentarios***
        
        Los comentarios de un proyecto se podrán añadir y visualizar en los detalles del proyecto.
        Un comentario se puede editar o borrar por el usuario que lo ha escrito o por el adminstrador del proyecto.
        
    - ***TIC-5.1 El propietario de un proyecto puede añadir colaboradores***
        
        En la clase Proyecto añadir una lista de colaboradores.
        En el editar de Proyecto añadir el selector de usuarios.
        
    - ***TIC-5.2 Los usuarios pueden ver los proyectos en los que son colaboradores***
        
        Cambiar la seguridad por sesión para que un usuario pueda entrar a la vista de los proyectos en los que es colaborador, pero no al editar.
        En el listado de Proyectos, un usuarios tiene que poder ver los proyectos que ha creado y también los que es colaborador.

        Enlace a un googledocs sobre la vista de los proyectos donde los usuarios pueden ver tanto sus proyectos como en los cuales colaboran: 
        https://docs.google.com/a/gcloud.ua.es/document/d/1gHIalRQ6jLa91o_e1oJuCmZUrjV_QNA9tRpkwOMfnoY/edit?usp=sharing
        
    - ***TIC-5.3 Añadir descripción al proyecto***
    
        Se puede meter desde el editar, así solamente el creador del proyecto puede editar la descripción.
        En el ver solamente se visualizará.
        
    - ***TIC-5.4 Los colaboradores de un proyecto pueden asignar tareas***
        
        Cambiar el asignar tarea y la tabla de tareas del editar al ver de     proyecto, así los colaboradores podrán también asignar tareas.
        
    - ***TIC-6.1 Estado tarea***
        
        Las tareas al crearse tendrán un estado por defecto que será "sin empezar".
        El estado de la tarea podrá cambiarse en el modificar mediante un selector.
        Los estados pueden ser "iniciada", "terminada" o "sin empezar".
        El estado de la tarea  se visualizará tanto en los detalles de cada tarea como en el listado.
        
    - ***TIC-7.1 Búsqueda tareas***
    
        En el listado de tareas añadir un cuadro de texto para permitir buscar las tareas.
        En este ticket se habilitará que se pueda buscar por id y por descripción.

        **Revisión**
        Ticket más largo de lo esperado. Se vio la posibilidad de incluir el plugin         basado en html/jquery Datatable que agiliza (una vez implementado) la          navegación y la gestión de una tabla con datos: permite búsquedas, filtros,        exportaciones, etc.
    El desarrollo ha sido costoso pero finalmente se ha conseguido.
    
    - ***TIC-8.1 Filtrado de campos de Tarea***
    
        Permitir filtrar todos los campos de tareas que aparecen en el listado de tareas del usuario.
        Utilizando el buscador generado en la FT-7, se puede utilizar para filtrar por distintos campos. Ampliar funcionamiento a demás campos tales como: estado, fecha de finalización o color.
        
    - ***TIC-9.1 Incluir diseño y funcionamiento estático***
    
        Incluir un diseño que mejore la presencia de la primera página que ve el usuario. Ya está implantada la plantilla, simplemente mejorarlo con elementos más vistosos.

        Incluir enlaces y presentación de forma estática, que los enlaces no lleven a ningún sitio de momento.

        **Plantillas a valorar**:
        http://wrapbootstrap.com/preview/WB019K3P8
        http://themedesigner.in/demo/matrix-admin/index.html
        https://blackrockdigital.github.io/startbootstrap-sb-admin/

        **Plugins interesantes**:
        http://canvasjs.com/samples/dashboards/
        
    - ***TIC-9.2 Avance de tareas y proyectos del usuario***
    
        Que aparezca un avance (en forma de tabla) de las tareas y proyectos (mostrar tres de cada - si los tuviera).

        Podrá navegar desde esa tabla-avance al listado de todas las tareas y al listado de todos los proyectos.

    - ***TIC-9.3 Elementos informativos***
    
        Mostrar mensajes que aporten valor informativo al usuario: 
        Nº Tareas abiertas (Sin Empezar, Iniciada) y nº de tareas acabadas.
        Proyecto con más comentarios y más colaboradores.
        
    - ***TIC-9.4 Dashboard como página principal***
    
        Establecer la página de dashboard como página principal. 
        
        Verificar que al loguearse un usuario entra a la página de dashboard.
        
    - ***TIC-10.1 Creación fecha de finalización de tarea***
    
        Añadir fecha de finalización.
        
        Validar la fecha con javascript para que tenga formato correcto y que sea mayor que la fecha actual.
        
    - ***TIC-10.2 Modificación de fecha, demostración.***
    
        La fecha de finalización se visualizará en los detalles de la tarea, teniendo un texto con letra más grande que los demás datos.
        
        En el listado de tareas también se visualizará la fecha de finalización.
        
        Modificación de la fecha de finalizacion.
        
    - ***TIC-11.1 Añadir color a la tarea***
        
        Añadir el color en la entidad Tarea.
        
        Que se pueda elegir el color tanto en el crear como en el editar de la tarea.
        
        El color es opcional.
        
    - ***TIC-11.2 Visualizar el color de la tarea en el listado de tareas***
        
        Lo ideal sería que se viera el color en la vista en lugar de verse el código hexadecimal del color.
        
    - ***TIC-12.1 Crear enlaces y descargar archivo txt***
    
        En esta tarea será necesario un cierto tiempo de investigación sobre cómo trabaja Play con archivos (aunque imaginamos que será propio de Java, pero puede tener cierta configuración más al tener que descargar desde el servidor).

        Se crearán los enlaces de descarga. En las vistas se establecerán enlaces de descarga. Estos enlaces no irán a ningún sitio todavía.

        Una vez investigado, se desarrollará la utilidad de que al pinchar en algún enlace, empiece a descargar el archivo.

        El archivo contendrá, tal y como indica la Feature, los datos de cada tarea y la separación entre tareas será con un salto de línea.
        
    - ***TIC-13.1 Borrar colaboradores del proyecto***
    
        Se permite borrar los colaboradores de un proyecto.
        
    - ***TIC-14.1 Documentación sprint 1***
    
        Realizar la documentación requerida en la práctica 4 (sprint 1)

Si tenemos en cuenta el tamaño en puntos de cada ticket (se detalla al principio de este documento), quedaría así para cada uno de nosotros:

* **Ouadi Chamit (7 puntos)**

    - ***FT-3 (M) (2 puntos)***
    - ***FT-5 (L) (2 puntos)***
    - ***FT-6 (S) (1 punto)***
    - ***FT-10 (M) (2 puntos)***
    
* **Benjamín Pamies Cartagena (6 puntos)**

    - ***FT-4 (S) (1 punto)***
    - ***FT-5 (L) (2 punto)***
    - ***FT-11 (M) (2 puntos)***
    - ***FT-13 (S) (1 punto)***
    
* **Manuel García Menárguez (7 puntos)**

    - ***FT-7 (S) (1 punto)***
    - ***FT-8 (S) (1 punto)***
    - ***FT-9 (L) (4 puntos)***
    - ***FT-12 (S) (1 punto)***

* ##### Capturas de la web

Se muestran algunas capturas de las secciones desarroladas y/o mejoradas en este sprint.

* **Dashboard**

![GitHub Logo](/docs/dashboard1.png)

![GitHub Logo](/docs/dashboard2.png)

* **Creación de tarea con nuevos parámetros**

Los parámetros nuevos son: fecha de finalización, estado y color. Éste último se muestra un selector de colores en hexadecimal. Para ello se ha utilizado un plugin javascript llamado JSColor.

![GitHub Logo](/docs/parametrosTareas.png)

* **Búsqueda, filtrado y exportar tareas**

Se ha realizado mediante el plugin Datatable realizado en HTML, CSS y Jquery.

La carga y manejo de los datos es mediante un método en AJAX que accede aun método de un Controller y devuelve una respuesta al manejador de Datatable para cargar los datos de forma paginada (10 registros por página).

![GitHub Logo](/docs/busquedaTareas.png)

* **Comentarios del proyecto**

Un usuario puede añadir una descripción y también realizar comentarios en el proyecto.

![GitHub Logo](/docs/descripcionProyecto.png)

* **Asignar tareas y colaboradores al proyecto**

Un usuario puede, mediante la vista de edición del proyecto, asignar tareas propias al proyecto y también asignar colaboradores. Los colaboradores son usuarios registrados en el sistema.

![GitHub Logo](/docs/asignarTareaProyecto.png)

![GitHub Logo](/docs/asignarColaborador.png)

* ##### Metodología seguida

La metodología seguida en este sprint ha sido la de reunirnos o presencialmente o por Slack los tres componentes del grupo.

En el tablero "En espera", se han ido definiendo las funcionalidades (FT) y cuando uno de los componentes elige esa feature, lo descompone en 1 o varios tickets.

Se intentan documentar lo mejor posibles los tickets y pasa al tablero "Seleccionados" dicho ticket (la FT permanecerá en el tablero "En espera" hasta que termine toda su funcionalidad). Dicho tablero solo puede tener un máximo de 5 tickets (WIP=5). En ningún momento del sprint se ha conseguido llenar.

Cuando se empieza a desarrollar, se pasa  el ticket al tablero "En desarrollo", se crea una rama y se sube al repositorio para que esté visible. Este punto en los últimos tickets no se ha cumplido al 100%, en su lugar, se ha trabajado en local y cuando se ha tenido una versión más o menos terminada del ticket, se sube para ya realizar el pull request.

Cuando la rama necesita mezclarse, se añade a "En desarrollo - Done" y posteriormente al tablero "Integración". Dicho tablero puede tener un máximo de 2 tickets (WIP=2). El resto permanecerán en un estado anterior "En desarrollo - Done" esperando a que se mezclen dichos pull requests. **Ver último punto** de Retrospectiva para ver qué inconvenientes nos ha presentado este punto.

Una vez se mezcla el pull request en la rama master, se añade el ticket al tablero "Integración - Done". Posteriormente, al tablero "Pruebas funcionales". Lo ideal sería que cuando está en este estado "Pruebas funcionales", todos los componentes implicados en la aceptación del pull request (en concreto, 2 según hemos establecido) prueben el, pull request que acaba de mezclarse para probarlo en la misma aplicación. Esto no ha sido siempre así, si no que se ha optado por un modelo algo más "en cascada" en este punto. Se ha aceptado todo pull request (con una mínima revisión) y si alguien en algún momento ha detectado un error con un pull request antiguo, se le ha comentado al responsable del ticket, para que lo tenga en cuenta y lo solucione posteriormente, aunque fuera con otro commit y estando desarrollando otra tarea.

* ##### Informes reuniones scrum

Los componentes del grupo hemos simulado dos reuniones en scrum (se han hecho más, pero documentadas solo éstas).

Hemos detallado al máximo posible lo que se habló en esas reuniones:

* **Reunión diaria 1**

**Fecha:  Miércoles 30 de noviembre: 13:15 a 14:15**

Algunas tareas *off-topic* de las típicas preguntas propias de un scrum diario:

- Se añade el número de feature a cada tarjeta.

	FT-X Nombre funcionalidad
	
- Se añaden las etiquetas necesarias a cada feature.

	- Amarilla, verde o azul según corresponda su tamaño (esto ya estaba de antemano)
	- Amarilla para indicar que es "Feature"
	- Verde junto con el nombre de cada funcionalidad. Aquí comenzamos creando una etiqueta de cada color en vez de verde, pero vimos
	que tener tres colores diferentes para cada funcionalidad puede ser un caos, además que luego se repetirían puesto que hay más funcionalidades que colores disponibles.
	Así que se ha dejado tal y como se hizo en la práctica 3: Color verde y mismo nombre que funcionalidad.

Preguntas:

- Ouadi Chamit

    - ¿Qué he hecho?: He investigado acerca la funcionalidad del acceso por sesión en Play Framework. (FT-3 Implementar seguridad)
    
	 - ¿Qué voy a hacer?: Empezar con el código a hacer pruebas de lo encontrado por Internet.

- Benjamín Pamies Cartagena

	 - ¿Qué he hecho?: Averiguar cómo crear relaciones entre clases, y cómo se crea la tabla en la base de datos respecto al ticket TIC-4.1 Añadir comentarios
	  
    - ¿Qué voy a hacer? Implementar la tarea de crear un comentario sobre un proyecto.
			
- Manuel García Menárguez

	 - ¿Qué he hecho?: Investigar cómo exportar fichero txt en play respecto al ticket TIC-12.1 Detalles tarea y exportar tareas txt.
	    
    - ¿Qué voy a hacer?: Implementar la tarea.

* **Reunión diaria 2**

**Fecha:  Miércoles 14 de diciembre: 13:15 a 14:15**

Preguntas:

- Ouadi Chamit

    - ¿Qué he hecho?: En el listado de proyectos de un usuario, además de aparecer los proyectos que ha creado, ahora también tienen que aparecer los proyectos en los que le han añadido como colaborador.
Los proyectos en los que es colaborador en la columna ACCIONES sólo debe aparecer el icono de la lupa para entrar en la vista de detalles, los botones de editar y borrar se tienen que ocultar para que no pueda pulsarlos.

    - ¿Qué voy a hacer?: Implementar la tarea de permitir al usuario poder asignar una tarea a un proyecto, tocar la parte del controller y la capa dao y services, comprobar si se asigna la tarea.

- Benjamín Pamies Cartagena

	- ¿Qué he hecho?: Crear el Service y la relación N:M Colaborador-Proyecto.
	  
    - ¿Qué voy a hacer? Modificar el modelo Usuario y el modelo Proyecto para que pueda tener una relación muchos a muchos N:M. Implementar y probar si se añade un colaborador a un proyecto desde el usuario propietario del proyecto.
			
- Manuel García Menárguez

	 - ¿Qué he hecho?: He realizado los dos primeros tickets de la FT-9 Dashboard. Los puntos ya permiten visualizar el dashboard gráficamente y la tabla de tareas y proyectos.
	    
    - ¿Qué voy a hacer?: Quedan dos últimos tickets de la FT-9 Dashboard. Mostrarán más información en la vista como el nº de tareas abiertas y cerradas, los proyectos con más comentarios o más colaboradores. También intentaré hacer para mañana que el dashboard sea la página de inicio, que es el ticket 9.4.

* ##### Retrospectiva y conclusiones del sprint

Echamos la vista atrás para analizar todo lo ocurrido en este sprint y enumeramos algunos puntos que han ido bien y otros que se podrían mejorar en siguientes sprints.
* **Detalles que sí han funcionado**

Hemos estado bien sincronizados gracias a la comunicación de Slack con Github y a su vez Trello con Github.

Trello ha sido muy buen planificador de tareas, a partir de ello podemos tener una vision global de cómo ha ido el desarrollo de las funcionalidades, así como ir viendo mediante la navegación entre tableros los estados de las tareas.

Cada uno cuando ha visto algo por corregir o mejorar, hemos dejado "nota" en el chat de Slack y cada uno cuando ha podido lo ha visto y solventado. La comunicación es muy dinámica.

En cuanto al desarrollo, ha sido llevado prácticamente al día.

* **Detalles por mejorar**

Una de las cosas a mejorar ha sido un error por nuestra parte por establecer la aceptación del pull request a 2 personas (somos 3). Si uno de los que tiene que aceptarlo no está disponible en el momento en el que se crea el pull request, eso genera retraso. Esto ha provocado algunos retrasos en la mezcla con la rama master y ciertas dependencias en algunas tareas (no se puede iniciar una hasta que no esté la otra integrada). 

Respecto a Trello, se ha echado en falta una funcionalidad y no es otra que la de permitir agrupar tareas. El funcionamiento de colores ha sido algo confuso.

Por tanto, para el siguiente sprint y depende de cómo se avance en el proyecto, podría valorarse el utilizar otro gestor de tareas más avanzado.



