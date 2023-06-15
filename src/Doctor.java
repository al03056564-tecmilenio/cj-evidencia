public class Doctor extends BaseEntity{
    private String nombre;
    private String especialidad;

    public Doctor(String identificador, String nombre, String especialidad) {
        this.setIdentificador(identificador);
        this.nombre = nombre;
        this.especialidad = especialidad;
    }


    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    @Override
    public String toString() {
        return "Doctor [Identificador: " + this.getIdentificador() + ", Nombre: " + nombre + ", Especialidad: " + especialidad + "]";
    }
}