# Informe Pr�ctica 4

En este documento se especifica todo lo referente a la pr�ctica 4. NO se han actualizado los manual de usuario ni la documentaci�n t�cnica para esta pr�ctica. En su lugar, toda la documentaci�n generada se detalla aqu�.

### - Release v1.2

* ##### Historias de usuario originales vs implementadas

Al tama�o L se le asignan 4 puntos, al tama�o M, 2 y al tama�o S, 1.
En los tama�os L y M es necesraio descomponer la historia en varias tareas (tickets).

As� es como qued� nuestro tablero con el t�tulo y las especificaciones originales:

* **Tama�o grande (L)**

    - ***Dashboard***
        
        **Descripci�n**
        El usuario quiere un dashboard para poder ver las tareas, notificaciones             y proyectos de un vistazo al inicio y saber el estado de todo r�pidamente.
        
        **COS**:
        Se debe haber creado una vista que muestre proyectos, tareas y notificaciones de un usuario.

* **Tama�o mediana (M)**

    - ***Implementar seguridad***
        
        **Descripci�n**:
        Como usuario quiero que solo yo o gente autorizada pueda entrar a mi lista          para proteger mis datos.
        
        **COS**: 
        Controlar la seguridad y el acceso por URL.
        
    - ***Fecha de tarea***
    
        **Descripci�n**:
        Como usuario quiero fijar una fecha en cada tarea para organizar entregas y         el tablero. Fecha inicio y final.
        
        **COS**:
        Quiero una fecha l�mite que cuando la tarea est� cerca de la fecha pueda            cambiar de color.
        Al llegar la fecha l�mite tambi�n cambia de color.
        
    - ***Asignar colores a una tarea***
    
        **Descripci�n**:
        Como usuario quiero poder asignar colores a una tarea para poder visualizar         l�gicamente el proyecto.
        
        **COS**:
        A�adir un color a una tarea y poder visualizar qu� color tiene la misma.
        
    - ***Compartir proyecto***
    
        **Descripci�n**:
        Como usuario quiero compartir un proyecto con otro usuario para poder               trabajar conjuntamente.
        
        **COS**:
        El usuario que ha creado el proyecto puede a�adir usuarios colaboradores al         proyecto.
        Los colaboradores no pueden editar el proyecto.
        
    - ***A�adir comentarios***
        
        **Descripci�n**:
        Como usuario quiero poder a�adir comentarios a una tarea o proyecto en              el que participo para poder expresar una idea u opini�n.
        
        **COS**:
        Se debe poder poner un texto que sea visto por todos los colaboradores de           las tareas o proyectos.
        Un comentario se puede editar o borrar por el usuario que lo ha escrito o          el adminstrador.

* **Tama�o peque�o (S)**
    
    - ***Estado de la tarea***
    
        **Descripci�n**:
        El usuario quiere tener un seguimiento del estado de su tarea (finalizado,         iniciado) para tener una idea de la evoluci�n.

        **COS**:
        El usuario debe poder marcar un estado (iniciado, en proceso, finalizado) en la tarea y cambiarlo.
    
    - ***Buscar tareas***
        
        **Descripci�n**:
        Como usuario quiero buscar tareas para poder encontrarlas m�s f�cilmente.
        
        **COS**:
        A partir de un texto poder filtrar tareas por su descripci�n.
    
    - ***Filtrado tareas***
    
        **Descripci�n**:
        Como usuario quiero filtrar las tareas por tama�o o fecha para organizar o         buscar tareas.
        
        **COS**:
        Tener un filtro que muestre solamente las tareas con la estimaci�n puesta en el     filtro.
    
    - ***Exportar tareas***
    
        **Descripci�n**:
        El usuario quiere poder exportar sus tareas para tenerlas offline.

        **COS**:
        El usuario debe poder expertarlo en un TXT.
    
    - ***Borrar colaboradores proyecto***
    
        **Descripci�n**: 
        Como usuario porpietario de un proyecto quiero poder eliminar a otros miembros.
        
        **COS**:
        Eliminar a un colaborador y verificar que su nombre no aparece en                  colaboradores.

