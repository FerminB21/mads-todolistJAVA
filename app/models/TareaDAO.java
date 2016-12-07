package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.List;

public class TareaDAO {

  public static Tarea findTareaUsuario(Integer tareaId,Integer usuarioId) {
  //  Logger.debug("Se obtiene proyecto Test: hhhhhhhhhhhh " + proyectoId+" "+usuarioId);
      TypedQuery<Tarea> query = JPA.em().createQuery(
              "select u from Tarea u where  usuarioId = :usuarioId and id = :tareaId", Tarea.class);
      try {
        query.setParameter("usuarioId", usuarioId).setParameter("tareaId", tareaId);
        //query.setParameter("proyectoId", proyectoId);
          Tarea tarea = query.getSingleResult();
          return tarea;
      } catch (NoResultException ex) {
          return null;
      }
  }

    public static Tarea find(Integer idTarea) {
        return JPA.em().find(Tarea.class, idTarea);
    }

    public static Tarea create(Tarea tarea) {
        JPA.em().persist(tarea);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(tarea);
        Logger.debug(tarea.toString());
        return tarea;
    }

    public static Tarea update(Tarea tarea) {
        return JPA.em().merge(tarea);
    }

    /**
     * Borra la tarea de la base de datos
     *
     * @param idTarea
     * @return Tarea
     */
    public static void delete(int idTarea) {
        try {
            Tarea tarea = JPA.em().getReference(Tarea.class, idTarea);
            JPA.em().remove(tarea);
        } catch (EntityNotFoundException ex) {
            Logger.debug("Se intenta borrar una tarea no existente. Salta excepción.");
        }
    }
///select * from tareas where usuarioId=X and idProyecto=Y

    public static List<Tarea> findTareasNoAsignadas(Integer id) {

        TypedQuery<Tarea> query = JPA.em().createQuery(
                "select u from Tarea u where  usuarioId = :usuarioId and proyectoId is null", Tarea.class);
        try {
            List<Tarea> tareas = query.setParameter("usuarioId", id).getResultList();
            return tareas;
        } catch (NoResultException ex) {
            return null;
        }

    }


}
