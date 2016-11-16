package services;

import java.util.List;
import java.util.ArrayList;

import models.*;

public class ProyectosService {

    public static List<Proyecto> listaProyectosUsuario(Integer usuarioId) {
        Usuario usuario = UsuarioDAO.find(usuarioId);
        if (usuario != null) {
            return usuario.proyectos;
        } else {
            throw new UsuariosException("Usuario no encontrado");
        }
    }
}
