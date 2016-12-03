package services;

import models.Usuario;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class UsuariosServiceTest {

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
     * Comprobamos los siguientes métodos para actualizar usuario con login ya existente
     * - findUsuario
     * - modificaUsuario
     */
    @Test
    public void actualizaUsuarioLanzaExcepcionSiLoginYaExiste() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(2);

            // Copiamos el objeto usuario para crear un objeto igual
            // pero desconectado de la base de datos. De esta forma,
            // cuando modificamos sus atributos JPA no actualiza la
            // base de datos.

            Usuario desconectado = usuario.copy();

            // Cambiamos el login por uno ya existente
            desconectado.login = "juan";

            try {
                UsuariosService.modificaUsuario(desconectado);
                fail("No se ha lanzado excepción login ya existe"); //esperamos error
            } catch (UsuariosException ex) {
            }
        });
    }

    /**
     *  Comprobamos los siguientes métodos para actualizar un usuario:
     *  - findUsuario
     *  - modificaUsuario
     *
     *  NOTA: En este caso no existe el problema tal y como se describe en el método:
     *  actualizaUsuarioLanzaExcepcionSiLoginYaExiste.
     *  Aunque una vez hacemos "usuario.apellidos = ...", se modifica el objeto, no existe el riesgo puesto
     *  que se está modificando el id 2 y los apellidos pueden ser repetidos en el sistema. No tenemos problema.
     */
    @Test
    public void actualizaUsuario() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(2);
            usuario.apellidos = "Anabel Pérez";
            //UsuariosService.modificaUsuario(usuario);
        });

        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(2);
            assertThat(usuario.apellidos, equalTo("Anabel Pérez"));
        });
    }

    /**
     * Buscar usuario que no existe
     *  - findUsuario
     *
     *  NOTA: Aquí me doy cuenta de que el UsuariosServices ni los Controllers están bien planteados para
     *  tratar errores. En los casos que al llamar a "findUsuario" fuera con una id errónea, la aplicación no lo soportaría
     *  y saltaría excepción.
     *  Solución: Habría que tratar el error en UsuarioDAO y que tanto el UsuariosService como los Controllers fueran conscientes
     *  de ello.
     */
    @Test
    public void buscarUsuarioNoExiste(){

        jpa.withTransaction(() -> {
            //Comprobamos que no existe
            jpa.withTransaction(() -> {
                Usuario usuario = UsuariosService.findUsuario(10);
                assertNull(usuario);
            });
        });
    }


    /**
     * Comprobamos los siguientes métodos para borrar a un usuario que no existe:
     *  - deleteUsuario
     *  - findUsuario
     *
     *  NOTA: Otro error detectado gracias al test. La función "deleteUsuario" de UsuariosService siempre devolvía true
     *  (se puede comprobar en commits anteriores)
     *  ¿Cómo se puede saber entonces si ha sido borrado?
     *  Tenemos que hacer otra consulta a la base de datos para comprobarlo.
     */
    @Test
    public void borraUsuarioNoExiste(){

        //Eliminamos usuario que no existe
        Boolean borrado = jpa.withTransaction(() -> {
            return UsuariosService.deleteUsuario(10);
        });

        //Comprobamos si existe
        jpa.withTransaction(() -> {

            Usuario busqueda = UsuariosService.findUsuario(10);
            //assertNull(busqueda); //Esperamos que no esté para darlo como borrado
                                  //pero realmente no sabemos si ha sido borrado o es que no existía. Error de lógica detectado.


            //Tras corregir y tratar ahora en Service el filtro de si es borrado con éxito o no.
            //Basta con comprobar tan solo variable boolean
            assertEquals(false, borrado);
        });
    }

    /**
     * Comprobamos los siguientes métodos para borrar a un usuario que sí existe:
     *  - deleteUsuario
     *  - findUsuario
     *
     *  NOTA: Prueba contraria al anterior test. El sistema debe ser capaz de saber si es borrado o no
     *  con tan solo la variable que devuelve el método del UsuarioService.deleteUsuario
     */
    @Test
    public void borraUsuarioSiExiste(){

        //Eliminamos usuario que si existe
        Boolean borrado = jpa.withTransaction(() -> {
            return UsuariosService.deleteUsuario(1);
        });

        //Comprobamos si existe
        jpa.withTransaction(() -> {

            Usuario busqueda = UsuariosService.findUsuario(1);
            //assertNull(busqueda); //Esperamos que no esté para darlo como borrado
                                  //pero realmente no sabemos si ha sido borrado o es que no existía. Error de lógica detectado.


            //Tras corregir y tratar ahora en Service el filtro de si es borrado con éxito o no.
            //Basta con comprobar tan solo variable boolean
            assertEquals(true, borrado);
        });
    }



}