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

public class CrearProyectoTest {

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
     * Crear proyecto asociado a un usuario
     * Se utilizan las clases ProyectoDAO y UsuarioDAO.
     */
     //<Proyecto id="3" usuarioId="1" nombre="Leer el libro de inglés" />
    @Test
    public void Proyecto1Test() {
        Integer ProyectoId = jpa.withTransaction(() -> {
            Proyecto proyecto = new Proyecto("mads");
            Usuario usuario = UsuarioDAO.find(2); //recuperamos usuario id 2 (Anabel)
            proyecto.usuario = usuario; //se modifica pero no llama al update porque se creó sin JPA
            proyecto = ProyectoDAO.create(proyecto); //se creará ya asociado con un usuario
            return proyecto.id;
        });

        jpa.withTransaction(() -> {
            //Recuperamos el proyecto
            Proyecto proyecto = ProyectoDAO.find(ProyectoId);
            Usuario usuario = UsuarioDAO.find(2);
            // Comprobamos que se recupera también el usuario del proyecto
            assertEquals(proyecto.usuario.nombre, usuario.nombre);

            // Comprobamos que se recupera la relación inversa: el usuario
            // contiene el nuevo proyecto
            assertEquals(usuario.proyectos.size(), 2);

        });
    }


        @Test
        public void Proyecto2Test() {
            Integer ProyectoId = jpa.withTransaction(() -> {
                Proyecto proyecto = new Proyecto("mads");
                Usuario usuario = UsuarioDAO.find(1); //recuperamos usuario id 1 (juan)
                proyecto.usuario = usuario; //se modifica pero no llama al update porque se creó sin JPA
                proyecto = ProyectoDAO.create(proyecto); //se creará ya asociado con un usuario
                return proyecto.id;
            });

            jpa.withTransaction(() -> {
                //Recuperamos el proyecto
                Proyecto proyecto = ProyectoDAO.find(ProyectoId);
                Usuario usuario = UsuarioDAO.find(1);
                // Comprobamos que se recupera también el usuario del proyecto
                assertEquals(proyecto.usuario.nombre, usuario.nombre);
                assertTrue(usuario.proyectos.contains(new Proyecto("mads")));
            });
    }

    @Test
    public void proyecto3Test(){
       jpa.withTransaction(() -> {
            try{
                Proyecto proyecto = new Proyecto("Resolver los ejercicios de programación");
                ProyectosService.crearProyectoUsuario(proyecto,20);
                fail("Debería haberse lanzado la excepción. No se puede crear proyecto con usuario que no existe");
            }catch(UsuariosException ex){
            }
        });
    }

}
