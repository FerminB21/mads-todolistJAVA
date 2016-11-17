import models.Proyecto;
import models.ProyectoDAO;
import org.dbunit.JndiDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;
import play.db.Database;
import play.db.Databases;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;
import services.ProyectosService;

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by mads on 17/11/16.
 */
public class BorrarProyectoTest {
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
                FileInputStream("test/resources/proyectos_dataset.xml"));
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(initialDataSet);
        databaseTester.onSetup();
    }

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
     * Los tests son de igual funcionamiento que para Tarea ya que tiene el mismo modo de funcionamiento, mismo
     * nº de parámetros, etc.
     */

    /**
     * Borrar proyecto asociado a un usuario
     * Se utilizan las clases ProyectoDAO.
     */
    @Test
    public void eliminarProyectoUsuario() {
        //Borramos el proyecto
        jpa.withTransaction(() -> {
            Proyecto proyecto = ProyectoDAO.find(2);
            ProyectoDAO.delete(proyecto.id);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Proyecto busqueda = ProyectoDAO.find(2);
            assertNull(busqueda);
        });
    }

    /**
     * Borrar proyecto EXISTENTE asociado a un usuario
     * Se utilizan las clases ProyectosService.
     */
    @Test
    public void eliminarProyectoUsuarioExistente() {

        //Eliminamos proyecto existente
        jpa.withTransaction(() -> {
            Boolean borrado = ProyectosService.deleteProyecto(2);
            //Basta con comprobar tan solo variable boolean
            assertEquals(true, borrado);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Proyecto busqueda = ProyectosService.findProyectoUsuario(2);
            assertNull(busqueda);
        });
    }

    /**
     * Borrar proyecto NO EXISTENTE
     * Se utilizan las clases ProyectosService.
     */
    @Test
    public void eliminarProyectoNoExistenteUsuarioServiceTest() {

        //Eliminamos proyecto NO existente
        jpa.withTransaction(() -> {
            Boolean borrado = ProyectosService.deleteProyecto(10);
            //Basta con comprobar tan solo variable boolean
            assertEquals(false, borrado);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Proyecto busqueda = ProyectosService.findProyectoUsuario(10);
            assertNull(busqueda);
        });
    }
}
