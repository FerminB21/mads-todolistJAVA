package controllers;

import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;

import models.*;
import services.*;

import views.html.*;

import javax.inject.*;
import java.util.List;

public class TareasController extends Controller {

    @Inject
    FormFactory formFactory;

    @Transactional(readOnly = true)
    public Result listaTareas(Integer idUsuario) {
        String aviso = flash("aviso");
        String error = flash("error");
        Logger.debug("Mensaje de aviso: " + aviso);
        Logger.debug("Mensaje de error: " + error);
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        if (usuario == null) {
            return notFound("Usuario no encontrado");
        } else {
            List<Tarea> tareas = TareasService.listaTareasUsuario(idUsuario);
            return ok(listaTareas.render(tareas, usuario, aviso, error));
        }
    }

    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevaTarea(Integer idUsuario) {
        return ok(formCreacionTarea.render(formFactory.form(Tarea.class), idUsuario, ""));
    }

    @Transactional
    // Añade una nueva tarea en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    //PARA EJECUTAR ESTE MÉTODO HA SIDO NECESARIO REALIZAR: activator run
    //Daba error como si no detectara que el campo descripcion de Tarea no existiera
    public Result grabaNuevaTarea(Integer idUsuario) {

        Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
        if (tareaForm.hasErrors()) {
            return badRequest(formCreacionTarea.render(tareaForm, idUsuario, "Hay errores en el formulario"));
        }
        Tarea tarea = tareaForm.get();
        Integer estimacion = Integer.parseInt(tareaForm.form().bindFromRequest().get("estimacion"));
        tarea.estimacion = estimacion;
        Logger.debug("Tarea a grabar: " + tarea.toString());
        tarea = TareasService.crearTareaUsuario(tarea, idUsuario);
        flash("gestionaTarea", "La tarea se ha grabado correctamente");
        Logger.debug("Tarea guardada correctamente: " + tarea.toString());
        return redirect(routes.TareasController.listaTareas(idUsuario));
    }

    @Transactional
    public Result formularioEditaTarea(Integer idTarea, Integer idUsuario) {
        //Cargamos vacío el form
        Form<Tarea> tareaForm = formFactory.form(Tarea.class);
        //Obtenemos de la base de datos la tarea
        Tarea tarea = TareasService.findTareaUsuario(idTarea);
        //Cargamos en el form los datos del usuario
        tareaForm = tareaForm.fill(tarea);
        //Retornamos a la vista los datos del usuario en el form
        return ok(formModificacionTarea.render(tareaForm, idUsuario, ""));
    }

    @Transactional
    public Result grabaTareaModificada(Integer idUsuario) {
        Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
        if (tareaForm.hasErrors()) {
            return badRequest(formModificacionTarea.render(tareaForm, idUsuario, "Hay errores en el formulario"));
        }

        //Recuperamos los datos de la tarea
        Tarea tarea = tareaForm.get();
        //Comprobamos que el usuario existe (evitamos problemas de referencias)
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        if (usuario != null) {
            tarea.usuario = usuario;
            Logger.debug("Tarea a grabar: " + tarea.toString());
            tarea = TareasService.modificaTareaUsuario(tarea);
            flash("gestionaTarea", "La tarea se ha modificado correctamente (modificar)");
            Logger.debug("Tarea guardada correctamente (modificar): " + tarea.toString());
            return redirect(routes.TareasController.listaTareas(idUsuario));
        } else {
            return badRequest(formModificacionTarea.render(tareaForm, idUsuario, "Error inesperado. Vuelva a intentarlo"));
        }
    }

    /**
     * Elimina la tarea
     *
     * @param idTarea,
     * @return Result
     */
    @Transactional
    public Result borraTarea(int idTarea, int idUsuario) {
        Tarea tarea = TareasService.findTareaUsuario(idTarea);
        //Si se ha borrado recargamos página
        if (TareasService.deleteTarea(idTarea)) {
            return ok("Tarea borrada con éxito.");
        } else { //Si no, devolvemos error
            return badRequest("Tarea no se ha podido eliminar.");
        }

    }
}
