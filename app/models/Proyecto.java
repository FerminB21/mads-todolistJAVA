package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    @Constraints.Required
    public String nombre;
    @ManyToOne
    @JoinColumn(name = "usuarioId")
    public Usuario usuario;
    @OneToMany(mappedBy = "proyecto")
    public List<Tarea> tareas = new ArrayList<Tarea>();
    @OneToMany(mappedBy="proyecto")
    public List<Comentario> comentarios = new ArrayList<Comentario>();

    // Un constructor vacío necesario para JPA
    public Proyecto() {
    }

    // Un constructor a partir del nombre
    public Proyecto( String nombre ) {
        this.nombre = nombre;
    }

    public Boolean propietario( Integer idUsuario ) {
        return usuario.id == idUsuario;
    }

    public Proyecto copy() {
        Proyecto nuevo = new Proyecto(this.nombre);
        nuevo.id = this.id;
        nuevo.nombre = this.nombre;
        return nuevo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        Proyecto other = (Proyecto) obj;
        // Si tenemos los ID, comparamos por ID
        if (id != null && other.id != null)
            return (id == other.id);
            // sino comparamos por campos obligatorios
        else {
            if (nombre == null) {
                if (other.nombre != null) return false;
            } else if (!nombre.equals(other.nombre)) return false;
        }
        return true;
    }

    public String toString() {
        return String.format("Proyecto id: %s nombre: %s", id, nombre);
    }
}
