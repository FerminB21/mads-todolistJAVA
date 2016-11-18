package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;

public class ProyectoDAO {
    public static Proyecto find(Integer idProyecto) {
        return JPA.em().find(Proyecto.class, idProyecto);
    }

    public static Proyecto create (Proyecto proyecto) {
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
    public static Tarea delete(int idUsuario, int idTarea, int idProyecto) {

          Tarea tarea=JPA.em().find(Tarea.class, idTarea);
          tarea.proyecto = null;
          return JPA.em().merge(tarea);
    }
}
