import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.junit.*;
import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;

import play.*;
import play.mvc.*;


import javax.persistence.*;
import java.io.FileInputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.*;
import services.*;

/**
 * Created by mads on 17/11/16.
 */
public class DashboardTest {
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
                FileInputStream("test/resources/proyectos_tareas_dataset.xml"));
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
     * Busca los proyectos con más tareas. Devuelve lista de proyectos ordenados por nº de tareas.
     * Se utilizan las clases ProyectoDAO.
     */
    @Test
    public void buscarProyectosConMasTareas() {

        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de proyectos adecuados según el usuario pasado
            List<Proyecto> proyectos = ProyectoDAO.findProyectosConMasTareas(1);
            assertEquals(proyectos.size(), 2);


            // Comprobamos que se devuelven en el orden esperado
            assertTrue(proyectos.get(0).tareas.size() == 2);
            assertTrue(proyectos.get(1).tareas.size() == 1);

        });
    }

    /**
     * Busca los proyectos con más tareas. Devuelve lista de proyectos ordenados por nº de tareas.
     * Se utilizan las clases ProyectosService.
     */
    @Test
    public void buscarProyectosConMasTareasService() {

        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de proyectos adecuados según el usuario pasado
            List<Proyecto> proyectos = ProyectosService.findProyectosConMasTareas(1);
            assertEquals(proyectos.size(), 2);


            // Comprobamos que se devuelven en el orden esperado
            assertTrue(proyectos.get(0).tareas.size() == 2);
            assertTrue(proyectos.get(1).tareas.size() == 1);

        });
    }

    /**
     * Busca los proyectos con más comentarios. Devuelve lista de proyectos ordenados por nº de comentarios.
     * Se utilizan las clases ProyectosService.
     */
    @Test
    public void buscarProyectosConMasComentarios() {

        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de proyectos adecuados según el usuario pasado
            List<Proyecto> proyectos = ProyectosService.findProyectosConMasComentarios(1);
            assertEquals(proyectos.size(), 2);

            // Comprobamos que se devuelven en el orden esperado
            assertTrue(proyectos.get(0).comentarios.size() == 3);
            assertTrue(proyectos.get(1).comentarios.size() == 1);

        });
    }

    /**
     * Busca las tareas abiertas. Devuelve una lista de tareas con estado != 3
     * Se utilizan las clases TareaDAO y TareasService.
     */
    @Test
    public void buscarTareasAbiertas() {

        //Capa DAO
        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de tareas adecuadas según el usuario pasado
            List<Tarea> tareas = TareaDAO.findTareasAbiertas(1);
            assertEquals(tareas.size(), 1);

            // Comprobamos que se devuelven en el orden esperado
            assertTrue(tareas.get(0).estado != 3);
        });

        //Capa Service
        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de tareas adecuadas según el usuario pasado
            List<Tarea> tareas = TareasService.findTareasAbiertas(1);
            assertEquals(tareas.size(), 1);

            // Comprobamos que se devuelven en el orden esperado
            assertTrue(tareas.get(0).estado != 3);

        });
    }

    /**
     * Busca las tareas finalizadas. Devuelve una lista de tareas con estado = 3
     * Se utilizan las clases TareaDAO y TareasService.
     */
    @Test
    public void buscarTareasFinalizadas() {

        //Capa Dao
        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de tareas adecuadas según el usuario pasado
            List<Tarea> tareas = TareaDAO.findTareasAcabadas(1);
            assertEquals(tareas.size(), 2);

            // Comprobamos que se devuelven en el orden esperado
            assertTrue(tareas.get(0).estado == 3);
            assertTrue(tareas.get(1).estado == 3);

        });

        //Capa Service
        jpa.withTransaction(() -> {
            //Comprobamos que devuelve el nº de tareas adecuadas según el usuario pasado
            List<Tarea> tareas = TareasService.findTareasAcabadas(1);
            assertEquals(tareas.size(), 2);

            // Comprobamos que se devuelven en el orden esperado
            assertTrue(tareas.get(0).estado == 3);
            assertTrue(tareas.get(1).estado == 3);

        });
    }
}
