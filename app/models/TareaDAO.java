package models;

import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class TareaDAO {

    public static Tarea findTareaUsuario(Integer tareaId, Integer usuarioId) {
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

    /**
     * @param idUsuario
     * @param filtro
     * @param sortBy
     * @param order
     * @param inicio
     * @param tamanoPagina
     * @return List<Tarea>
     */
    public static List<Tarea> busquedaTareasUsuario(Integer idUsuario, String filtro, String sortBy, String order, Integer inicio, Integer tamanoPagina) {
        //Se monta la cadena de consulta

        String sql = "select u from Tarea u";

        //Where
        sql += " where usuarioId = :usuarioId ";
        if (!filtro.equals("")) {
            sql += " and (id like '%" + filtro + "%' or descripcion like '%" + filtro + "%' or estado = " + EstadoTareaEnum.getIdByDescripcion(filtro) + " or color like '%" + filtro.substring(1) + "%' or fechaFinTarea like '%" + filtro + "%')";
        }

        //Order
        if (!sortBy.equals("")) {
            sql += " order by " + sortBy + " " + order;
        }

        TypedQuery<Tarea> query = JPA.em().createQuery(sql, Tarea.class).setFirstResult(inicio).setMaxResults(tamanoPagina);

        try {
            List<Tarea> tareas = query.setParameter("usuarioId", idUsuario).getResultList();
            return tareas;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public static List<Tarea> findTareasAcabadas(Integer id) {

        TypedQuery<Tarea> query = JPA.em().createQuery(
                "select u from Tarea u where  usuarioId = :usuarioId and fechaFinTarea is not null and fechaFinTarea!='0000-00-00'", Tarea.class);
        try {
            List<Tarea> tareas = query.setParameter("usuarioId", id).getResultList();
            return tareas;
        } catch (NoResultException ex) {
            return null;
        }
    }


}