Para organizar nuestro tablero, primero tuvimos que realizar una estimaci�n cada uno de los componentes del grupo para definir definitivamente el tama�o de las tareas. Se crearon las funcionalidades (Feature - FT) y cada una de ellas se descompuso en tareas o tickets (TC-X).

Antes de nada comentar que el tablero es b�sicamente el mismo salvo en una sola funcionalidad que cambi� de tama�o. **Compartir proyecto** era tama�o mediano (M) y se ha pasado a (L). Tambi�n, a parte de modificar la descripci�n como es l�gico para especificar m�s, se han cambiado algunos t�tulos para hacerlos m�s claros.

A continuaci�n se mostrar� el tablero con su descripci�n, sus condiciones y sus tickets.

* **Features (FT)**

   - ***FT-3 Implementar seguridad (M)***
   
        **Descripci�n**:
        Como usuario quiero que solo yo pueda ver o modificar mis datos.
        Como administrador quiero tener acceso a todo el sistema y que un usuario solo pueda ver o modificar sus propios datos.

        **COS**:
        Los datos de un usuario solo podr�n ser vistos o modificados por �l mismo o         por el administrador del sistema.
        Si un usuario intenta acceder a una URL que no le pertenece, ser�         redireccionado a su dashboard principal.
        Las secciones visuales (men�s, botones, etc.) para un usuario ser�n diferentes que para las del administrador. 
        El usuario no tendr� de momento ning�n men� izquierdo, navegar� a trav�s de enlaces o botones.
        El administrador podr� tener acceso a un men� en la izquierda.
    
    - ***FT-4 A�adir comentarios (S)***
    
        **Descripci�n**:
    Como usuario quiero poder a�adir comentarios a un proyecto en el que participo para poder expresar una idea u opini�n.

        **COS**:
        Los comentarios de un proyecto se podr�n a�adir y visualizar en los detalles del proyecto.
        Un comentario se puede editar o borrar por el usuario que lo ha escrito o por el adminstrador del proyecto.

        **Documentaci�n**:
        https://docs.google.com/document/d/1AVhqb8lcuFg3ZX7O15XYHHyKNjKrdctcSBgECCA3S4g/edit
        
    - ***FT-5 Compartir proyecto (L)***
    
        **Descripci�n**:
    Como usuario quiero compartir un proyecto con otro usuario para poder visualizar las tareas del proyecto.

        **COS**:
        El usuario que ha creado el proyecto puede a�adir usuarios colaboradores al proyecto, seleccion�ndolos de una lista de usuarios registrados.
        Los colaboradores no pueden editar la descripci�n del proyecto.
        En la tabla de proyectos debe aparecer tanto sus proyectos como los compartidos.
        
    - ***FT-6 Estado tarea (S)***
    
        **Descripci�n**:
    Como usuario quiero poder marcar una tarea como iniciada o terminada para organizarme mejor.

        **COS**:
        Las tareas al crearse tendr�n un estado por defecto que ser� "sin empezar".
        El estado de la tarea podr� cambiarse en el modificar mediante un selector.
        Los estados pueden ser "iniciada", "terminada" o "sin empezar".
        El estado de la tarea  se visualizar� tanto en los detalles de cada tarea como en el listado.
        
    - ***FT-7 Buscar tareas (S)***
    
        **Descripci�n**:
        Como usuario quiero buscar tareas para poder encontrarlas m�s f�cilmente.

        **COS**:
        A partir de un texto escrito en un buscador se filtrar�n las tareas por su descripci�n.
        La descripci�n de las tareas resultantes deben contener el texto del buscador, no se estrictamente el mismo.

    - ***FT-8 Filtrado tareas (S)***
    
        **Descripci�n**:
        Como usuario quiero filtrar las tareas por estado o fecha para organizar las tareas.

        **COS**:
        Disponer de un filtro de tareas que filtre tanto por estado y color como por fecha de finalizaci�n.
        El filtrado se har� a trav�s del mismo campo buscador para no complicar la tarea.
        Debe permitir buscar por todos los campos que tiene la tarea. Si se agregan nuevos campos, hay que a�adirlos al filtrado.
        
    - ***FT-9 Dashboard (L)***
    
        **Descripci�n**:
    Como usuario quiero un dashboard para poder ver las tareas, proyectos y datos estad�sticos de un vistazo al inicio y saber el estado actual de todo r�pidamente.

        **COS**:
        Al conectarse el usuario su p�gina principal ser� el dashboard.
        En �l aparecer�n los proyectos asociados, sus tareas y ciertos datos informativos.
        
    - ***FT-10 Fecha tareas (M)***
    
        **Descripci�n**:
    Como usuario de una tarea quiero a�adir fecha de finalizaci�n.

        **COS**:
        La fecha de finalizaci�n puede no indicarse (opcional).
        Poder indicar la fecha de finalizaci�n en la inserci�n y en la modificaci�n de la tarea.
        La fecha de finalizaci�n se visualizar� en los detalles de la tarea, teniendo un texto con letra m�s grande que los dem�s datos.
        En el listado de tareas tambi�n se visualizar� la fecha de finalizaci�n.
        
    - ***FT-11 Asignar colores tarea (M)***
    
        **Descripci�n**:
        Como usuario quiero poder asignar colores a una tarea para visualizarla m�s r�pidamente.
        
        **COS**:
        Poder elegir colores de entre un listado est�tico y asignarlos a la tarea. Esta opci�n podr� darse en la inserci�n y en la modificaci�n de la tarea.
        Los colores de cada tarea se visualizar�n tanto en los detalles de cada tarea como en el listado de todas las tareas, en peque�o.
        
    - ***FT-12 Exportar tareas (S)***
    
        **Descripci�n**:
        Como usuario quiero poder exportar mis tareas para tenerlas offline, en mi ordenador.

        **COS**:
        El usuario debe poder exportar sus tareas en un archivo con extensi�n .txt.
        En el archivo aparecer�n todos los datos de cada tarea. Cada tarea estar� separada por un salto de l�nea.

        **Enlace a documento explicativo Google Docs**:
        https://docs.google.com/document/d/16se-t22KXajcM0Z68KIZk7WhwQq7usw-pSANILsgBBI/edit?usp=sharing
        
    - ***FT-13 Borrar colaboradores proyecto (S)***
    
        **Descripci�n**: 
        Como usuario propietario de un proyecto quiero poder eliminar a otros miembros colaboradores.

        **COS**:
        Eliminar a un colaborador y verificar que su nombre no aparece en colaboradores.
        Al eliminar un proyecto, antes tambi�n se tienen que eliminar sus colaboradores. Si no, falla la integridad referencial.
        
    - ***FT-14 Release v1.2***
    
        **Descripci�n**: 
        Una vez terminadas todas las FT-2, crear la Release v1.1 del proyecto.

