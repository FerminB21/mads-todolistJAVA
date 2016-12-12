package services;

import models.*;
import play.Logger;

import java.util.List;
import java.util.ArrayList;

import org.springframework.util.Assert;

public class ProyectosService {

    public static List<Proyecto> listaProyectosUsuario(Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if (usuario != null) {
            return usuario.proyectos;
        } else {
            throw new ServiceException("Usuario no encontrado");
        }
    }

    /**
     * Guarda el proyecto asociada a un usuario, invoca a ProyectoDAO.create
     *
     * @param descripcion, usuarioId
     * @return Proyecto
     */
    public static Proyecto crearProyectoUsuario(Proyecto proyecto, Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if (usuario != null) {
            proyecto.usuario = usuario;
            //  Logger.debug("Se crea proyecto: " + proyecto + " asociada al usuario " +usuarioId);
            return ProyectoDAO.create(proyecto); //se le pasa ya con el usuario metido
        } else {
            throw new ServiceException("Usuario no encontrado");
        }
    }

    /**
     * Busca el proyecto en la base de datos por id pasada como parámetro. Invoca ProuectoDAO.find
     * Devuelve un objeto usuario o null si éste no existe.
     *
     * @param id
     * @return Proyecto
     */
    public static Proyecto findProyectoUsuario(int id) {
        Proyecto proyecto = ProyectoDAO.find(id);
        Logger.debug("Se obtiene proyecto: " + id);

           return proyecto;
    }
    /**
     *
     *se busca un proyecto de un usuario
     */
    public static Proyecto findProyectoPorUsuario(Integer idProyecto, Integer idUsuario) {
        Proyecto proyecto = ProyectoDAO.findProyectoUsuario(idProyecto, idUsuario);
        Logger.debug("Se obtiene proyecto: " + idProyecto);
        if(proyecto!=null){
           return proyecto;

        }else{
          throw new ServiceException("Los datos no son correctos");
        }
    }    /**
     * Modifica el proyecto
     *
     * @param proyecto
     * @param idColaborador
     * @return Proyecto
     */
    public static Proyecto modificaProyectoUsuario( Proyecto proyecto, Integer idColaborador ) {
        Assert.notNull( proyecto, "proyecto, no puede ser null"  );

        Usuario usuario = UsuarioDAO.find(proyecto.usuario.id);
        if( usuario == null ) {
            Logger.debug("Usuario asociado al proyecto a editar no existe: " + proyecto.usuario.id);
            throw new ServiceException("Usuario asociado al proyecto a editar no existe: " + proyecto.usuario.id);
        }

        if( idColaborador != null ) {
            Usuario colaborador = UsuariosService.findUsuario( idColaborador );
            proyecto.colaboradores.add( colaborador );
            usuario.colaboraciones.add( proyecto );
        }

        ProyectoDAO.update(proyecto);
        return proyecto;
    }

    /**
     * Se devuelve true si ha sido borrado o false si no.
     * Puede darse el caso de que se pase una id no válida. En ese caso, no se borrará
     * y el método devolverá false.
     *
     * @param id
     * @return boolean
     */
    public static boolean deleteProyecto(int id) {
        //Comprobamos antes de borrar si el proyecto existe
        //Si no existe, es que la id la hemos pasado mal
        //Tal vez, intento de burla?
        Proyecto existente = ProyectoDAO.find(id);
        if (existente != null) {
            Logger.debug("Existe, intenta borrarse");
            //Si tiene tareas asociadas, las recorremos una a una para eliminarlas del proyecto
            if(existente.tareas.size()>0)
                for(Tarea tarea : existente.tareas){
                    ProyectoDAO.deleteTarea(existente.usuario.id, tarea.id, id);
                }
            ProyectoDAO.delete(id); //Intentamos borrar
            //Volvemos a comprobar
            Proyecto existente2 = ProyectoDAO.find(id);
            if (existente2 == null) {
                Logger.debug("Borrado correcto.");
                return true;
            }

        }
        return false;
    }

    public static List<Tarea> tareasNoAsignadas(int iduser) {

        Usuario existente = UsuarioDAO.find(iduser);
        if (existente == null) {
            Logger.debug("Usuario asociado al proyecto a editar no existe: " + iduser);
            throw new ServiceException("Usuario asociado al proyecto a editar no existe: " + iduser);
        }
        List<Tarea> tareas = TareaDAO.findTareasNoAsignadas(iduser);
        return tareas;

    }

    public static List<Usuario> usuariosNoAsignados( Integer idProyecto ) {
        Assert.notNull( idProyecto, "idProyecto, no puede ser null" );

        List<Usuario> dev = new ArrayList<Usuario>();

        Proyecto proyecto = ProyectosService.findProyectoUsuario( idProyecto );
        List<Usuario> usuarios = UsuariosService.findAllUsuarios();
        for( Usuario usuario: usuarios ) {
            if( !proyecto.colaborador( usuario.id ) ) {
                dev.add( usuario );
            }
        }

        return dev;
    }

    public static boolean deleteTarea(int idUsuario, int idTarea, int idProyecto) {
        Tarea existente = TareaDAO.find(idTarea);
        if (existente != null) {
            Logger.debug("Existe, intenta borrarse");
            ProyectoDAO.deleteTarea(idUsuario, idTarea, idProyecto); //Intentamos borrar
            //Volvemos a comprobar
          /*  Tarea existente2 = ProyectoDAO.find(idProyecto);
            if(existente2 == null){
                Logger.debug("Borrado correcto.");
                return true;
            }*/
            return true;
        } else {
            Logger.debug("No existe, es un intento de burla");
            return false;
        }
    }

    public static List<Proyecto> findProyectosConMasTareas(Integer idUsuario) {
        List<Proyecto> proyectos = ProyectoDAO.findProyectosConMasTareas(idUsuario);
        if(proyectos == null){
            throw new ServiceException("No se devuelve ningún proyecto con más tareas, ha fallado algo en la consulta.");
        }
        return proyectos;
    }

}
