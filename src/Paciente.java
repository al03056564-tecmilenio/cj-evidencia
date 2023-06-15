public class Paciente extends BaseEntity{
    private String nombre;

    public Paciente(String identificador, String nombre) {
        this.setIdentificador(identificador);
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Paciente [Identificador: " + this.getIdentificador() + ", Nombre: " + nombre + "]";
    }
}