* **Tickets (TC-X)**

    - ***TIC-3.1 Investigar funcionalidad nativa control acceso sesi�n***
        
        Investigar sobre funcionalidad  sobre controlar acceso por sesi�n.

        **Pruebas funcionales**
        
        Enlace a un googledocs sobre la funcionalidad de la sesi�n en la parte del controller: https://docs.google.com/document/d/1FDOoZAZJZaAyM_Bw80Z1xm_wj7bfND8_FqmD1oijKxE/edit?usp=sharing
    
    - ***TIC-3.2 Aplicar por todo el sistema la seguridad***
        
        Aplicar en todo el c�digo del sistema la investigaci�n realizada en el ticket anterior.

        Asegurarse que bloquea el acceso a todo aquel que intente acceder a una secci�n del programa no permitida.

        Enlace a un googledocs sobre la funcionalidad de la aplicacion de la seguridad en todo el sistema:
        https://docs.google.com/document/d/1-l6Fx9nY2CO9IZJWO9L0nTotm1ZoG9NwIyY_PMnqPwI/edit#heading=h.afdwlbl8wbx8
        
    - ***TIC-4.1 A�adir comentarios***
        
        Los comentarios de un proyecto se podr�n a�adir y visualizar en los detalles del proyecto.
        Un comentario se puede editar o borrar por el usuario que lo ha escrito o por el adminstrador del proyecto.
        
    - ***TIC-5.1 El propietario de un proyecto puede a�adir colaboradores***
        
        En la clase Proyecto a�adir una lista de colaboradores.
        En el editar de Proyecto a�adir el selector de usuarios.
        
    - ***TIC-5.2 Los usuarios pueden ver los proyectos en los que son colaboradores***
        
        Cambiar la seguridad por sesi�n para que un usuario pueda entrar a la vista de los proyectos en los que es colaborador, pero no al editar.
        En el listado de Proyectos, un usuarios tiene que poder ver los proyectos que ha creado y tambi�n los que es colaborador.

        Enlace a un googledocs sobre la vista de los proyectos donde los usuarios pueden ver tanto sus proyectos como en los cuales colaboran: 
        https://docs.google.com/a/gcloud.ua.es/document/d/1gHIalRQ6jLa91o_e1oJuCmZUrjV_QNA9tRpkwOMfnoY/edit?usp=sharing
        
    - ***TIC-5.3 A�adir descripci�n al proyecto***
    
        Se puede meter desde el editar, as� solamente el creador del proyecto puede editar la descripci�n.
        En el ver solamente se visualizar�.
        
    - ***TIC-5.4 Los colaboradores de un proyecto pueden asignar tareas***
        
        Cambiar el asignar tarea y la tabla de tareas del editar al ver de     proyecto, as� los colaboradores podr�n tambi�n asignar tareas.
        
    - ***TIC-6.1 Estado tarea***
        
        Las tareas al crearse tendr�n un estado por defecto que ser� "sin empezar".
        El estado de la tarea podr� cambiarse en el modificar mediante un selector.
        Los estados pueden ser "iniciada", "terminada" o "sin empezar".
        El estado de la tarea  se visualizar� tanto en los detalles de cada tarea como en el listado.
        
    - ***TIC-7.1 B�squeda tareas***
    
        En el listado de tareas a�adir un cuadro de texto para permitir buscar las tareas.
        En este ticket se habilitar� que se pueda buscar por id y por descripci�n.

        **Revisi�n**
        Ticket m�s largo de lo esperado. Se vio la posibilidad de incluir el plugin         basado en html/jquery Datatable que agiliza (una vez implementado) la          navegaci�n y la gesti�n de una tabla con datos: permite b�squedas, filtros,        exportaciones, etc.
    El desarrollo ha sido costoso pero finalmente se ha conseguido.
    
    - ***TIC-8.1 Filtrado de campos de Tarea***
    
        Permitir filtrar todos los campos de tareas que aparecen en el listado de tareas del usuario.
        Utilizando el buscador generado en la FT-7, se puede utilizar para filtrar por distintos campos. Ampliar funcionamiento a dem�s campos tales como: estado, fecha de finalizaci�n o color.
        
    - ***TIC-9.1 Incluir dise�o y funcionamiento est�tico***
    
        Incluir un dise�o que mejore la presencia de la primera p�gina que ve el usuario. Ya est� implantada la plantilla, simplemente mejorarlo con elementos m�s vistosos.

        Incluir enlaces y presentaci�n de forma est�tica, que los enlaces no lleven a ning�n sitio de momento.

        **Plantillas a valorar**:
        http://wrapbootstrap.com/preview/WB019K3P8
        http://themedesigner.in/demo/matrix-admin/index.html
        https://blackrockdigital.github.io/startbootstrap-sb-admin/

        **Plugins interesantes**:
        http://canvasjs.com/samples/dashboards/
        
    - ***TIC-9.2 Avance de tareas y proyectos del usuario***
    
        Que aparezca un avance (en forma de tabla) de las tareas y proyectos (mostrar tres de cada - si los tuviera).

        Podr� navegar desde esa tabla-avance al listado de todas las tareas y al listado de todos los proyectos.

    - ***TIC-9.3 Elementos informativos***
    
        Mostrar mensajes que aporten valor informativo al usuario: 
        N� Tareas abiertas (Sin Empezar, Iniciada) y n� de tareas acabadas.
        Proyecto con m�s comentarios y m�s colaboradores.
        
    - ***TIC-9.4 Dashboard como p�gina principal***
    
        Establecer la p�gina de dashboard como p�gina principal. 
        
        Verificar que al loguearse un usuario entra a la p�gina de dashboard.
        
    - ***TIC-10.1 Creaci�n fecha de finalizaci�n de tarea***
    
        A�adir fecha de finalizaci�n.
        
        Validar la fecha con javascript para que tenga formato correcto y que sea mayor que la fecha actual.
        
    - ***TIC-10.2 Modificaci�n de fecha, demostraci�n.***
    
        La fecha de finalizaci�n se visualizar� en los detalles de la tarea, teniendo un texto con letra m�s grande que los dem�s datos.
        
        En el listado de tareas tambi�n se visualizar� la fecha de finalizaci�n.
        
        Modificaci�n de la fecha de finalizacion.
        
    - ***TIC-11.1 A�adir color a la tarea***
        
        A�adir el color en la entidad Tarea.
        
        Que se pueda elegir el color tanto en el crear como en el editar de la tarea.
        
        El color es opcional.
        
    - ***TIC-11.2 Visualizar el color de la tarea en el listado de tareas***
        
        Lo ideal ser�a que se viera el color en la vista en lugar de verse el c�digo hexadecimal del color.
        
    - ***TIC-12.1 Crear enlaces y descargar archivo txt***
    
        En esta tarea ser� necesario un cierto tiempo de investigaci�n sobre c�mo trabaja Play con archivos (aunque imaginamos que ser� propio de Java, pero puede tener cierta configuraci�n m�s al tener que descargar desde el servidor).

        Se crear�n los enlaces de descarga. En las vistas se establecer�n enlaces de descarga. Estos enlaces no ir�n a ning�n sitio todav�a.

        Una vez investigado, se desarrollar� la utilidad de que al pinchar en alg�n enlace, empiece a descargar el archivo.

        El archivo contendr�, tal y como indica la Feature, los datos de cada tarea y la separaci�n entre tareas ser� con un salto de l�nea.
        
    - ***TIC-13.1 Borrar colaboradores del proyecto***
    
        Se permite borrar los colaboradores de un proyecto.
        
    - ***TIC-14.1 Documentaci�n sprint 1***
    
        Realizar la documentaci�n requerida en la pr�ctica 4 (sprint 1)

