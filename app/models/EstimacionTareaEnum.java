package models;

public enum EstimacionTareaEnum {
    SIN_ESTIMACION(0, ""),
    PEQUEÑO(1, "Pequeño"),
    MEDIANO(2, "Mediano"),
    GRANDE(3, "Grande");

    public final Integer id;
    public final String descripcion;

    private EstimacionTareaEnum(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
