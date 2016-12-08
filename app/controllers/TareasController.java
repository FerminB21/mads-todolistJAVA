package controllers;

import models.Tarea;
import models.Usuario;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;

import models.*;
import services.*;

import views.html.*;

import javax.inject.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.TareasService;
import services.UsuariosService;

import views.html.*;

import javax.inject.Inject;
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

        String variable=session().get("usuario");

        if (variable!=null){

            Usuario usuario = UsuariosService.findUsuario(idUsuario);
            if (usuario == null) {
                return notFound("Usuario no encontrado");
            } else if(!usuario.login.equals(variable)){

                  return notFound("No autorizado a acceder a zonas de otros usuarios");
            } else {
                List<Tarea> tareas = TareasService.listaTareasUsuario(idUsuario);
                return ok(listaTareas.render(tareas, usuario, aviso, error));
            }
        } else {
            return unauthorized("hello, debes iniciar session");
        }
    }

    @Transactional
    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevaTarea(Integer idUsuario) {
      String variable=session().get("usuario");
        if (variable!=null){
          Usuario usuario = UsuariosService.findUsuario(idUsuario);
          if (usuario == null) {
              return notFound("Usuario no encontrado");
          }else if(!usuario.login.equals(variable)){

                return notFound("No autorizado a acceder a zonas de otros usuarios");
         }else{
            return ok(formCreacionTarea.render(formFactory.form(Tarea.class), idUsuario, ""));
         }
        }else{
            return unauthorized("hello, debes iniciar session");
        }
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

        String variable=session().get("usuario");
        if (variable!=null){
            //Cargamos vacío el form
            Form<Tarea> tareaForm = formFactory.form(Tarea.class);

            //parte añadida
            //obtenemos la tarea si pertenece al usuario con su id
            Tarea tarea=TareasService.findTareaPorUsuario(idTarea, idUsuario);

            //Cargamos en el form los datos del usuario
            tareaForm = tareaForm.fill(tarea);
            //Retornamos a la vista los datos del usuario en el form
            return ok(formModificacionTarea.render(tareaForm, idUsuario, ""));
        } else {

          return unauthorized("hello, debes iniciar session");
        }
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
     * @param idTarea, idUsuario
     * @return Result
     */
    @Transactional
    public Result borraTarea(int idTarea, int idUsuario) {

        String variable=session().get("usuario");
        if (variable!=null){
          Tarea tarea = TareasService.findTareaUsuario(idTarea);
          //Si se ha borrado recargamos página
          if (TareasService.deleteTarea(idTarea)) {
              return ok("Tarea borrada con éxito.");
          } else { //Si no, devolvemos error
              return badRequest("Tarea no se ha podido eliminar.");
          }
        }else{
          return unauthorized("hello, debes iniciar session");
        }
    }

    @Transactional
    public Result detalleTarea(int idTarea, int idUsuario) {
        Tarea tarea = TareasService.findTareaUsuario(idTarea);
        return ok(detalleTarea.render(tarea));
    }

    /**
     * Exporta una sola tarea en un fichero con extensión .txt
     * @param idTarea, idUsuario
     * @return File
     */
    @Transactional
    public Result exportarTarea(int idTarea, int idUsuario) {
        String nombreFichero = "tarea.txt";
        File archivo = new File(nombreFichero);

        //Si ya existe, lo borramos
        if(archivo.exists()) {
            archivo.delete();
        } else { //Si no, escribimos los datos de la tarea
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(archivo));
                Tarea tarea = TareasService.findTareaUsuario(idTarea);
                bw.write(tarea.toString()+"\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response().setContentType("application/x-download");
        response().setHeader("Content-disposition","attachment; filename="+nombreFichero);
        return ok(archivo);
    }

    /**
     * Exporta todas las tareas del usuario pasado como parámetro en un fichero con extensión .txt
     * @param idUsuario
     * @return File
     */
    @Transactional
    public Result exportarTareas(int idUsuario) {
        String nombreFichero = "listaTareas.txt";
        File archivo = new File(nombreFichero);

        //Si ya existe, lo borramos
        if(archivo.exists()) {
            archivo.delete();
        } else { //Si no, escribimos la lista de tareas
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(archivo));
                List<Tarea> tareas = TareasService.listaTareasUsuario(idUsuario);
                for(Tarea tarea: tareas){
                    bw.write(tarea.toString()+"\n");
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response().setContentType("application/x-download");
        response().setHeader("Content-disposition","attachment; filename="+nombreFichero);
        return ok(archivo);
    }
}
