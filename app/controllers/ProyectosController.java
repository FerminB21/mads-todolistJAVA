package controllers;

import javax.inject.*;

import play.*;
import play.mvc.*;
import views.html.*;

public class ProyectosController extends Controller {

    @Inject FormFactory formFactory;

    @Transactional(readOnly = true)
    // Devuelve una página con la lista de proyectos
    public Result listaProyectos() {
       List<Proyecto> proyectos = ProyectosService.findAllProyectos();
       return ok(listaProyectos.render(proyectos));
    }
}
