package services;

import java.util.List;
import java.util.ArrayList;

import models.*;

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
     * @return Tarea
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
}
