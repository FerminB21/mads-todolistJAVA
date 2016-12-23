import models.Proyecto;
import models.Usuario;
import models.UsuarioDAO;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListadoProyectosTest {

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

    @Test
    public void obtenerProyectosDeUsuario() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(1);
            assertEquals(usuario.proyectos.size(), 3);
        });
    }

    @Test
    public void ListadoProyectosService() {
        jpa.withTransaction(() -> {
            List<Proyecto> proyectos = ProyectosService.listaProyectosUsuario(1);
            assertEquals(proyectos.size(), 3);

            // Comprobamos que los proyectos se devuelven ordenadas por id

            //Se refactoriza la función
            Proyecto anterior = proyectos.get(0);
            for (int i = 1; i < proyectos.size(); i++) {
                Proyecto p = proyectos.get(i);
                if (anterior != null) {
                    assertTrue(anterior.id < p.id);
                    anterior = p;
                }
            }
        });
    }
}
