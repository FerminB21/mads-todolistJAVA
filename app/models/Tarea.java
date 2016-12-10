package models;

import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    @Constraints.Required
    public String descripcion;
    @ManyToOne
    @JoinColumn(name = "usuarioId")
    public Usuario usuario;
    public Integer estimacion;
    public Integer estado;
    @ManyToOne
    @JoinColumn(name = "proyectoId")
    public Proyecto proyecto;

    // Un constructor vacío necesario para JPA
    public Tarea() {
    }

    // El constructor principal con los campos obligatorios
    public Tarea(String descripcion) {
        this.descripcion = descripcion;
    }

    public Tarea copy() {
        Tarea nueva = new Tarea(this.descripcion);
        nueva.id = this.id;
        nueva.estimacion = this.estimacion;
        nueva.estado=this.estado;
        return nueva;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + ((descripcion == null) ? 0 : descripcion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        Tarea other = (Tarea) obj;
        // Si tenemos los ID, comparamos por ID
        if (id != null && other.id != null)
            return (id == other.id);
            // sino comparamos por campos obligatorios
        else {
            if (descripcion == null) {
                if (other.descripcion != null) return false;
            } else if (!descripcion.equals(other.descripcion)) return false;
        }
        return true;
    }

    /**
     * Comprueba si la tarea está asignada a un proyecto.
     * Si no está devuelve "Sin proyecto"
     * Si está, devuelve: Id - NombreProyecto
     * @return String
     */
    public String esAsignadaProyecto(){
        String asignadaAProyecto="Sin proyecto";
        if(proyecto != null){
            asignadaAProyecto = proyecto.id+"-"+proyecto.nombre;
        }
        return asignadaAProyecto;
    }

    public String toString() {
        return String.format("Tarea id: %s - descripción: %s - estimación: %s - estado: %s -proyecto: %s", id, descripcion, EstimacionTareaEnum.getById(estimacion),EstadoTareaEnum.getById(estado), esAsignadaProyecto());
    }

}
