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



    // Devuelve un formulario para crear un nuevo proyecto
    public Result formularioNuevoProyecto(Integer idUsuario) {
        return ok(formCreacionProyecto.render(formFactory.form(Proyecto.class), idUsuario, ""));
    }

    @Transactional
    // Añade una nuevp proyecto en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    //PARA EJECUTAR ESTE MÉTODO HA SIDO NECESARIO REALIZAR: activator run
    //Daba error como si no detectara que el campo nombre del proyecto no existiera
    public Result grabaNuevoProyecto(Integer idUsuario) {

        Form<Proyecto> proyectoForm = formFactory.form(Proyecto.class).bindFromRequest();
        if (proyectoForm.hasErrors()) {
            return badRequest(formCreacionProyecto.render(proyectoForm, idUsuario, "Hay errores en el formulario"));
        }
        Proyecto proyecto = proyectoForm.get();


        proyecto = ProyectosService.crearProyectoUsuario(proyecto, idUsuario);
        flash("gestionaProyecto", "el proyecto se ha grabado correctamente");
        Logger.debug("Proyecto guardado correctamente: " + proyecto.toString());
        
        return redirect(routes.ProyectosController.listaProyectos(idUsuario));
    }

    @Transactional
    public Result formularioEditaProyecto(Integer idProyecto, Integer idUsuario) {
        //Cargamos vacío el form
        Form<Proyecto> proyectoForm = formFactory.form(Proyecto.class);
        //Obtenemos de la base de datos la proyecto
        Proyecto proyecto = ProyectosService.findProyectoUsuario(idProyecto);
        //Cargamos en el form los datos del usuario
        proyectoForm = proyectoForm.fill(proyecto);
        //Retornamos a la vista los datos del usuario en el form
        return ok(formModificacionProyecto.render(proyectoForm, idUsuario, ""));
    }

    @Transactional
    public Result grabaProyectoModificado(Integer idUsuario) {
        Form<Proyecto> proyectoForm = formFactory.form(Proyecto.class).bindFromRequest();
        if (proyectoForm.hasErrors()) {
            return badRequest(formModificacionProyecto.render(proyectoForm, idUsuario, "Hay errores en el formulario"));
        }

        //Recuperamos los datos de la proyecto
        Proyecto proyecto = proyectoForm.get();
        //Comprobamos que el usuario existe (evitamos problemas de referencias)
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        if(usuario != null){
            proyecto.usuario=usuario;
            Logger.debug("proyecto a grabar: " + proyecto.toString());
            proyecto = ProyectosService.modificaProyectoUsuario(proyecto);
            flash("gestionaproyecto", "La proyecto se ha modificado correctamente (modificar)");
            Logger.debug("proyecto guardada correctamente (modificar): " + proyecto.toString());
            return redirect(routes.ProyectosController.listaProyectos(idUsuario));
        }
        else{
            return badRequest(formModificacionProyecto.render(proyectoForm, idUsuario, "Error inesperado. Vuelva a intentarlo"));
        }

    }

    /**
     * Elimina el proyecto
     * @param idProyecto,
     * @return Result
     */
    @Transactional
    public Result borraProyecto(int idProyecto, int idUsuario) {
        Proyecto proyecto = ProyectosService.findProyectoUsuario(idProyecto);
        //Si se ha borrado recargamos página
        if(ProyectosService.deleteProyecto(idProyecto)){
            return ok("Proyecto borrado con éxito.");
        }
        else{ //Si no, devolvemos error
            return badRequest("Proyecto no se ha podido eliminar.");
        }

    }


}
