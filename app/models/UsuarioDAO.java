package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.List;
import java.util.Date;

public class UsuarioDAO {
    public static Usuario create(Usuario usuario) {
        usuario.nulificaAtributos();
        JPA.em().persist(usuario);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(usuario);
        Logger.debug(usuario.toString());
        return usuario;
    }

    /**
     * Mejora:
     * TIC-17 - Se añade tratamiento de excepción
     *
     * @param usuario
     * @return Usuario
     */
    public static Usuario find(int idUsuario) {
        try {
            Usuario usuario = JPA.em().find(Usuario.class, idUsuario);
            return usuario;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static Usuario update(Usuario usuario) {
        return JPA.em().merge(usuario);
    }

    /**
     * Mejora:
     * TIC-17 - Se añade tratamiento de excepción
     *
     * @param usuario
     * @return Usuario
     */
    public static void delete(int idUsuario) {
        try {
            Usuario usuario = JPA.em().getReference(Usuario.class, idUsuario);
            JPA.em().remove(usuario);
        } catch (EntityNotFoundException ex) {
            Logger.debug("Se intenta borrar un usuario no existente. Salta excepción.");
        }
    }

    public static List<Usuario> findAll() {
        TypedQuery<Usuario> query = JPA.em().createQuery(
                "select u from Usuario u ORDER BY id", Usuario.class);
        return query.getResultList();
    }
    /**
     *
     * se busca un usuario por login
     *
     * @param login
     * @return Usuario
     */
    public static Usuario findByLogin(String login) {
        TypedQuery<Usuario> query = JPA.em().createQuery(
                "select u from Usuario u where u.login = :login", Usuario.class);
        try {
            Usuario usuario = query.setParameter("login", login).getSingleResult();
            return usuario;
        } catch (NoResultException ex) {
            return null;
        }
    }
}
