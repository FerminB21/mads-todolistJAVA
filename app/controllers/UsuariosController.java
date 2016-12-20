package controllers;

import java.util.List;
import javax.inject.*;

import play.*;
import play.mvc.*;

import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;
import play.mvc.Http.Session;

import views.html.*;

import services.*;
import models.*;

public class UsuariosController extends Controller {

    @Inject
    FormFactory formFactory;

    @Transactional(readOnly = true)
    // Devuelve una página con la lista de usuarios
    public Result listaUsuarios() {

        // Obtenemos el mensaje flash guardado en la petición
        // por el controller grabaUsuario
        String mensaje = flash("gestionaUsuario");
        Logger.debug("valor de la sesion  en listar usuario"+session().get("usuario"));

        String variable=session().get("usuario");

        if (variable!=null){
           if (variable.equals("admin")){
            List<Usuario> usuarios = UsuariosService.findAllUsuarios();
            return ok(listaUsuarios.render(usuarios, mensaje));
          }else{
               return unauthorized("hello, you are not admin");
               //return notFound("Esta tarea es gestion del administrador");
          }
        }else{
               return unauthorized("hello, debes iniciar session");
        }
    }

    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevoUsuario() {
      String variable=session().get("usuario");
      if (variable!=null){
         if (variable.equals("admin")){
        return ok(formCreacionUsuario.render(formFactory.form(Usuario.class), ""));
      }else{
            return unauthorized("hello, you are not admin");
          //return notFound("Esta tarea es gestion del usuario");
        }
      }else{
            return unauthorized("hello, debes iniciar session");
      }
    }

