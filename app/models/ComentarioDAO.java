package models;

import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * Created by mads on 7/12/16.
 */
public class ComentarioDAO {

    public static Comentario find( Integer idComentario ) {
        return JPA.em().find( Comentario.class, idComentario );
    }

    public static Comentario update( Comentario comentario ) {
        return JPA.em().merge( comentario );
    }

    public static Comentario create( Comentario comentario ) {
        JPA.em().persist( comentario );
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh( comentario );
        Logger.debug( comentario.toString() );
        return comentario;
    }

    public static void delete( int idComentario ) {
        try {
            Comentario comentario = JPA.em().getReference( Comentario.class, idComentario );
            JPA.em().remove( comentario );
        } catch( EntityNotFoundException e ) {
            Logger.debug( "No exite ningún comentario con ese id." );
        }
    }

    public static Comentario findComentarioUsuario( Integer comentarioId, Integer usuarioId ) {
        TypedQuery<Comentario> query = JPA.em().createQuery(
                "select u from Comentario u where usuarioId = :usuarioId and id = :comentarioId", Comentario.class );
        try {
            query.setParameter( "usuarioId", usuarioId );
            query.setParameter( "comentarioId", comentarioId );

            Comentario comentario = query.getSingleResult();
            return comentario;
        } catch( NoResultException e ) {
            return null;
        }
    }
}
