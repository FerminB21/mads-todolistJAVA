package services;

import models.*;
import play.Logger;

import java.util.Date;

import org.springframework.util.Assert;

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
    public static Comentario crearComentario( Comentario comentario, Integer usuarioId, Integer proyectoId ) {
        Assert.notNull( comentario, "comentario, no puede ser null" );
        Assert.notNull( usuarioId, "usuarioId, no puede ser null" );
        Assert.notNull( proyectoId, "proyectoId, no puede ser null" );

        Usuario usuario = UsuariosService.findUsuario( usuarioId );
        Proyecto proyecto = ProyectosService.findProyectoUsuario( proyectoId );

        if( usuario != null ) {
            if( proyecto != null ) {
                comentario.usuario = usuario;
                comentario.proyecto = proyecto;

                // al insertar se le mete la fecha actual
                comentario.fecha = new Date();

                Logger.debug( "Se crea el comentario: " + comentario.comentario );
                return ComentarioDAO.create( comentario );
            } else {
                throw new ServiceException( "Proyecto no encontrado" );
            }
        } else {
            throw new ServiceException( "Usuario no encontrado" );
        }
    }

    public static Comentario findComentarioPorUsuario( Integer idComentario, Integer idUsuario ) {
        Assert.notNull( idComentario, "idComentario, no puede ser null" );
        Assert.notNull( idUsuario, "idUsuario, no puede ser null" );

        Comentario comentario = ComentarioDAO.findComentarioUsuario( idComentario, idUsuario );
        if( comentario != null ) {
            Logger.debug( "Se obtiene el comentario con id: " + idComentario );
            return comentario;
        } else {
            throw new ServiceException( "Los datos no son correctos" );
        }
    }

    public static Comentario getComentarioById( Integer idComentario ) {
        Assert.notNull( idComentario, "idComentario, no puede ser null" );

        return ComentarioDAO.find( idComentario );
    }

    public static Comentario editarComentario( Comentario comentario, Integer idUsuario, Integer idProyecto ) {
        Assert.notNull( comentario, "comentario, no puede ser null" );
        Assert.notNull( idUsuario, "idUsuario, no puede ser null" );
        Assert.notNull( idProyecto, "idProyecto, no puede ser null" );

        //FIXME debe haber una forma más óptima de hacer esto
        // hay que recuperar el comentario que está en la base de datos porque el que nos llega ha perdido las relaciones
        Comentario comentarioBD = getComentarioById( comentario.id );

        Usuario usuario = UsuariosService.findUsuario( idUsuario );
        Proyecto proyecto = ProyectosService.findProyectoUsuario( idProyecto );
        // sólo el propietario del comentario o el propietario del proyecto pueden editar el comentario
        if( comentarioBD.propietario( idUsuario ) || proyecto.propietario( idUsuario ) ) {
            // volver a meter las relaciones...
            comentario.usuario = usuario;
            comentario.proyecto = proyecto;

            return ComentarioDAO.update( comentario );
        } else {
            throw new ServiceException( "No puedes editar este comentario." );
        }
    }

    public static void borrarComentario( Integer idComentario, Integer idUsuario, Integer idProyecto ) {
        Assert.notNull( idComentario, "idComentario, no puede ser null" );
        Assert.notNull( idUsuario, "idUsuario, no puede ser null" );
        Assert.notNull( idProyecto, "idProyecto, no puede ser null" );

        Comentario comentario = getComentarioById( idComentario );
        Proyecto proyecto = ProyectosService.findProyectoUsuario( idProyecto );
        // sólo el propietario del comentario o el propietario del proyecto pueden borrar el comentario
        if( comentario.propietario( idUsuario ) || proyecto.propietario( idUsuario ) ) {
            ComentarioDAO.delete( idComentario );
        } else {
            throw new ServiceException( "No puedes borrar este comentario." );
        }
    }
}
