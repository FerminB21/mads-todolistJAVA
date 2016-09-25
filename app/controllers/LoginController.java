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

public class LoginController extends Controller {

   @Inject FormFactory formFactory;

   // Devuelve un formulario para registrar
   public Result formularioLogueoRegistro() {
       return ok(formLogueoRegistro.render(formFactory.form(Usuario.class),"", "logueo"));
   }

   @Transactional
   // Verifica si el usuario puede loguearse y devuelve código HTTP
   // de redirección a la página de portada
   public Result logueoUsuario() {

     Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
     if (usuarioForm.hasErrors()) {
         //Le paso un tercer parámetro a la vista para indicar de donde venimos
         return badRequest(formLogueoRegistro.render(usuarioForm, "Hay errores en el formulario", "logueo"));
     }
     Usuario usuario = usuarioForm.get();
     //Debemos comprobar si ese usuario ya existe en la base de datos
     List<Usuario> usuarios = UsuariosService.findByUsuarios("login", usuario.login);
     if(usuarios.size() > 0){
       Logger.debug("Existe usuario con ese login");
       Usuario usuarioExistente = usuarios.get(0); //solo será 1
       //Comprobación de si el usuario tiene contraseña y nosotros también introducimos una
       if(usuario.password != null && usuarioExistente.password != null && usuarioExistente.password.equals(usuario.password)){ //Si coinciden contraseña, válido
         Logger.debug("Logueado correctamente");
         flash("entraUsuario", "Logueado correctamente.");
         return redirect(controllers.routes.HomeController.portada());
       }
       else if(usuarioExistente.password == null){ //Si es el mismo usuario pero no tiene contraseña (introducido por administrador)
         Logger.debug("Necesario activar el usuario");
         return badRequest(formLogueoRegistro.render(usuarioForm, "Usuario no activo. Necesario registrarse para activarlo.", "logueo"));
       }
       else{
         Logger.debug("Error de validación");
         return badRequest(formLogueoRegistro.render(usuarioForm, "Datos introducidos no coinciden.", "logueo"));
       }
     }
     else{
        //No existe ningún usuario con ese login
        Logger.debug("Error de validación");
        return badRequest(formLogueoRegistro.render(usuarioForm, "Usuario no existe. Regístrate para acceder.", "logueo"));
     }
   }

     @Transactional
     // Registra al usuario si ha insertado bien los datos y devuelve código HTTP
     // de redirección a la página de portada
     public Result registroUsuario() {

         Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
         if (usuarioForm.hasErrors()) {
             //Le paso un tercer parámetro a la vista para indicar de donde venimos
             return badRequest(formLogueoRegistro.render(usuarioForm, "Hay errores en el formulario", "registro"));
         }
         Usuario usuario = usuarioForm.get();
         //Debemos comprobar si ese usuario ya existe en la base de datos
         List<Usuario> usuarios = UsuariosService.findByUsuarios("login", usuario.login);
         if(usuarios.size() > 0){
           Logger.debug("Existe usuario con ese login");
           Usuario usuarioExistente = usuarios.get(0); //solo será 1
           if(usuarioExistente.password == null ||  usuarioExistente.password.equals("")){
             Logger.debug("No tiene contraseña, se actualizan sus datos");
             usuarioExistente.copiarDatos(usuario); //al usuario existente (que tiene id) le copiamos
                                                      //los datos del introducido por el form
             usuario = UsuariosService.modificaUsuario(usuarioExistente);
             flash("entraUsuario", "Tu usuario ya existía, se han modificado tus datos además de actualizarse tu contraseña.");
           }
           else{
             Logger.debug("Sí tiene contraseña, devolvemos error."+usuarioExistente.password);
             return badRequest(formLogueoRegistro.render(usuarioForm, "Usuario ya existente", "registro"));
           }
         }
         else{
            //Doy de alta nuevo
            usuario = UsuariosService.grabaUsuario(usuario);
            Logger.debug("No existe ninguno con ese login, se registra");
            flash("entraUsuario", "Te has dado de alta en TodoList. Bienvenido.");
         }

         //Si llegamos hasta aquí es que lo hemos registrado de una manera (nuevo) o de otra (modificando datos)
         Logger.debug("Usuario a grabar (registro): " + usuario.toString());
         Logger.debug("Usuario guardado correctamente: " + usuario.toString());
         return redirect(controllers.routes.HomeController.portada());
      }

}
