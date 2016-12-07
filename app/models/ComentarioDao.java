package models;

import play.Logger;
import play.db.jpa.JPA;

/**
 * Created by mads on 7/12/16.
 */
public class ComentarioDao {

    public static Comentario create( Comentario comentario ) {
        JPA.em().persist(comentario);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(comentario);
        Logger.debug(comentario.toString());
        return comentario;
    }
}
