package services;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import models.*;

public class UsuariosService {

    public static Usuario grabaUsuario(Usuario usuario) {
        Logger.debug("Se crea usuario: " + usuario.id);
        return UsuarioDAO.create(usuario);
    }

    /**
     * Mejora:
     * TIC-17 - Se añade tratamiento de excepción (se evita modificar login igual a uno ya existente)
     * @param usuario
     * @return Usuario
     */
    public static Usuario modificaUsuario(Usuario usuario) {
        Usuario existente = UsuarioDAO.findByLogin(usuario.login);
        if (existente != null && existente.id != usuario.id)
            throw new UsuariosException("Login ya existente: " + usuario.login);
        UsuarioDAO.update(usuario);
        return usuario;
    }

    public static Usuario findUsuario(int id) {
      Usuario usuario = UsuarioDAO.find(id);
      Logger.debug("Se obtiene usuario: " + id);
      return usuario;
    }

    /**
     * Mejora:
     * TIC-17 - Ahora comprueba si se ha borrado
     * @param id
     * @return boolean
     */
    public static boolean deleteUsuario(int id) {
        //Comprobamos antes de borrar si el usuario existe
        //Si no existe, es que la id la hemos pasado mal
        //Tal vez, intento de burla?
        Usuario existente = UsuarioDAO.find(id);
        if(existente != null){
            Logger.debug("Existe, intenta borrarse");
            UsuarioDAO.delete(id); //Intentamos borrar
            //Volvemos a comprobar
            Usuario existente2 = UsuarioDAO.find(id);
            if(existente2 == null){
                Logger.debug("Borrado correcto.");
                return true;
            }
            return false;
        }
        else{
            Logger.debug("No existe, es un intento de burla");
            return false;
        }
    }

    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }

    public static Usuario findByLogin(String valor){
      Usuario usuario = UsuarioDAO.findByLogin(valor);
      return usuario;
    }
}
