package models;

public enum EstadoTareaEnum {
    SIN_ASIGNAR(0, ""),
    SIN_EMPEZAR(1, "Sin Empezar"),
    INICIADA(2, "Iniciada"),
    TERMINADA(3, "Terminada");


    public final Integer id;
    public final String descripcion;

    private EstadoTareaEnum(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

    public static EstadoTareaEnum getById(Integer id) {
      //String estadoTarea="Sin Estado";
        for(EstadoTareaEnum e : values()) {
            if(e.id.equals(id)) return e;
        }
        return null;
    }

    public static int getIdByDescripcion(String descripcion){
        for(EstadoTareaEnum e : values()) {
            if(e.descripcion.equals(descripcion)) return e.id;
        }
        return -1; //Si devolvemos 0 estamos devolviendo un estado, así que se devuelve -1 para indicar
                   //que no es correcto
    }
}
