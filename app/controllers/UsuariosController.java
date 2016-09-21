package controllers;

import java.util.List;
import javax.inject.*;

import play.*;
import play.mvc.*;
import views.html.*;
import static play.libs.Json.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;

import services.*;
import models.*;

public class UsuariosController extends Controller {

   @Inject FormFactory formFactory;

  // Devuelve un formulario para crear un nuevo usuario
   public Result formularioNuevoUsuario() {
       return ok(formCreacionUsuario.render(formFactory.form(Usuario.class),""));
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
       flash("grabaUsuario", "El usuario se ha grabado correctamente");
       //De momento no
       //return redirect(controllers.routes.UsuariosController.listaUsuarios());
       Logger.debug("Usuario guardado correctamente: " + usuario.toString());
       return redirect(controllers.routes.UsuariosController.formularioNuevoUsuario());

}
}
