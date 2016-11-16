# Documentación técnica

### - Release v1.1

* ##### Funcionalidades añadidas

    * Es posible asignar una estimación de tamaño a cada tarea. Se permite: Pequeño, Mediano o Grande.

    * Al loguearse un usuario registrado ahora redireccionará a su listado de tareas.

    * Al loguearse un usuario se creará una sesión que servirá para decidir por qué páginas navegar. El filtrado se ha realizado de momento a nivel de botones o enlaces (si tiene sesión, se mostrará una cosa y si no tiene (usuario admin), se mostrará otra cosa). No se descarta implementar filtrado por rutas en un futuro.

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