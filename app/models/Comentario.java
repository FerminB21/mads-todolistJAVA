package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mads on 3/12/16.
 */
@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;
    @Constraints.Required
    public String comentario;
    @ManyToOne
    @JoinColumn(name="usuarioId")
    public Usuario usuario;
    @ManyToOne
    @JoinColumn(name="proyectoId")
    public Proyecto proyecto;
    @Formats.DateTime(pattern="dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    public Date fecha;

    public Comentario() {
    }

    public Comentario( String comentario ) {
        this.comentario = comentario;
    }

    public Boolean propietario( Integer idUsuario ) {
        return usuario.id == idUsuario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "comentario='" + comentario + '\'' +
                ", fecha=" + fecha +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comentario that = (Comentario) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (comentario != null ? !comentario.equals(that.comentario) : that.comentario != null) return false;
        if (usuario != null ? !usuario.equals(that.usuario) : that.usuario != null) return false;
        if (proyecto != null ? !proyecto.equals(that.proyecto) : that.proyecto != null) return false;
        return fecha != null ? fecha.equals(that.fecha) : that.fecha == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (comentario != null ? comentario.hashCode() : 0);
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        result = 31 * result + (proyecto != null ? proyecto.hashCode() : 0);
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        return result;
    }
}