Si tenemos en cuenta el tama�o en puntos de cada ticket (se detalla al principio de este documento), quedar�a as� para cada uno de nosotros:

* **Ouadi Chamit (7 puntos)**

    - ***FT-3 (M) (2 puntos)***
    - ***FT-5 (L) (2 puntos)***
    - ***FT-6 (S) (1 punto)***
    - ***FT-10 (M) (2 puntos)***
    
* **Benjam�n Pamies Cartagena (6 puntos)**

    - ***FT-4 (S) (1 punto)***
    - ***FT-5 (L) (2 punto)***
    - ***FT-11 (M) (2 puntos)***
    - ***FT-13 (S) (1 punto)***
    
* **Manuel Garc�a Men�rguez (7 puntos)**

    - ***FT-7 (S) (1 punto)***
    - ***FT-8 (S) (1 punto)***
    - ***FT-9 (L) (4 puntos)***
    - ***FT-12 (S) (1 punto)***

* ##### Capturas de la web

Se muestran algunas capturas de las secciones desarroladas y/o mejoradas en este sprint.

* **Dashboard**

![GitHub Logo](/docs/dashboard1.png)

![GitHub Logo](/docs/dashboard2.png)

* **Creaci�n de tarea con nuevos par�metros**

Los par�metros nuevos son: fecha de finalizaci�n, estado y color. �ste �ltimo se muestra un selector de colores en hexadecimal. Para ello se ha utilizado un plugin javascript llamado JSColor.

