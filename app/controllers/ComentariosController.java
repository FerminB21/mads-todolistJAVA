package controllers;

import models.Comentario;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import services.ComentariosService;
import views.html.*;

import javax.inject.Inject;

/**
 * Created by mads on 7/12/16.
 */
public class ComentariosController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result formularioNuevoComentario( Integer idUsuario, Integer idProyecto ) {
        return ok( formCreacionComentario.render( formFactory.form( Comentario.class ), idUsuario, idProyecto, "" ) );
    }

    @Transactional
    public Result formularioEditaComentario( Integer idUsuario, Integer idProyecto, Integer idComentario ) {

        // esto hay que ponerlo en todas las funciones???
        String variable=session().get("usuario");
        if( variable != null ) {
            Form<Comentario> comentarioForm = formFactory.form( Comentario.class );
            Comentario comentario = ComentariosService.findComentarioPorUsuario( idComentario, idUsuario );
            comentarioForm = comentarioForm.fill( comentario );
            return ok( formModificacionComentario.render( comentarioForm, idUsuario, idProyecto, "" ) );
        } else {
            // esto debería ser genérico...
            // si no, cada vez puedes poner un mensaje diferente e incluso olvidar ponerlo
            return unauthorized( "tururú" );
        }
    }

    @Transactional
    public Result grabaNuevoComentario( Integer idUsuario, Integer idProyecto ) {
        Form<Comentario> comentarioForm = formFactory.form( Comentario.class ).bindFromRequest();
        if( comentarioForm.hasErrors() ) {
            return badRequest( formCreacionComentario.render( comentarioForm, idUsuario, idProyecto, "Hay errores en el formulario" ) );
        }
        Comentario comentario = comentarioForm.get();
        Logger.debug( "Comentario a grabar: " + comentario.toString() );
        comentario = ComentariosService.crearComentario( comentario, idUsuario, idProyecto );
     //   flash("gestionaTarea", "La tarea se ha grabado correctamente");
        Logger.debug( "Comentario guardado correctamente: " + comentario.toString() );
        return redirect( routes.ProyectosController.detalleProyecto( idProyecto, idUsuario ) );
    }

    @Transactional
    public Result grabaComentarioModificado( Integer idUsuario, Integer idProyecto ) {
        Form<Comentario> comentarioForm = formFactory.form( Comentario.class ).bindFromRequest();
        if( comentarioForm.hasErrors() ) {
            return badRequest( formModificacionComentario.render( comentarioForm, idUsuario, idProyecto, "Hay errores en el formulario" ) );
        }
        Comentario comentario = comentarioForm.get();

//        Usuario usuario = UsuariosService.findUsuario( idUsuario );
//        comentario.usuario = usuario;
        comentario = ComentariosService.editarComentario( comentario, idUsuario, idProyecto );

        Logger.debug( "Comentario modificado correctamente: " + comentario.toString() );
        return redirect( routes.ProyectosController.detalleProyecto( idProyecto, idUsuario ) );
    }

    @Transactional
    public Result borraComentario( Integer idUsuario, Integer idProyecto, Integer idComentario ) {
        ComentariosService.borrarComentario( idComentario, idUsuario, idProyecto );
        return ok();
    }
}
