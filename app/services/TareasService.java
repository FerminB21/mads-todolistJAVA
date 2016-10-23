package services;

import java.util.List;
import java.util.ArrayList;

import models.*;
import play.Logger;

public class TareasService {

    public static List<Tarea> listaTareasUsuario(Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if (usuario != null) {
            return usuario.tareas;
        } else {
            throw new UsuariosException("Usuario no encontrado");
        }
    }

    /**
     * Guarda la tarea asociada a un usuario, invoca a TareaDAO.create
     * @param descripcion, usuarioId
     * @return Tarea
     */
    public static Tarea crearTareaUsuario(String descripcion, Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if(usuario != null){
            Tarea tarea = new Tarea(descripcion);
            tarea.usuario = usuario;
            Logger.debug("Se crea tarea: " + tarea + " asociada al usuario " +usuarioId);
            return TareaDAO.create(tarea); //se le pasa ya con el usuario metido
        }
        else{
            throw new UsuariosException("Usuario no encontrado");
        }
    }

    /**
     * Modifica la tarea
     * @param tarea
     * @return Tarea
     */
    public static Tarea modificaTareaUsuario(Tarea tarea) {
        //Hay que comprobar su usuario, por si se ha asignado uno que no existe

        Usuario existente = UsuarioDAO.find(tarea.usuario.id);
        if (existente == null){
            Logger.debug("Usuario asociado a la tarea a editar no existe: "+ tarea.usuario.id);
            throw new UsuariosException("Usuario asociado a la tarea a editar no existe: " + tarea.usuario.id);
        }
        TareaDAO.update(tarea);
        return tarea;
    }

    /**
     * Busca a la tarea en la base de datos por id pasada como parámetro. Invoca TareaDAO.find
     * Devuelve un objeto usuario o null si éste no existe.
     * @param id
     * @return Tarea
     */
    public static Tarea findTareaUsuario(int id) {
        Tarea tarea = TareaDAO.find(id);
        Logger.debug("Se obtiene tarea: " + id);
        return tarea;
    }
}