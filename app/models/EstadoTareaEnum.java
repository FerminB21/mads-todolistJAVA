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
        for(EstadoTareaEnum e : values()) {
            if(e.id.equals(id)) return e;
        }
        return null;
    }
}
