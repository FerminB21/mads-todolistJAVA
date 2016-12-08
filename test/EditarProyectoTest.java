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
public class EditarProyectoTest {
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
     * Editar nombre del proyecto
     * Se utilizan las clases ProyectoDAO y UsuarioDAO.
     */
    @Test
    public void editarNombreProyectoTest() {
        Integer proyectoId = jpa.withTransaction(() -> {

            Proyecto proyecto = ProyectoDAO.find(2);
            proyecto.nombre = "Correr 10 km";
            proyecto = ProyectoDAO.update(proyecto);
            return proyecto.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos el proyecto
            Proyecto proyecto = ProyectoDAO.find(proyectoId);
            assertEquals(proyecto.nombre, "Correr 10 km");
        });
    }


    @Test
    public void editarProyectoDeUsuarioTest() {
        Integer proyectoId = jpa.withTransaction(() -> {
            Proyecto proyecto =ProyectosService.findProyectoPorUsuario(4, 2);
            Logger.debug("Se obtiene proyecto Test: " + proyecto);
          //  Proyecto proyecto = ProyectoDAO.find(2);
            proyecto.nombre = "play app";
            //proyecto = ProyectoDAO.update(proyecto);
            return proyecto.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos el proyecto
            Proyecto proyecto = ProyectoDAO.find(proyectoId);
            assertEquals(proyecto.nombre, "play app");
        });
    }


    @Test
    public void editarProyectoDeUsuarioLanzaExcepcionTest() {
         jpa.withTransaction(() -> {
           try {
                 Proyecto proyecto =ProyectosService.findProyectoPorUsuario(3, 100);
               fail("No se ha lanzado excepción proyecto no pertenece a usuario"); //esperamos error
           } catch (ServiceException ex) {
           }


        });

    }


    @Test
    public void editarProyectoDAODeUsuarioNullTest() {
        Integer proyectoId = jpa.withTransaction(() -> {
            Proyecto proyecto =ProyectoDAO.findProyectoUsuario(4, 2);
            Logger.debug("Se obtiene proyecto Test: " + proyecto);
          //  Proyecto proyecto = ProyectoDAO.find(2);
            proyecto.nombre = "play app";
            //proyecto = ProyectoDAO.update(proyecto);
            return proyecto.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos el proyecto
            Proyecto proyecto = ProyectoDAO.find(proyectoId);
            assertEquals(proyecto.nombre, "play app");
        });
    }


    @Test
    public void editarProyectoDAODeUsuarioTest() {
         jpa.withTransaction(() -> {

                 Proyecto proyecto =ProyectoDAO.findProyectoUsuario(2, 100);
                 assertNull(proyecto);


        });

    }
}
