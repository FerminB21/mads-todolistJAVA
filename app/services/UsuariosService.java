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

    public static Usuario modificaUsuario(Usuario usuario) {
        Logger.debug("Se modifica usuario: " + usuario.id);
        return UsuarioDAO.update(usuario);
    }

    public static Usuario findUsuario(String id) {
      Usuario usuario = UsuarioDAO.find(id);
      Logger.debug("Se obtiene usuario: " + id);
      return usuario;
    }

    public static boolean deleteUsuario(String id) {
        Logger.debug("Se borra usuario: " + id);
        UsuarioDAO.delete(id);
        return true;
    }

    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }

    public static List<Usuario> findByUsuarios(String param, String valor){
      List<Usuario> lista = UsuarioDAO.findBy(param, valor);
      Logger.debug("Numero de usuarios: " + lista.size());
      return lista;
    }
}