![GitHub Logo](/docs/parametrosTareas.png)

* **B�squeda, filtrado y exportar tareas**

Se ha realizado mediante el plugin Datatable realizado en HTML, CSS y Jquery.

La carga y manejo de los datos es mediante un m�todo en AJAX que accede aun m�todo de un Controller y devuelve una respuesta al manejador de Datatable para cargar los datos de forma paginada (10 registros por p�gina).

![GitHub Logo](/docs/busquedaTareas.png)

* **Comentarios del proyecto**

Un usuario puede a�adir una descripci�n y tambi�n realizar comentarios en el proyecto.

![GitHub Logo](/docs/descripcionProyecto.png)

* **Asignar tareas y colaboradores al proyecto**

Un usuario puede, mediante la vista de edici�n del proyecto, asignar tareas propias al proyecto y tambi�n asignar colaboradores. Los colaboradores son usuarios registrados en el sistema.

![GitHub Logo](/docs/asignarTareaProyecto.png)

![GitHub Logo](/docs/asignarColaborador.png)

* ##### Metodolog�a seguida

La metodolog�a seguida en este sprint ha sido la de reunirnos o presencialmente o por Slack los tres componentes del grupo.

En el tablero "En espera", se han ido definiendo las funcionalidades (FT) y cuando uno de los componentes elige esa feature, lo descompone en 1 o varios tickets.

