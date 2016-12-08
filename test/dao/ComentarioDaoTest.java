package dao;

import models.Comentario;
import models.ComentarioDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class ComentarioDaoTest {

    Database db;
    JPAApi jpa;

    @Before
    public void initDatabase() {
        db = Databases.inMemoryWith("jndiName", "DefaultDS");
        // Necesario para inicializar el nombre JNDI de la BD
        db.getConnection();
        // Se activa la compatibilidad MySQL en la BD H2
        db.withConnection(connection -> {
            connection.createStatement().execute("SET MODE MySQL;");
        });
        jpa = JPA.createFor("memoryPersistenceUnit");
    }

    @After
    public void shutdownDatabase() {
        //Después de cada test borra la tabla
        db.withConnection(connection -> {
            connection.createStatement().execute("DROP TABLE Usuario;");
        });
        jpa.shutdown();
        db.shutdown();
    }

    @Test
    public void creaBuscaComentarioTest() {

        //Creamos el comentario
        int id = jpa.withTransaction( () -> {
            Comentario nuevo = new Comentario( "Comentario de prueba" );
            Comentario comentario = ComentarioDAO.create( nuevo );
            return comentario.id;
        });

        //Comprobamos que existe
        jpa.withTransaction(() -> {
            Comentario busqueda = ComentarioDAO.find( id );
            assertThat( busqueda.comentario, equalTo( "Comentario de prueba" ) );
        });
    }

    @Test
    public void borraComentarioTest() {

        //Creamos el comentario
        int id = jpa.withTransaction( () -> {
            Comentario nuevo = new Comentario( "Comentario de prueba" );
            Comentario comentario = ComentarioDAO.create( nuevo );
            return comentario.id;
        });

        //Eliminamos el comentario
        jpa.withTransaction( () -> {
            ComentarioDAO.delete( id );
        });

        //Comprobamos que no existe
        jpa.withTransaction( () -> {
            Comentario busqueda = ComentarioDAO.find( id );
            assertNull( busqueda );
        });
    }

}
