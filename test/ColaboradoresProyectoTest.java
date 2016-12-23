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

public class ColaboradoresProyectoTest {
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
                FileInputStream("test/resources/colaboradores_dataset.xml"));
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
     * Test para comprobar que funciona el asignar colaborador a un proyecto y el borrado de proyectos con colaboradores
     * Primero, se le asigna un colaborador a un proyecto y se comprueba que se ha creado la relación
     * Segundo, ese proyecto se elimina y se comprueba que efectivamente se ha podido realizar el borrado
     */
    @Test
    public void asignarColaboradorBorrarProyectoTest() {
        Integer idProyecto = jpa.withTransaction( () -> {
            Proyecto proyecto = ProyectoDAO.find( 10 );

            Proyecto desconectado = proyecto.copy();

            ProyectosService.modificaProyectoUsuario( desconectado, 20 );
            assertEquals( true, desconectado.colaborador( 20 ) );

            return desconectado.id;
        });

        jpa.withTransaction( () -> {
            ProyectosService.deleteProyecto( idProyecto );

            Proyecto busqueda = ProyectoDAO.find( idProyecto );
            assertNull(busqueda);
        });
    }

}
