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

import play.*;
import play.mvc.*;

public class EditarTareaUsuarioTest {

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
     * Editar tarea asociada a un usuario
     * Se utilizan las clases TareaDAO y UsuarioDAO.
     */
    @Test
    public void editarDescripcionTareaUsuarioTest() {
        Integer tareaId = jpa.withTransaction(() -> {

            Tarea tarea = TareaDAO.find(2);
            tarea.descripcion = "Resolver los ejercicios de matemáticas";
            tarea = TareaDAO.update(tarea);
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos la tarea
            Tarea tarea = TareaDAO.find(tareaId);
            assertEquals(tarea.descripcion, "Resolver los ejercicios de matemáticas");
        });
    }

    /**
     * Editar tarea asociada a un usuario. Se le cambia el usuario a uno EXISTENTE
     * Se utilizan las clases TareasService y UsuarioDAO.
     */
    @Test
    public void editarTareaUsuarioServiceTest() {
        Integer tareaId = jpa.withTransaction(() -> {

            Tarea tarea = TareasService.findTareaUsuario(2);
            tarea.usuario = UsuariosService.findUsuario(2); //se le cambia al usuario 2
            tarea = TareasService.modificaTareaUsuario(tarea);
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos la tarea
            Tarea tarea = TareaDAO.find(tareaId);
            Usuario usuario = UsuariosService.findUsuario(2);
            assertEquals(tarea.usuario, usuario);
        });
    }

    /**
     * Editar tarea asociada a un usuario. Se le cambia el usuario a uno NO EXISTENTE
     * Se utilizan las clases TareasService
     */
    @Test
    public void editarTareaUsuarioNoExisteServiceTest() {
        //Comprobamos antes el usuario 1 cuántas tareas tiene asociada
        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(1);
            assertEquals(3, usuario.tareas.size());
        });

        jpa.withTransaction(() -> {
            //Simulamos, fuera del entorno JPA, la tarea 1 con el usuario 20 no existente
            Tarea tarea = new Tarea("Hola, esto es una prueba");
            Usuario usuario = new Usuario("manuel", "prueba");
            tarea.usuario = usuario;
            tarea.id = 1; //la tarea creada le ponemos un id existente
            //para que una vez se llame al Service, JPA por dentro sepa enlazar esta tarea a la id 1.

            try {
                tarea.usuario.id = 20;
                tarea = TareasService.modificaTareaUsuario(tarea);
                fail("Debería haberse lanzado la excepción. No se puede cambiar la tarea a un usuario que no existe.");
            } catch (ServiceException ex) {
            }
        });

        //Comprobamos que sigue teniendo las mismas tareas
        //que no se ha creado implícitamente la tarea con usuario 20
        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(1);
            assertEquals(3, usuario.tareas.size());
        });
    }

    //////////////////////////
    /////////////////////////



    @Test
    public void editarTareaDeUsuarioTest() {
        Integer tareaId = jpa.withTransaction(() -> {
            Tarea tarea =TareasService.findTareaPorUsuario(4, 2);
            Logger.debug("Se obtiene tarea Test: " + tarea);
          //  Proyecto proyecto = ProyectoDAO.find(2);
            tarea.descripcion = "play app";
            //proyecto = ProyectoDAO.update(proyecto);
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos el proyecto
            Tarea tarea = TareaDAO.find(tareaId);
            assertEquals(tarea.descripcion, "play app");
        });
    }


    @Test
    public void editarTareaDeUsuarioLanzaExcepcionTest() {
         jpa.withTransaction(() -> {
           try {
                 Tarea tarea =TareasService.findTareaPorUsuario(3, 100);
               fail("No se ha lanzado excepción Tarea no pertenece a usuario"); //esperamos error
           } catch (ServiceException ex) {
           }


        });

    }


    @Test
    public void editarTareaDAODeUsuarioNullTest() {
        Integer tareaId = jpa.withTransaction(() -> {
            Tarea tarea = TareaDAO.findTareaUsuario(4, 2);
            Logger.debug("Se obtiene tarea Test: " + tarea);
          //  Proyecto proyecto = ProyectoDAO.find(2);
            tarea.descripcion = "play app";
            //proyecto = ProyectoDAO.update(proyecto);
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos el proyecto
            Tarea tarea = TareaDAO.find(tareaId);
            assertEquals(tarea.descripcion, "play app");
        });
    }


    @Test
    public void editarTareaDAODeUsuarioTest() {
         jpa.withTransaction(() -> {

                 Tarea tarea=TareaDAO.findTareaUsuario(2, 100);
                 assertNull(tarea);


        });

    }

}
