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

public class ProyectosController extends Controller {

    @Inject FormFactory formFactory;

    @Transactional(readOnly = true)
    // Devuelve una página con la lista de proyectos
    public Result listaProyectos(Integer idUsuario) {
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        if (usuario == null) {
            return notFound("Usuario no encontrado");
        } else {
            List<Proyecto> proyectos = ProyectosService.listaProyectosUsuario(idUsuario);
            return ok(listaProyectos.render(proyectos, usuario));
        }
    }



    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevoProyecto(Integer idUsuario) {
        return ok(formCreacionProyecto.render(formFactory.form(Proyecto.class), idUsuario, ""));
    }

    @Transactional
    // Añade una nueva tarea en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    //PARA EJECUTAR ESTE MÉTODO HA SIDO NECESARIO REALIZAR: activator run
    //Daba error como si no detectara que el campo descripcion de Tarea no existiera
    public Result grabaNuevoProyecto(Integer idUsuario) {

        Form<Proyecto> proyectoForm = formFactory.form(Proyecto.class).bindFromRequest();
        if (proyectoForm.hasErrors()) {
            return badRequest(formCreacionProyecto.render(proyectoForm, idUsuario, "Hay errores en el formulario"));
        }
        Proyecto proyecto = proyectoForm.get();


        proyecto = ProyectosService.crearProyectoUsuario(proyecto, idUsuario);
        flash("gestionaTarea", "La tarea se ha grabado correctamente");
        Logger.debug("Tarea guardada correctamente: " + proyecto.toString());
        //return redirect(routes.TareasController.listaTareas(idUsuario));
        return redirect(routes.ProyectosController.listaProyectos(idUsuario));
    }


}
