# Documentación técnica

### - Release v1.1

* ##### Funcionalidades añadidas

    * Es posible asignar una estimación de tamaño a cada tarea. Se permite: Pequeño, Mediano o Grande.

    * Al loguearse un usuario registrado ahora redireccionará a su listado de tareas.

    * Al loguearse un usuario se creará una sesión que servirá para decidir por qué páginas navegar. El filtrado se ha realizado de momento a nivel de botones o enlaces (si tiene sesión, se mostrará una cosa y si no tiene (usuario admin), se mostrará otra cosa). No se descarta implementar filtrado por rutas en un futuro.
    
    * CRUD de proyectos y asignación de tareas. Se permite añadir proyectos a un usuario. Un proyecto está compuesto por su nombre y un conjunto de tareas. Dichas tareas las elige el usuario mediante un panel (editando el proyecto).
    
    * A un proyecto pueden asociarse tareas o eliminarse de él. La tarea que se borre NO se borra del sistema, simplemente queda libre del proyecto y podrá asignarse a otro o no asignarse a ninguno.

* ##### Cambios relevantes realizados en el código

    * La estimación de tareas se ha realizado creando una entidad Enum llamada `EstimacionTareaEnum`. Dicho Enum tiene los siguientes valores:

        * SIN_ESTIMACION(0, "")

        * PEQUEÑO(1, "Pequeño")

        * MEDIANO(2, "Mediano")

        * GRANDE(3, "Grande")

    * Se ha realizado un cambio necesario en `TareasService`. Para crear un tarea a un usuario (método `crearTareaUsuario`), recibía como parámetros, la descripción de la tarea y el id del usuario. Esto no es cómodo, ya que si se amplía la clase `Tarea`, como en este caso que nos encontramos, nos tocaría cambiar el método y todas sus llamadas a lo largo del proyecto y añadir los nuevos campos. Con el cambio de ahora la función recibirá un objeto `Tarea` (que podrá contener el nº de atributos que sean y la id del usuario). Ejemplo:

        * Llamada anterior:

        `crearTareaUsuario`(String descripcion, Integer usuarioId)

        * Llamada actual:

        `crearTareaUsuario`(Tarea tarea, Integer usuarioId)
    
    * El código desarrollado para el CRUD de proyectos es prácticamente igual que para tareas. El mayor toque de cambio es en las relaciones ya que se compone de una relación 0:N con tareas y 1:1 con usuario.