package models;

import javax.persistence.*;
import play.data.validation.Constraints;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Proyecto {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;
    @Constraints.Required
    public String nombre;
    @ManyToOne
    @JoinColumn(name="usuarioId")
    public Usuario usuario;
    @OneToMany(mappedBy="proyecto")
    public List<Tarea> tareas = new ArrayList<Tarea>();

    // Un constructor vacío necesario para JPA
    public Proyecto() {}

    // Un constructor a partir del nombre
    public Proyecto(String nombre) {
        this.nombre = nombre;
    }

    public String toString() {
        return String.format("Proyecto id: %s nombre: %s", id, nombre);
    }
}
