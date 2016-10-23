import org.hibernate.HibernateException;
import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.junit.*;
import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.*;
import services.*;

import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

public class BorrarTareaUsuarioTest {

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
                FileInputStream("test/resources/tareas_dataset.xml"));
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
     * Borrar tarea asociada a un usuario
     * Se utilizan las clases TareaDAO.
     */
    @Test
    public void eliminarTareaUsuarioTest() {
        //Borramos la tarea
        jpa.withTransaction(() -> {
            Tarea tarea = TareaDAO.find(2);
            TareaDAO.delete(tarea.id);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Tarea busqueda = TareaDAO.find(2);
            assertNull(busqueda);
        });
    }

    /**
     * Borrar tarea EXISTENTE asociada a un usuario
     * Se utilizan las clases TareaDAO.
     */
    @Test
    public void eliminarTareaUsuarioServiceTest() {

        //Eliminamos tarea existente
        jpa.withTransaction(() -> {
            Boolean borrado = TareasService.deleteTarea(2);
            //Basta con comprobar tan solo variable boolean
            assertEquals(true, borrado);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Tarea busqueda = TareasService.findTareaUsuario(2);
            assertNull(busqueda);
        });
    }

}