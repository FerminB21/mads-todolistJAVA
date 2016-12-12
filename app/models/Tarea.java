package models;

import play.data.validation.Constraints;
import java.util.*;
import java.text.*;
import javax.persistence.*;
import java.util.Date;
import play.data.format.Formats;

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
    ////añadir fecha de finalizacion
    @Formats.DateTime(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    public Date fechaFinTarea;
    ///
    @ManyToOne
    @JoinColumn(name = "proyectoId")
    public Proyecto proyecto;
    public String color;

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
        nueva.color = this.color;
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

    public String tieneFechaFinalizacion(){
        String tieneFecha="Sin fecha de finalizacion";
        if(fechaFinTarea != null){
            String myDate=new SimpleDateFormat("dd-MM-yyyy").format(fechaFinTarea);
            tieneFecha = myDate.toString();
        }
        return tieneFecha;
    }

    public String tareaTieneEstado(){
        String tareaEstado="Sin Empezar";
        if( estado != null && estado != 0){
            return  EstadoTareaEnum.getById(estado).toString();
        }
        return tareaEstado;
    }

    public String toString() {
        return String.format("Tarea id: %s - descripción: %s - estimación: %s - estado: %s - fechFinalizacion: %s -proyecto: %s - color: %s", id, descripcion, EstimacionTareaEnum.getById(estimacion),EstadoTareaEnum.getById(estado),fechaFinTarea, esAsignadaProyecto(), color);
    }

}
