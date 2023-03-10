package models;

import play.Logger;
import play.db.jpa.JPA;
import services.UsuariosService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjDoubleConsumer;

public class ProyectoDAO {
    public static Proyecto find(Integer idProyecto) {
        return JPA.em().find(Proyecto.class, idProyecto);
    }
    /**
     * Se comprueba si el proyecto pertenece al usuario
     * en caso contrario se devuelve null, asi no se puede ver proyectos de otros
     * usuarios
     * @param usuario
     * @return Usuario
     */
    public static Proyecto findProyectoUsuario(Integer proyectoId,Integer usuarioId) {
    //  Logger.debug("Se obtiene proyecto Test: " + proyectoId+" "+usuarioId);
        TypedQuery<Proyecto> query = JPA.em().createQuery(
                "select u from Proyecto u where  usuarioId = :usuarioId and id = :proyectoId", Proyecto.class);
        try {
          query.setParameter("usuarioId", usuarioId).setParameter("proyectoId", proyectoId);
          //query.setParameter("proyectoId", proyectoId);
            Proyecto proyecto = query.getSingleResult();
            return proyecto;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static Proyecto create(Proyecto proyecto) {
        JPA.em().persist(proyecto);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(proyecto);
        Logger.debug(proyecto.toString());
        return proyecto;
    }

    public static Proyecto update(Proyecto proyecto) {
        return JPA.em().merge(proyecto);
    }

    /**
     * Borra el proyecto de la base de datos
     *
     * @param idProyecto
     * @return Proyecto
     */
    public static void delete(int idProyecto) {
        try {
            Proyecto proyecto = JPA.em().getReference(Proyecto.class, idProyecto);
            JPA.em().remove(proyecto);
        } catch (EntityNotFoundException ex) {
            Logger.debug("Se intenta borrar un proyecto no existente. Salta excepción.");
        }
    }

    public static Tarea deleteTarea(int idUsuario, int idTarea, int idProyecto) {

        Tarea tarea = JPA.em().find(Tarea.class, idTarea);
        tarea.proyecto = null;
        return JPA.em().merge(tarea);
    }

    public static List<Proyecto> findProyectosConMasTareas(Integer idUsuario){
        //Al realizar una consulta con dos tablas, no sirve enlazar por clave ajena para los tests (para el programa sí)
        //Se realiza una "native query", con el formato utilizado en mysql estándar
        //Hay que enlazar por objeto
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        String query = "select p.* from Tarea t, Proyecto p where t.proyectoId=p.id and p.usuarioId = :usuarioId group by proyectoId order by count(*) desc;";

        try {
            List<Proyecto> proyectos = JPA.em().createNativeQuery(query, Proyecto.class).setParameter("usuarioId", usuario.id).getResultList();
            return proyectos;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static List<Proyecto> findProyectoUsuarioColaborador(Integer usuarioId) {

        TypedQuery<Proyecto> query = JPA.em().createQuery(
                "select p.id,p.nombre,p.usuarioId from Proyecto p,COL_PROJ c  where p.id=c.PROJ_ID  and c.COL_ID = :usuarioId", Proyecto.class);

        try {
          query.setParameter("usuarioId", usuarioId);

            List<Proyecto> proyectos = query.getResultList();
            return proyectos;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static List<Proyecto> findProyectosConMasComentarios(Integer idUsuario){
        //Al realizar una consulta con dos tablas, no sirve enlazar por clave ajena para los tests (para el programa sí)
        //Se realiza una "native query", con el formato utilizado en mysql estándar
        //Hay que enlazar por objeto
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        String query = "select p.* from Comentario t, Proyecto p where t.proyectoId=p.id and p.usuarioId = :usuarioId group by proyectoId order by count(*) desc;";

        try {
            List<Proyecto> proyectos = JPA.em().createNativeQuery(query, Proyecto.class).setParameter("usuarioId", usuario.id).getResultList();
            return proyectos;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static List<Proyecto> findProyectosConMasColaboradores(Integer idUsuario){
        //Al realizar una consulta con dos tablas, no sirve enlazar por clave ajena para los tests (para el programa sí)
        //Se realiza una "native query", con el formato utilizado en mysql estándar
        //Hay que enlazar por objeto
        Usuario usuario = UsuariosService.findUsuario(idUsuario);
        String query = "select p.* from COL_PROJ t, Proyecto p where t.PROJ_ID=p.id and p.usuarioId = :usuarioId group by PROJ_ID order by count(*) desc;";

        try {
            List<Proyecto> proyectos = JPA.em().createNativeQuery(query, Proyecto.class).setParameter("usuarioId", usuario.id).getResultList();
            return proyectos;
        } catch (NoResultException ex) {
            return null;
        }
    }
}
