package services;

import models.*;
import play.Logger;

import java.util.Date;

/**
 * Created by mads on 7/12/16.
 */
public class ComentariosService {

    /**
     * Crea un comentario nuevo con un usuario y un proyecto asociados
     *
     * @param comentario
     * @param usuarioId
     * @param proyectoId
     * @return comentario creado
     */
    public static Comentario crearComentario(Comentario comentario, Integer usuarioId, Integer proyectoId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        Proyecto proyecto = ProyectoDAO.find(proyectoId);

        // al insertar se le mete la fecha actual
        comentario.fecha = new Date();

        if (usuario != null && proyecto != null) {
            comentario.usuario = usuario;
            comentario.proyecto = proyecto;
            Logger.debug("Se crea el comentario: " + comentario.comentario );
            return ComentarioDao.create( comentario );
        } else {
            throw new UsuariosException("Usuario no encontrado");
        }
    }
}