Se intentan documentar lo mejor posibles los tickets y pasa al tablero "Seleccionados" dicho ticket (la FT permanecer� en el tablero "En espera" hasta que termine toda su funcionalidad). Dicho tablero solo puede tener un m�ximo de 5 tickets (WIP=5). En ning�n momento del sprint se ha conseguido llenar.

Cuando se empieza a desarrollar, se pasa  el ticket al tablero "En desarrollo", se crea una rama y se sube al repositorio para que est� visible. Este punto en los �ltimos tickets no se ha cumplido al 100%, en su lugar, se ha trabajado en local y cuando se ha tenido una versi�n m�s o menos terminada del ticket, se sube para ya realizar el pull request.

Cuando la rama necesita mezclarse, se a�ade a "En desarrollo - Done" y posteriormente al tablero "Integraci�n". Dicho tablero puede tener un m�ximo de 2 tickets (WIP=2). El resto permanecer�n en un estado anterior "En desarrollo - Done" esperando a que se mezclen dichos pull requests. **Ver �ltimo punto** de Retrospectiva para ver qu� inconvenientes nos ha presentado este punto.

Una vez se mezcla el pull request en la rama master, se a�ade el ticket al tablero "Integraci�n - Done". Posteriormente, al tablero "Pruebas funcionales". Lo ideal ser�a que cuando est� en este estado "Pruebas funcionales", todos los componentes implicados en la aceptaci�n del pull request (en concreto, 2 seg�n hemos establecido) prueben el, pull request que acaba de mezclarse para probarlo en la misma aplicaci�n. Esto no ha sido siempre as�, si no que se ha optado por un modelo algo m�s "en cascada" en este punto. Se ha aceptado todo pull request (con una m�nima revisi�n) y si alguien en alg�n momento ha detectado un error con un pull request antiguo, se le ha comentado al responsable del ticket, para que lo tenga en cuenta y lo solucione posteriormente, aunque fuera con otro commit y estando desarrollando otra tarea.

* ##### Informes reuniones scrum

Los componentes del grupo hemos simulado dos reuniones en scrum (se han hecho m�s, pero documentadas solo �stas).

Hemos detallado al m�ximo posible lo que se habl� en esas reuniones:

* **Reuni�n diaria 1**

**Fecha:  Mi�rcoles 30 de noviembre: 13:15 a 14:15**

Algunas tareas *off-topic* de las t�picas preguntas propias de un scrum diario:

- Se a�ade el n�mero de feature a cada tarjeta.

	FT-X Nombre funcionalidad
	
- Se a�aden las etiquetas necesarias a cada feature.

	- Amarilla, verde o azul seg�n corresponda su tama�o (esto ya estaba de antemano)
	- Amarilla para indicar que es "Feature"
	- Verde junto con el nombre de cada funcionalidad. Aqu� comenzamos creando una etiqueta de cada color en vez de verde, pero vimos
	que tener tres colores diferentes para cada funcionalidad puede ser un caos, adem�s que luego se repetir�an puesto que hay m�s funcionalidades que colores disponibles.
	As� que se ha dejado tal y como se hizo en la pr�ctica 3: Color verde y mismo nombre que funcionalidad.

Preguntas:

- Ouadi Chamit

    - �Qu� he hecho?: He investigado acerca la funcionalidad del acceso por sesi�n en Play Framework. (FT-3 Implementar seguridad)
    
	 - �Qu� voy a hacer?: Empezar con el c�digo a hacer pruebas de lo encontrado por Internet.

