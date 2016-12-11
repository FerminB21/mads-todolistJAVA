import models.Tarea;
import models.TareaDAO;
import models.Usuario;
import models.UsuarioDAO;
import models.EstadoTareaEnum;
import org.dbunit.JndiDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.*;
import play.db.Database;
import play.db.Databases;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;
import services.TareasService;
import services.ServiceException;
import services.UsuariosService;
import java.util.Date;
import java.text.*;
import play.data.format.Formats;
import play.Logger;

import java.io.FileInputStream;

import static org.junit.Assert.*;

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
    public void crearTareaUsuarioServiceTest() {
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
    public void crearTareaUsuarioNoExisteErrorTest() {
        jpa.withTransaction(() -> {
            try {
                Tarea tarea = new Tarea("Resolver los ejercicios de programación");
                TareasService.crearTareaUsuario(tarea, 20);
                fail("Debería haberse lanzado la excepción. No se puede crear tarea con usuario que no existe");
            } catch (ServiceException ex) {
            }
        });
    }

    @Test
    public void crearTareaUsuarioConEstimacionTest() {
        Integer tareaId = jpa.withTransaction(() -> {
            Tarea tarea = new Tarea("Resolver los ejercicios de programación");
            Usuario usuario = UsuarioDAO.find(2);
            tarea.usuario = usuario;
            tarea.estimacion = 1; // se le añade una estimación
            tarea = TareaDAO.create(tarea);
            return tarea.id;
        });

        jpa.withTransaction(() -> {
            Tarea tarea = TareaDAO.find(tareaId);
            // Comprobamos que efectivamente ha guardado el valor de la estimación
            assertTrue(tarea.estimacion == 1);
        });
    }




    //////////////////////////////
    /**
     * crear una tarea y asignarle un estado y asignarla a un usuario
     *
     */
    @Test
    public void crearTareaConEstadoTest() {
        //Comprobamos antes el usuario 1 cuántas tareas tiene asociada
        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(1);
            assertEquals(3, usuario.tareas.size());

        });

        Integer tareaId =jpa.withTransaction(() -> {
            //se crea una tarea nueva
            Tarea tarea = new Tarea("Hola, esto es una prueba");
            tarea.estado=1;//se asigna un estado a esta tarea
            tarea.estimacion=1;

            TareasService.crearTareaUsuario(tarea, 1);//se crea la tarea y se asigna al usuario
            return tarea.id;
        });


        jpa.withTransaction(() -> {
            Tarea tarea = TareasService.findTareaUsuario(tareaId);
            //se comprueba si la tarea tiene el estado asignado
            assertEquals(EstadoTareaEnum.getById(tarea.estado).toString(), "Sin Empezar");
        });

        //Comprobamos si se ha sumado una tarea mas el usuario con id 1

        jpa.withTransaction(() -> {
            Usuario usuario = UsuariosService.findUsuario(1);
            assertEquals(4, usuario.tareas.size());
        });
    }




////////////////////modificar estado de tarea

@Test
public void crearModificarTareaConEstadoTest() {
    //Comprobamos antes el usuario 1 cuántas tareas tiene asociada
    jpa.withTransaction(() -> {
        Usuario usuario = UsuariosService.findUsuario(2);
        assertEquals(1, usuario.tareas.size());
    });

    Integer tareaId =jpa.withTransaction(() -> {
        //se crea una tarea nueva
        Tarea tarea = new Tarea("Hola, esto es una prueba");
        tarea.estado=1;//se asigna el estado sin empezar a la tarea creada
        tarea.estimacion=1;


        TareasService.crearTareaUsuario(tarea, 2);//asignar la tarea al usuario con id igual a dos
        return tarea.id;
    });


    jpa.withTransaction(() -> {
        Tarea tarea = TareasService.findTareaUsuario(tareaId);
        tarea.estado=2;//aqui modifico el estado a iniciado
        Tarea tarea2=TareasService.modificaTareaUsuario(tarea);
        //se comprueba si se ha modificado el estado
        assertEquals(EstadoTareaEnum.getById(tarea2.estado).toString(), "Iniciada");
    });

    //se comprueba si el usuario tiene otra tarea mas
    jpa.withTransaction(() -> {
        Usuario usuario = UsuariosService.findUsuario(2);
        assertEquals(2, usuario.tareas.size());
    });
}

///tests sobre la capa Tarea DatabaseOperation

