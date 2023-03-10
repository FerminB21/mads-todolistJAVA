package services;

import models.Usuario;
import models.UsuarioDAO;
import play.Logger;

import java.util.List;

public class UsuariosService {

    /**
     * Guarda al usuario, invoca a UsuarioDAO.create
     *
     * @param usuario
     * @return Usuario
     */
    public static Usuario grabaUsuario(Usuario usuario) {
        Logger.debug("Se crea usuario: " + usuario.id);
        return UsuarioDAO.create(usuario);
    }

    /**
     * Mejora:
     * TIC-17 - Se añade tratamiento de excepción (se evita modificar login igual a uno ya existente)
     *
     * @param usuario
     * @return Usuario
     */
    public static Usuario modificaUsuario(Usuario usuario) {
        Usuario existente = UsuarioDAO.findByLogin(usuario.login);
        if (existente != null && existente.id != usuario.id)
            throw new ServiceException("Login ya existente: " + usuario.login);
        UsuarioDAO.update(usuario);
        return usuario;
    }


    /**
     * Busca al usuario en base a su id pasada como parámetro. Invoca UsuarioDAO.find
     * Devuelve un objeto usuario o null si éste no existe.
     *
     * @param id
     * @return Usuario
     */
    public static Usuario findUsuario(int id) {
        Usuario usuario = UsuarioDAO.find(id);
        Logger.debug("Se obtiene usuario: " + id);
        return usuario;

    }



    /*public static Usuario findUsuarioPorLogin(String login) {
        Usuario usuario = UsuarioDAO.findByLogin(login);
        Logger.debug("Se obtiene usuario: " + login);
        if(usuario!=null)
          return usuario;

          throw new UsuariosException("Usuario no esta en la base de datos");
    }*/


    /**
     * Mejora:
     * TIC-17 - Ahora comprueba si se ha borrado
     *
     * @param id
     * @return boolean
     */
    public static boolean deleteUsuario(int id) {
        //Comprobamos antes de borrar si el usuario existe
        //Si no existe, es que la id la hemos pasado mal
        //Tal vez, intento de burla?
        Usuario existente = UsuarioDAO.find(id);
        if (existente != null) {
            Logger.debug("Existe, intenta borrarse");
            UsuarioDAO.delete(id); //Intentamos borrar
            //Volvemos a comprobar
            Usuario existente2 = UsuarioDAO.find(id);
            if (existente2 == null) {
                Logger.debug("Borrado correcto.");
                return true;
            }
            return false;
        } else {
            Logger.debug("No existe, es un intento de burla");
            return false;
        }
    }

    /**
     * Busca en la BBDD todos los usuarios gracias a UsuarioDAO.findAll y los devuelve.
     *
     * @return List<Usuario>
     */
    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }

    /**
     * Busca al usuario dado un login pasado como parámetro. Invoca a UsuarioDAO.findByLogin
     * Devuelve un objeto usuario, que puede ser Usuario o null si no hay ninguno.
     *
     * @param valor
     * @return Usuario
     */
    public static Usuario findByLogin(String valor) {
        Usuario usuario = UsuarioDAO.findByLogin(valor);
        return usuario;
    }
}
