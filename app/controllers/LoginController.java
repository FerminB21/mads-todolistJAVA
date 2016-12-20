package controllers;

import java.util.List;
import javax.inject.*;

import play.*;
import play.mvc.*;
import play.mvc.Http.Session;
import views.html.*;

import static play.libs.Json.*;

import models.Usuario;
import play.Logger;

import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.UsuariosService;
import views.html.formLogueoRegistro;

import javax.inject.Inject;

public class LoginController extends Controller {

    @Inject
    FormFactory formFactory;

    // Devuelve un formulario para registrar
    public Result formularioLogueoRegistro() {

          session().clear();
        return ok(formLogueoRegistro.render(formFactory.form(Usuario.class), "", "logueo"));
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
        Usuario usuarioExistente = UsuariosService.findByLogin(usuario.login);
        if (usuarioExistente != null) {
            Logger.debug("Existe usuario con ese login");
            //Comprobación de si el usuario tiene contraseña y nosotros también introducimos una
            if (usuario.password != null && usuarioExistente.password != null && usuarioExistente.password.equals(usuario.password)) { //Si coinciden contraseña, válido
                Logger.debug("Logueado correctamente");
                //Creamos sesión (es un string)
                session().put("usuario" , usuarioExistente.login);
                session().put("idUsuario" , String.valueOf(usuarioExistente.id)); //esto debería ser temporal -> hay que guardar el objeto usuario en la sesión y no datos sueltos
                //Logger.debug("valor de la sesion  "+session().get("usuario"));

                return redirect(controllers.routes.UsuariosController.dashboard(usuarioExistente.id));
            } else if (usuarioExistente.password == null) { //Si es el mismo usuario pero no tiene contraseña (introducido por administrador)
                Logger.debug("Necesario activar el usuario");
                return badRequest(formLogueoRegistro.render(usuarioForm, "Usuario no activo. Necesario registrarse para activarlo.", "logueo"));
            } else {
                Logger.debug("Error de validación");
                return badRequest(formLogueoRegistro.render(usuarioForm, "Datos introducidos no coinciden.", "logueo"));
            }
        } else {
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
        String repitePassword = Form.form().bindFromRequest().get("repitePassword");
        if (usuarioForm.hasErrors()) {
            //Le paso un tercer parámetro a la vista para indicar de donde venimos
            return badRequest(formLogueoRegistro.render(usuarioForm, "Hay errores en el formulario", "registro"));
        }

        Usuario usuario = usuarioForm.get();
        //Debemos comprobar si ese usuario ya existe en la base de datos
        Usuario usuarioExistente = UsuariosService.findByLogin(usuario.login);
        if (usuarioExistente != null) {
            //Le paso un tercer parámetro a la vista para indicar de donde venimos
            return badRequest(formLogueoRegistro.render(usuarioForm, "Ya existe ese login. Inténtalo con otro.", "registro"));
        }

        //Comprobamos errores relacionados entre los campos del formulario
        if (usuario.password.equals("") || repitePassword.equals("") || !usuario.password.equals(repitePassword)) {
            return badRequest(formLogueoRegistro.render(usuarioForm, "Ha habido un problema con las contraseñas. Asegúrate de que coincidan y no estén vacías.", "registro"));
        } else { //Si funciona
            //Doy de alta nuevo
            usuario = UsuariosService.grabaUsuario(usuario);
            flash("entraUsuario", "Te has dado de alta en TodoList. Bienvenido.");
            //Si llegamos hasta aquí es que lo hemos registrado de una manera (nuevo) o de otra (modificando datos)
            Logger.debug("No existe ninguno con ese login, se registra");
            Logger.debug("Usuario a grabar (registro): " + usuario.toString());
            Logger.debug("Usuario guardado correctamente: " + usuario.toString());

            //Nuevo - tic-9.4 dashboard como usuario principal
            //Al registrarse el usuario ahora también iniciará sesión en vez de redirigir a la página de portada
            session().put("usuario" , usuario.login);
            session().put("idUsuario" , String.valueOf(usuario.id));
            return redirect(controllers.routes.UsuariosController.dashboard(usuario.id));
        }
    }
    public static Session session() {
        return Http.Context.current().session();
    }
}
