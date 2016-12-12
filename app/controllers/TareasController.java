package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Tarea;
import models.Usuario;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;

import models.*;
import play.libs.Json;
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
import java.util.Map;

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
        tarea.estimacion = Integer.parseInt(tareaForm.form().bindFromRequest().get("estimacion"));
        tarea.color = tareaForm.form().bindFromRequest().get("color");
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
        //Logger.debug("Estado tarea: " + tarea.estado.toString());
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

    /**
     * Busca tareas por algún parámetro
     * @param idUsuario
     * @return Page
     */
    @Transactional
    public Result busquedaTareas(int idUsuario) {

        Map<String, String[]> params = request().queryString();

        //El total registros será el total de las tareas del usuario
        Integer totalRegistros = TareasService.listaTareasUsuario(idUsuario).size();
        String filter = params.get("search[value]")[0];

        Integer tamanoPagina = Integer.valueOf(params.get("length")[0]);
        Integer inicio = Integer.valueOf(params.get("start")[0]);

        //Parámetros de ordenación (por defecto por id)
        String sortBy = "id";

        String order = params.get("order[0][dir]")[0];

        switch (Integer.valueOf(params.get("order[0][column]")[0])) {
            case 0 :  sortBy = "id"; break;
            case 1 :  sortBy = "color"; break;
            case 2 :  sortBy = "descripcion"; break;
            case 3 :  sortBy = "estado"; break;
            case 4 :  sortBy = "fecha finalización"; break;
        }

        //Lanzamos la búsqueda completa (se busca por todos los campos
        List<Tarea> tareasPage = TareasService.busquedaTareasUsuario(idUsuario, filter, sortBy, order, inicio, tamanoPagina);

        //Si hay filtro, mostramos el total de la consulta del filtro
        Integer totalRegistrosMostrados;
        if(!filter.equals(""))
            totalRegistrosMostrados = tareasPage.size();
        else //Si no, mostramos el total completo
            totalRegistrosMostrados = totalRegistros;

        /**
         * Construct the JSON to return
         */
        ObjectNode result = Json.newObject();

        result.put("draw", Integer.valueOf(params.get("draw")[0]));
        result.put("recordsTotal", totalRegistros);
        result.put("recordsFiltered", totalRegistrosMostrados);

        ArrayNode an = result.putArray("data");

        //Recorremos los objetos para rellenarlos en el Datatable
        for (Tarea tarea : tareasPage) {
            ObjectNode row = Json.newObject();
            row.put("0", tarea.id);
            row.put("1", "<div style='width: 100%; background-color: #"+tarea.color+"'>#"+tarea.color+"</div>");
            row.put("2", tarea.descripcion);
            row.put("3", tarea.tareaTieneEstado());
            row.put("4", tarea.tieneFechaFinalizacion());
            row.put("5", routes.TareasController.detalleTarea(tarea.id, idUsuario).absoluteURL(request()));
            row.put("6", routes.TareasController.formularioEditaTarea(tarea.id, idUsuario).absoluteURL(request()));
            row.put("7", routes.TareasController.borraTarea(tarea.id, idUsuario).absoluteURL(request()));
            an.add(row);
        }
        return ok(result);
    }
}
