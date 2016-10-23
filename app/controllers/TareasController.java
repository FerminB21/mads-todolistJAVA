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

    @Inject FormFactory formFactory;

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
        Logger.debug("Tarea a grabar: " + tarea.toString());
        tarea = TareasService.crearTareaUsuario(tarea.descripcion, idUsuario);
        flash("gestionaTarea", "La tarea se ha grabado correctamente");
        Logger.debug("Usuario guardado correctamente: " + tarea.toString());
        return redirect(routes.TareasController.listaTareas(idUsuario));
    }
}