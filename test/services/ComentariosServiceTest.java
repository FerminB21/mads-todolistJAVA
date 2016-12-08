package services;

import models.Comentario;
import org.dbunit.JndiDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;
import play.db.Database;
import play.db.Databases;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import java.io.FileInputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class ComentariosServiceTest {

    static Database db;
    static JPAApi jpa;
    JndiDatabaseTester databaseTester;

    @BeforeClass
    static public void initDatabase() {
        db = Databases.inMemoryWith("jndiName", "DefaultDS");
        // Necesario para inicializar el nombre JNDI de la BD
        db.getConnection();
        // Se activa la compatibilidad MySQL en la BD H2
        db.withConnection(connection -> {
            connection.createStatement().execute("SET MODE MySQL;");
        });
        jpa = JPA.createFor("memoryPersistenceUnit");
    }

    @Before
    public void initData() throws Exception {
        databaseTester = new JndiDatabaseTester("DefaultDS");
        IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new
                FileInputStream("test/resources/comentarios_dataset.xml"));
        databaseTester.setDataSet(initialDataSet);

        // Definimos como operación TearDown DELETE_ALL para que se
        // borren todos los datos de las tablas del dataset
        // (el valor por defecto DbUnit es DatabaseOperation.NONE)
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);

        // Definimos como operación SetUp CLEAN_INSERT, que hace un
        // DELETE_ALL de todas las tablase del dataset, seguido por un
        // INSERT. (http://dbunit.sourceforge.net/components.html)
        // Es lo que hace DbUnit por defecto, pero así queda más claro.
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);

        databaseTester.onSetup();
    }

    /**
     * NOTA: Se comprueba a quitar el "onTearDown" después de cada método. Así, podemos ver qué es lo que hace
     *  cuando llega a este método.
     */
    @After
    public void clearData() throws Exception {
        databaseTester.onTearDown();
    }

    @AfterClass
    static public void shutdownDatabase() {
        jpa.shutdown();
        db.shutdown();
    }

    /**
     * Comprueba que salta la excepción cuando no existe un usuario con el id pasado
     *
     */
    @Test
    public void creaComentarioUsuarioNoExisteTest() {
        jpa.withTransaction( () -> {
            Comentario nuevo = new Comentario( "Comentario de prueba" );
            try {
                ComentariosService.crearComentario( nuevo, 3, 1 );
                fail( "No se ha lanzado excepción usuario no existe" ); //esperamos error
            } catch( ServiceException e ) {
                assertThat( e.getMessage(), equalTo( "Usuario no encontrado" ) );
            }
        });
    }

    /**
     * Comprueba que salta la excepción cuando el usuario existe
     * pero no existe ningún proyecto con el id pasado
     */
    @Test
    public void creaComentarioProyectoNoExisteTest() {
        jpa.withTransaction( () -> {
            Comentario nuevo = new Comentario( "Comentario de prueba" );
            try {
                ComentariosService.crearComentario( nuevo, 1, 3 );
                fail( "No se ha lanzado excepción proyecto no existe" ); //esperamos error
            } catch( ServiceException e ) {
                assertThat( e.getMessage(), equalTo( "Proyecto no encontrado" ) );
            }
        });
    }

    /**
     * Comprueba que al crear un comentario se han creado bien las relaciones
     * Tras crear el comentario, tiene que tener asociado el usuario que lo escribe
     * Tras crear el comentario, tiene que tener asociado el proyecto donde se escribe
     */
    @Test
    public void creaComentarioConRelacionesTest() {
        int id = jpa.withTransaction( () -> {
            Comentario nuevo = new Comentario( "Comentario de prueba" );
            Comentario comentario = ComentariosService.crearComentario( nuevo, 1, 1 );
            return comentario.id;
        });

        jpa.withTransaction( () -> {
            Comentario busqueda = ComentariosService.getComentarioById( id );

            assertEquals( new Long( 1 ), new Long( busqueda.usuario.id ) );
            assertEquals( new Long( 1 ),new Long( busqueda.proyecto.id ) );
        });
    }

    /**
     * Comprueba que un usuario no puede modificar el comentario de otro usuario
     *
     */
    @Test
    public void editarComentarioSinSerPropietarioTest() {
        jpa.withTransaction( () -> {
            Comentario comentario = ComentariosService.getComentarioById( 3 );

            Comentario desconectado = comentario.copy();
            desconectado.comentario = "Comentario modificado";
            try {
                ComentariosService.editarComentario( desconectado, 2, 2 );
                fail( "No se ha lanzado excepción" ); //esperamos error
            } catch( ServiceException e ) {
                assertThat( e.getMessage(), equalTo( "No puedes editar este comentario." ) );
            }
        });
    }

    /**
     * Comprueba que un usuario puede modificar un comentario de un proyecto suyo
     * aunque el comentario no sea suyo
     */
    @Test
    public void editarComentarioPropietarioProyectoTest() {
        jpa.withTransaction( () -> {
            Comentario comentario = ComentariosService.getComentarioById( 2 );

            Comentario desconectado = comentario.copy();
            desconectado.comentario = "Comentario modificado";
            ComentariosService.editarComentario( desconectado, 1, 1 );
            assertThat( desconectado.comentario, equalTo( "Comentario modificado" ) );
        });
    }

    /**
     * Comprueba que un usuario no puede borrar el comentario de otro usuario
     *
     */
    @Test
    public void borrarComentarioSinSerPropietarioTest() {
        jpa.withTransaction( () -> {
            Comentario comentario = ComentariosService.getComentarioById( 3 );
            try {
                ComentariosService.borrarComentario( comentario.id, 2, 2 );
                fail( "No se ha lanzado excepción" ); //esperamos error
            } catch( ServiceException e ) {
                assertThat( e.getMessage(), equalTo( "No puedes borrar este comentario." ) );
            }
        });
    }

    /**
     * Comprueba que un usuario puede borrar un comentario de un proyecto suyo
     * aunque el comentario no sea suyo
     */
    @Test
    public void borrarComentarioPropietarioProyectoTest() {
        jpa.withTransaction( () -> {
            ComentariosService.borrarComentario( 2, 1, 1 );
        });

        jpa.withTransaction( () -> {
            Comentario comentario = ComentariosService.getComentarioById( 2 );
            assertNull( comentario );
        });
    }
}
