package controllers;

import play.mvc.*;

import views.html.*;


/**
 * Created by mads on 15/11/16.
 */
public class AcercaDeController extends Controller {

    // Devuelve la página acerca-de
    public Result acercaDe() {
        return ok(acercaDe.render());
    }
}
