package dao;

import play.Logger;
import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import models.*;

import java.util.List;

public class UsuarioDaoTest {

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

    /**
     * Crea un usuario nuevo (pepe) y comprobamos si coincide con lo que esperamos.
     * Métodos que se prueban:
     *  - create
     *  - find
     */
    @Test
    public void creaBuscaUsuario() {

        //Creamos el usuario
        Integer id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Comprobamos que existe
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(id);
            assertThat(usuario.login, equalTo("pepe"));
        });
    }

    /**
     * Busca el usuario por login
     * Métodos que se prueban:
     *  - findByLogin
     */
    @Test
    public void buscaUsuarioLogin() {

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.findByLogin("pepe");
            assertNull(usuario);
        });
    }

    /**
     * Comprobamos los siguientes métodos para cambiar el login de un usuario:
     *  - create
     *  - find
     *  - update
     */
    @Test
    public void modificaUsuario(){

        //Creamos el usuario
        int id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            Usuario usuario = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Modificamos usuario
        jpa.withTransaction(() -> {
            Usuario busqueda = UsuarioDAO.find(id);
            busqueda.login = "pruebaUpdate"; //no hace falta llamar al update
        });

        //Comprobamos que se ha cambiado
        jpa.withTransaction(() -> {
            Usuario usuarioModificado = UsuarioDAO.find(id); //volvemos a buscar
            assertThat(usuarioModificado.login, equalTo("pruebaUpdate"));
        });
    }

    /**
     * Comprobamos los siguientes métodos para borrar al usuario:
     *  - create
     *  - find
     *  - delete
     */
    @Test
    public void borraUsuario(){

        //Creamos el usuario
        int id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            Usuario usuario = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Eliminamos usuario
        jpa.withTransaction(() -> {
            UsuarioDAO.delete(id);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Usuario busqueda = UsuarioDAO.find(id);
            assertNull(busqueda);
        });
    }

    /**
     * Comprobamos los siguientes métodos para devolver la cantidad de usuarios creados:
     *  - create
     *  - findAll
     */
    @Test
    public void cantidadUsuarios(){

        //Creamos el usuario 1
        int id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            Usuario usuario = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Creamos el usuario 2
        int id2 = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("jorge", "jorge");
            Usuario usuario = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Comprobamos cantidad usuarios
        jpa.withTransaction(() -> {
            List<Usuario> usuarios = UsuarioDAO.findAll();
            assertEquals(2, usuarios.size()); //primero esperado, después real (al contrario que assertThat)
        });
    }
}