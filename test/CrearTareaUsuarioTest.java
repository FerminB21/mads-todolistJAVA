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

public class CrearTareaUsuarioTest {

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
     * Crear tarea asociada a un usuario
     * Se utilizan las clases TareaDAO y UsuarioDAO.
     */
    @Test
    public void crearTareaUsuarioTest() {
        Integer tareaId = jpa.withTransaction(() -> {
            Tarea tarea = new Tarea("Resolver los ejercicios de programación");
            Usuario usuario = UsuarioDAO.find(2); //recuperamos usuario id 2 (Anabel)
            tarea.usuario = usuario; //se modifica pero no llama al update porque se creó sin JPA
            tarea = TareaDAO.create(tarea); //se creará ya asociado con un usuario
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos la tarea
            Tarea tarea = TareaDAO.find(tareaId);
            Usuario usuario = UsuarioDAO.find(2);
            // Comprobamos que se recupera también el usuario de la tarea
            assertEquals(tarea.usuario.nombre, usuario.nombre);

            // Comprobamos que se recupera la relación inversa: el usuario
            // contiene la nueva tarea
            assertEquals(usuario.tareas.size(), 2);
            assertTrue(usuario.tareas.contains(new Tarea("Resolver los ejercicios de programación")));
        });
    }

    @Test
    public void crearTareaUsuarioServiceTest(){
        Integer tareaId = jpa.withTransaction(() -> {
            Tarea tarea = new Tarea("Resolver los ejercicios de programación");
            TareasService.crearTareaUsuario(tarea, 2);
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos la tarea
            Tarea tarea = TareasService.findTareaUsuario(tareaId);
            Usuario usuario = UsuariosService.findUsuario(2);
            // Comprobamos que se recupera también el usuario de la tarea
            assertEquals(tarea.usuario.nombre, usuario.nombre);

            // Comprobamos que se recupera la relación inversa: el usuario
            // contiene la nueva tarea

            assertEquals(usuario.tareas.size(), 2);
            assertTrue(usuario.tareas.contains(new Tarea("Resolver los ejercicios de programación")));
        });
    }

    @Test
    public void crearTareaUsuarioNoExisteErrorTest(){
       jpa.withTransaction(() -> {
            try{
                Tarea tarea = new Tarea("Resolver los ejercicios de programación");
                TareasService.crearTareaUsuario(tarea, 20);
                fail("Debería haberse lanzado la excepción. No se puede crear tarea con usuario que no existe");
            }catch(UsuariosException ex){
            }
        });
    }
}
