package models;

import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
    //  Logger.debug("Se obtiene proyecto Test: hhhhhhhhhhhh " + proyectoId+" "+usuarioId);
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
}