@Test
public void crearTareaUsuarioDAOTest() {
    Integer tareaId = jpa.withTransaction(() -> {
      //se crea una tarea nueva
        Tarea tarea = new Tarea("Resolver los ejercicios de programación");
        Usuario usuario = UsuarioDAO.find(2); //recuperamos usuario id 2 (Anabel)
        tarea.usuario = usuario; //se modifica pero no llama al update porque se creó sin JPA
        tarea.estado=1;
        tarea = TareaDAO.create(tarea); //se creará ya asociado con un usuario
        return tarea.id;
    });

    jpa.withTransaction(() -> {
        //Recuperamos la tarea
        Tarea tarea = TareaDAO.find(tareaId);
        Usuario usuario = UsuarioDAO.find(2);
        // Comprobamos que se recupera también el usuario de la tarea
        assertEquals(tarea.usuario.nombre, usuario.nombre);
        //se comprueba si se ha cambiado el estado de la tarea
        assertEquals(EstadoTareaEnum.getById(tarea.estado).toString(), "Sin Empezar");
        // Comprobamos que se recupera la relación inversa: el usuario
        // contiene la nueva tarea
        assertEquals(usuario.tareas.size(), 2);

    });
}




@Test
public void crearModificarTareaUsuarioDAOTest() {
    Integer tareaId = jpa.withTransaction(() -> {
        Tarea tarea = new Tarea("Resolver los ejercicios de programación");
        Usuario usuario = UsuarioDAO.find(2); //recuperamos usuario id 2 (Anabel)
        tarea.usuario = usuario; //se modifica pero no llama al update porque se creó sin JPA
        tarea.estado=1;
        tarea = TareaDAO.create(tarea); //se creará ya asociado con un usuario
        return tarea.id;
    });

    jpa.withTransaction(() -> {
        //Recuperamos la tarea
        Tarea tarea = TareaDAO.find(tareaId);
        assertEquals(EstadoTareaEnum.getById(tarea.estado).toString(), "Sin Empezar");
        tarea.estado=2;//aqui modifico el estado a iniciado y se comprueba si se ha modificado el estado
        Tarea tarea2=TareaDAO.update(tarea);
        assertEquals(EstadoTareaEnum.getById(tarea2.estado).toString(), "Iniciada");

    });
}


/////creacion de tarea con fecha de finalizacion

//test sobre la capa Services

@Test
public void crearTareaUsuarioConFechaFinalizacionTest() throws ParseException{
    Integer tareaId = jpa.withTransaction(() -> {

        Tarea tarea = new Tarea("Tarea con fecha de finalizacion");//creacion de la tarea
        tarea.estimacion = 1; // se le añade una estimación
        tarea.estado=1;//se le añade un estado
          SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
          //Date a=null;
          try{
             tarea.fechaFinTarea=formatter.parse("22-10-2017");//se le añade una fecha de finalizacion a la tarea
          }catch(ParseException e){
          }
             tarea = TareasService.crearTareaUsuario(tarea,2);//se crea la tarea
             return tarea.id;
    });

    jpa.withTransaction(() -> {
        Tarea tarea = TareasService.findTareaUsuario(tareaId);
        //se convierte una fecha a tipo string
        String myDate=new SimpleDateFormat("dd-MM-yyyy").format(tarea.fechaFinTarea);
        //se crea una fecha con formato string
        String myotherDate="22-10-2017";

        assertEquals(myDate ,myotherDate);//se comprueba el valor deseado con el valor real

    });
}

//test sobre la capa DAO para cmprobar si se ha hecho la creacion de la tarea con la fecha de finalizacion
@Test
public void crearTareaUsuarioConFechaFinalizacionDAOTest() throws ParseException{
    Integer tareaId = jpa.withTransaction(() -> {
        //se crea la tarea
        Tarea tarea = new Tarea("Tarea con fecha de finalizacion");
        //se le asocia un usuario
        Usuario usuario = UsuarioDAO.find(2);
        tarea.usuario = usuario;
        //se le asigna una estimacion
        tarea.estimacion = 1;
        // se le añade un estado
        tarea.estado=1;


          SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
          //Date a=null;
          try{
            //se le añade una fecha de finalizacion
             tarea.fechaFinTarea=formatter.parse("22-10-2016");
          }catch(ParseException e){
          }
             tarea = TareaDAO.create(tarea);
             return tarea.id;
    });

    jpa.withTransaction(() -> {
        Tarea tarea = TareaDAO.find(tareaId);
        //conseguir la fecha de finalizacion de la tarea que acabamos de crear
        String myDate=new SimpleDateFormat("dd-MM-yyyy").format(tarea.fechaFinTarea);
        //se crea una fecha de tipo string, porque la fecha devuelta por la base de datos, se convierte a string
        String myotherDate="22-10-2016";
        //se comprueba si el valor deseado coincide con el valor real
        assertEquals(myDate ,myotherDate);

    });
}

}
