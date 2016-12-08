package dao;

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

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class UsuarioDaoDbUnitTest {

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
                FileInputStream("test/resources/usuarios_dataset.xml"));
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
     * Comprobamos los siguientes métodos para buscar un usuario por login
     * - findByLogin
     */
    @Test
    public void findUsuarioPorLogin() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.findByLogin("juan");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
            try {
                Date diezDiciembre93 = sdf.parse("10-12-1993");
                assertTrue(usuario.login.equals("juan") &&
                        usuario.fechaNacimiento.compareTo(diezDiciembre93) == 0);
            } catch (java.text.ParseException ex) {
            }
        });
    }

    /**
     *  Comprobamos los siguientes métodos para actualizar un usuario:
     *  - find
     *  - update
     */
    @Test
    public void actualizaUsuario() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(2);
            usuario.apellidos = "Anabel Pérez";
            UsuarioDAO.update(usuario);
        });

        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(2);
            assertThat(usuario.apellidos, equalTo("Anabel Pérez"));
        });
    }

    /**
     * Comprobamos los siguientes métodos para crear un usuario:
     *  - create
     *  - find
     */
    @Test
    public void creaUsuario(){

        //Creamos el usuario
        Integer id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Comprobamos que existe
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(id);
            assertThat(usuario.login, equalTo("pepe"));
        });
    }

    /**
     * Comprobamos los siguientes métodos para borrar al usuario:
     *  - find
     *  - delete
     */
    @Test
    public void borraUsuario(){

        //Borramos el usuario
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(1);
            UsuarioDAO.delete(usuario.id);
        });

        //Comprobamos que no existe
        jpa.withTransaction(() -> {
            Usuario busqueda = UsuarioDAO.find(1);
            assertNull(busqueda);
        });
    }

    /**
     * Comprobamos los siguientes métodos para devolver la cantidad de usuarios creados:
     *  - create
     *  - findAll
     */
    @Test
    public void cantidadUsuarios(){

        //Creamos el usuario 1
        Integer id = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("pepe", "pepe");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Creamos el usuario 2
        Integer id2 = jpa.withTransaction(() -> {
            Usuario nuevo = new Usuario("jorge", "jorge");
            nuevo = UsuarioDAO.create(nuevo);
            return nuevo.id;
        });

        //Comprobamos cantidad usuarios (deben ser 4 -> dos son los que hay originalmente más dos insertados)
        jpa.withTransaction(() -> {
            List<Usuario> usuarios = UsuarioDAO.findAll();
            assertEquals(4, usuarios.size()); //primero esperado, después real (al contrario que assertThat)
        });
    }

}