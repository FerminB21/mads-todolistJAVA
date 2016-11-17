package services;

import java.util.List;
import java.util.ArrayList;

import models.*;
import play.Logger;

public class ProyectosService {

    public static List<Proyecto> listaProyectosUsuario(Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if (usuario != null) {
            return usuario.proyectos;
        } else {
            throw new UsuariosException("Usuario no encontrado");
        }
    }

    /**
     * Guarda el proyecto asociada a un usuario, invoca a ProyectoDAO.create
     * @param descripcion, usuarioId
     * @return Proyecto
     */
    public static Proyecto crearProyectoUsuario(Proyecto proyecto, Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if(usuario != null){
            proyecto.usuario = usuario;
          //  Logger.debug("Se crea proyecto: " + proyecto + " asociada al usuario " +usuarioId);
            return ProyectoDAO.create(proyecto); //se le pasa ya con el usuario metido
        }
        else{
            throw new UsuariosException("Usuario no encontrado");
        }
    }

    /**
     * Busca el proyecto en la base de datos por id pasada como parámetro. Invoca ProuectoDAO.find
     * Devuelve un objeto usuario o null si éste no existe.
     * @param id
     * @return Proyecto
     */
    public static Proyecto findProyectoUsuario(int id) {
        Proyecto proyecto = ProyectoDAO.find(id);
        Logger.debug("Se obtiene proyecto: " + id);
        return proyecto;
    }

    /**
     * Modifica el proyecto
     * @param proyecto
     * @return Proyecto
     */
    public static Proyecto modificaProyectoUsuario(Proyecto proyecto) {
        //Hay que comprobar su usuario, por si se ha asignado uno que no existe

        Usuario existente = UsuarioDAO.find(proyecto.usuario.id);
        if (existente == null){
            Logger.debug("Usuario asociado al proyecto a editar no existe: "+ proyecto.usuario.id);
            throw new UsuariosException("Usuario asociado al proyecto a editar no existe: " + proyecto.usuario.id);
        }
        ProyectoDAO.update(proyecto);
        return proyecto;
    }
}
