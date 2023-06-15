public class Cita {
    private String fechaHora;
    private Doctor doctor;
    private Paciente paciente;

    public Cita(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Fecha y Hora: ").append(fechaHora);

        if (doctor != null) {
            builder.append(", Doctor: ").append(doctor.getNombre());
        }

        if (paciente != null) {
            builder.append(", Paciente: ").append(paciente.getNombre());
        }

        builder.append("]");
        return builder.toString();
    }

}