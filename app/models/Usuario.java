package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @Constraints.Required
    public String login;
    public String password;
    public String nombre;
    public String apellidos;
    public String eMail;
    @Formats.DateTime(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    public Date fechaNacimiento;
    @OneToMany(mappedBy = "usuario")
    public List<Tarea> tareas = new ArrayList<Tarea>();
    @OneToMany(mappedBy = "usuario")
    public List<Proyecto> proyectos = new ArrayList<Proyecto>();
    @OneToMany(mappedBy="usuario")
    public List<Comentario> comentarios = new ArrayList<>();
    @ManyToMany
    @JoinTable(
        name="COL_PROJ",
        joinColumns=@JoinColumn(name="COL_ID", referencedColumnName="id"),
        inverseJoinColumns=@JoinColumn(name="PROJ_ID", referencedColumnName="id")
    )
    public List<Proyecto> colaboraciones = new ArrayList<Proyecto>();

    // Un constructor vacío necesario para JPA
    public Usuario() {
    }

    // El constructor principal con los campos obligatorios
    public Usuario(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // El constructor copia (todos menos el id)
    public void copiarDatos(Usuario usuario) {
        this.login = usuario.login;
        this.password = usuario.password;
        this.nombre = usuario.nombre;
        this.apellidos = usuario.apellidos;
        this.eMail = usuario.eMail;
        this.fechaNacimiento = usuario.fechaNacimiento;
    }

    // Sustituye por null todas las cadenas vacías que pueda tener
    // un usuario en sus atributos
    public void nulificaAtributos() {
        if (nombre != null && nombre.isEmpty()) nombre = null;
        if (apellidos != null && apellidos.isEmpty()) apellidos = null;
        if (eMail != null && eMail.isEmpty()) eMail = null;
    }

    public String toString() {
        String fechaStr = null;
        if (fechaNacimiento != null) {
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            fechaStr = formateador.format(fechaNacimiento);
        }
        return String.format("Usuario id: %s login: %s passworld: %s nombre: %s " +
                        "apellidos: %s eMail: %s fechaNacimiento: %s",
                id, login, password, nombre, apellidos, eMail, fechaStr);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        Usuario other = (Usuario) obj;
        // Si tenemos los ID, comparamos por ID
        if (id != 0 && other.id != 0)
            return (id == other.id);
            // sino comparamos por campos obligatorios
        else {
            if (login == null) {
                if (other.login != null) return false;
            } else if (!login.equals(other.login)) return false;
            if (password == null) {
                if (other.password != null) return false;
            } else if (!password.equals(other.password)) return false;
        }
        return true;
    }

    /**
     * Se crea dicha función (TIC-17)
     * Copia los datos de un objeto a otro
     *
     * @return Usuario
     */
    public Usuario copy() {
        Usuario nuevo = new Usuario();
        nuevo.id = this.id;
        nuevo.login = this.login;
        nuevo.password = this.password;
        nuevo.apellidos = this.apellidos;
        nuevo.eMail = this.eMail;
        nuevo.fechaNacimiento = this.fechaNacimiento;
        return nuevo;
    }

}
