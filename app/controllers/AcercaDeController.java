package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.acercaDe;


/**
 * Created by mads on 15/11/16.
 */
public class AcercaDeController extends Controller {

    // Devuelve la página acerca-de
    public Result acercaDe() {
        return ok(acercaDe.render());
    }
}