- Benjam�n Pamies Cartagena

	 - �Qu� he hecho?: Averiguar c�mo crear relaciones entre clases, y c�mo se crea la tabla en la base de datos respecto al ticket TIC-4.1 A�adir comentarios
	  
    - �Qu� voy a hacer? Implementar la tarea de crear un comentario sobre un proyecto.
			
- Manuel Garc�a Men�rguez

	 - �Qu� he hecho?: Investigar c�mo exportar fichero txt en play respecto al ticket TIC-12.1 Detalles tarea y exportar tareas txt.
	    
    - �Qu� voy a hacer?: Implementar la tarea.

* **Reuni�n diaria 2**

**Fecha:  Mi�rcoles 14 de diciembre: 13:15 a 14:15**

Preguntas:

- Ouadi Chamit

    - �Qu� he hecho?: En el listado de proyectos de un usuario, adem�s de aparecer los proyectos que ha creado, ahora tambi�n tienen que aparecer los proyectos en los que le han a�adido como colaborador.
Los proyectos en los que es colaborador en la columna ACCIONES s�lo debe aparecer el icono de la lupa para entrar en la vista de detalles, los botones de editar y borrar se tienen que ocultar para que no pueda pulsarlos.

    - �Qu� voy a hacer?: Implementar la tarea de permitir al usuario poder asignar una tarea a un proyecto, tocar la parte del controller y la capa dao y services, comprobar si se asigna la tarea.

- Benjam�n Pamies Cartagena

	- �Qu� he hecho?: Crear el Service y la relaci�n N:M Colaborador-Proyecto.
	  
    - �Qu� voy a hacer? Modificar el modelo Usuario y el modelo Proyecto para que pueda tener una relaci�n muchos a muchos N:M. Implementar y probar si se a�ade un colaborador a un proyecto desde el usuario propietario del proyecto.
			
- Manuel Garc�a Men�rguez

	 - �Qu� he hecho?: He realizado los dos primeros tickets de la FT-9 Dashboard. Los puntos ya permiten visualizar el dashboard gr�ficamente y la tabla de tareas y proyectos.
	    
    - �Qu� voy a hacer?: Quedan dos �ltimos tickets de la FT-9 Dashboard. Mostrar�n m�s informaci�n en la vista como el n� de tareas abiertas y cerradas, los proyectos con m�s comentarios o m�s colaboradores. Tambi�n intentar� hacer para ma�ana que el dashboard sea la p�gina de inicio, que es el ticket 9.4.

* ##### Retrospectiva y conclusiones del sprint

Echamos la vista atr�s para analizar todo lo ocurrido en este sprint y enumeramos algunos puntos que han ido bien y otros que se podr�an mejorar en siguientes sprints.
* **Detalles que s� han funcionado**

Hemos estado bien sincronizados gracias a la comunicaci�n de Slack con Github y a su vez Trello con Github.

Trello ha sido muy buen planificador de tareas, a partir de ello podemos tener una vision global de c�mo ha ido el desarrollo de las funcionalidades, as� como ir viendo mediante la navegaci�n entre tableros los estados de las tareas.

Cada uno cuando ha visto algo por corregir o mejorar, hemos dejado "nota" en el chat de Slack y cada uno cuando ha podido lo ha visto y solventado. La comunicaci�n es muy din�mica.

En cuanto al desarrollo, ha sido llevado pr�cticamente al d�a.

* **Detalles por mejorar**

Una de las cosas a mejorar ha sido un error por nuestra parte por establecer la aceptaci�n del pull request a 2 personas (somos 3). Si uno de los que tiene que aceptarlo no est� disponible en el momento en el que se crea el pull request, eso genera retraso. Esto ha provocado algunos retrasos en la mezcla con la rama master y ciertas dependencias en algunas tareas (no se puede iniciar una hasta que no est� la otra integrada). 

Respecto a Trello, se ha echado en falta una funcionalidad y no es otra que la de permitir agrupar tareas. El funcionamiento de colores ha sido algo confuso.

Por tanto, para el siguiente sprint y depende de c�mo se avance en el proyecto, podr�a valorarse el utilizar otro gestor de tareas m�s avanzado.