    @Transactional
    // Añade un nuevo usuario en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    public Result grabaNuevoUsuario() {

        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
        if (usuarioForm.hasErrors()) {
            return badRequest(formCreacionUsuario.render(usuarioForm, "Hay errores en el formulario"));
        }
        Usuario usuario = usuarioForm.get();
        Logger.debug("Usuario a grabar: " + usuario.toString());
        usuario = UsuariosService.grabaUsuario(usuario);
        flash("gestionaUsuario", "El usuario se ha grabado correctamente");
        Logger.debug("Usuario guardado correctamente: " + usuario.toString());
        return redirect(controllers.routes.UsuariosController.listaUsuarios());

    }

    @Transactional
    public Result detalleUsuario(int id) {
      String variable=session().get("usuario");
        if (variable!=null){

              Usuario usuario = UsuariosService.findUsuario(id);
              if (usuario == null) {
                //aqui hay que poner la ruta del Dashboard
                ////////
                  return notFound("Usuario no encontrado");
              } else if(!variable.equals("admin") && !usuario.login.equals(variable)){

                     return notFound("No autorizado a acceder a zonas de otros usuarios");
              }else {
                  return ok(detalleUsuario.render(usuario));
              }

        }else{

            return unauthorized("debes iniciar sesion");
        }
    }

    @Transactional
    public Result dashboard(int id) {
        String variable=session().get("usuario");
        if (variable!=null){

            Usuario usuario = UsuariosService.findUsuario(id);
            if (usuario == null) {

                return notFound("Usuario no encontrado");
            } else if(!usuario.login.equals(variable)){

                return notFound("No autorizado a acceder a zonas de otros usuarios");
            }else {
                //Aquí "fabricamos" todos los datos para pasarle a la vista

                //Número de tareas totales
                int numeroTareasTotales = usuario.tareas.size();

                //tic-9.2 - Avance de tareas últimas por finalizar o finalizadas (3)
                List<Tarea> tareasUltimasFinalizadas = TareasService.busquedaTareasUsuario(id, "", "fechaFinTarea", "desc", 0, 3);

                //tic-9.2 - Avance de proyectos con más tareas (3)
                //tic-9.3 - Limitamos a 3 elementos
                List<Proyecto> proyectosConMasTareas = ProyectosService.findProyectosConMasTareas(id).subList(0,3);

                //tic-9.3 - Tareas abiertas
                List<Tarea> tareasAbiertas = TareasService.findTareasAbiertas(id);

                //tic-9.3 - Tareas acabadas
                List<Tarea> tareasAcabadas = TareasService.findTareasAcabadas(id);

                //tic-9.3 - Proyecto con más comentarios
                List<Proyecto> proyectosConMasComentarios = ProyectosService.findProyectosConMasComentarios(id);

                //tic-9.3 - Proyecto con más colaboradores
                List<Proyecto> proyectosConMasColaboradores = ProyectosService.findProyectosConMasColaboradores(id);

                return ok(dashboard.render(usuario, numeroTareasTotales, tareasAbiertas, tareasAcabadas, tareasUltimasFinalizadas, proyectosConMasTareas, proyectosConMasComentarios, proyectosConMasColaboradores));
            }

        }else{

            return unauthorized("debes iniciar sesion");
        }
    }

    @Transactional
    public Result detalleUsuarioPorLogin(String login) {

      String variable=session().get("usuario");
        if (variable!=null){
          Usuario usuario = UsuariosService.findByLogin(login);
          if (usuario == null) {
            //aqui hay que poner la ruta del Dashboard
            ////////
              return notFound("Usuario no encontrado");
          } else {
              return ok(detalleUsuario.render(usuario));
            }
        }else{
          return unauthorized("debes iniciar sesion");
        }
    }

    @Transactional
    public Result grabaUsuarioModificado() {
        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
        if (usuarioForm.hasErrors()) {
            return badRequest(formModificacionUsuario.render(usuarioForm, "Hay errores en el formulario"));
        }
        Usuario usuario = usuarioForm.get();
        Logger.debug("Usuario a grabar: " + usuario.toString());
        usuario = UsuariosService.modificaUsuario(usuario);
        flash("gestionaUsuario", "El usuario se ha modificado correctamente (modificar)");
        Logger.debug("Usuario guardado correctamente (modificar): " + usuario.toString());
        if (session().get("usuario").equals("admin"))
            //  if(session.getAttribute("usuarioSesion") != null)
            return redirect(controllers.routes.UsuariosController.listaUsuarios());
        else
            return ok(detalleUsuario.render(usuario));
    }

    @Transactional
    public Result editaUsuario(int id) {
      String variable=session().get("usuario");
        if (variable!=null){
            //Cargamos vacío el form
            Form<Usuario> usuarioForm = formFactory.form(Usuario.class);
            //Obtenemos de la base de datos el usuario
            Usuario usuario = UsuariosService.findUsuario(id);

            //aqui hay que poner la ruta del Dashboard
            //porque se hace una peticion con usuario no encontrado
            if (usuario == null) {
                return notFound("Usuario no encontrado");
            } else if( !variable.equals("admin") && !usuario.login.equals(variable)){

                   return notFound("No autorizado a acceder a zonas de otros usuarios");
            } else {
            //Cargamos en el form los datos del usuario
            usuarioForm = usuarioForm.fill(usuario);
            //Retornamos a la vista los datos del usuario en el form
            return ok(formModificacionUsuario.render(usuarioForm, ""));
          }
        }else{

          return unauthorized("debes iniciar sesion");
        }
    }

    /**
     * Correción:
     * TIC-17 - UsuariosService.deleteUsuario(id) ya devuelve TRUE o FALSE dependiendo de si se ha borrado
     * o no. Gracias a las pruebas se ha detectado el mal funcionamiento.
     * Devolvemos respuesta al AJAX, que a su vez, después de mostrar el mensaje, recargará la página
     *
     * @param id
     * @return Result
     */
    @Transactional
    public Result borraUsuario(int id) {

      String variable=session().get("usuario");
        if (variable!=null){
          //Si se ha borrado recargamos página
          if (UsuariosService.deleteUsuario(id)) {
              return ok("Usuario borrado con éxito.");
          } else { //Si no, devolvemos error
              return badRequest("Usuario no se ha podido eliminar.");
          }
        }else{
         return unauthorized("debes iniciar sesion");
        }

    }

    public static Session session() {
        return Http.Context.current().session();
    }
}
