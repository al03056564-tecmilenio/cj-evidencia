import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAccess {
    private static List<Doctor> doctores = new ArrayList<>();
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Paciente> pacientes = new ArrayList<>();
    private static List<Cita> citas = new ArrayList<>();
    private String fileName;
    public DataAccess(String filename) {
        fileName = filename;
    }
    public void Guardar() {
        JSONObject jsonDatos = new JSONObject();

        JSONArray jsonUsuarios = new JSONArray();
        for (Usuario usuario : usuarios) {
            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("identificador", usuario.getIdentificador());
            jsonUsuario.put("contraseña", usuario.getContraseña());
            jsonUsuarios.add(jsonUsuario);
        }
        jsonDatos.put("usuarios", jsonUsuarios);

        JSONArray jsonDoctores = new JSONArray();
        for (Doctor doctor : doctores) {
            JSONObject jsonDoctor = new JSONObject();
            jsonDoctor.put("identificador", doctor.getIdentificador());
            jsonDoctor.put("nombre", doctor.getNombre());
            jsonDoctor.put("especialidad", doctor.getEspecialidad());
            jsonDoctores.add(jsonDoctor);
        }
        jsonDatos.put("doctores", jsonDoctores);

        JSONArray jsonPacientes = new JSONArray();
        for (Paciente paciente : pacientes) {
            JSONObject jsonPaciente = new JSONObject();
            jsonPaciente.put("identificador", paciente.getIdentificador());
            jsonPaciente.put("nombre", paciente.getNombre());
            jsonPacientes.add(jsonPaciente);
        }
        jsonDatos.put("pacientes", jsonPacientes);

        JSONArray jsonCitas = new JSONArray();
        for (Cita cita : citas) {
            JSONObject jsonCita = new JSONObject();
            jsonCita.put("fechaHora", cita.getFechaHora());

            if (cita.getDoctor() != null) {
                JSONObject jsonDoctor = new JSONObject();
                jsonDoctor.put("identificador", cita.getDoctor().getIdentificador());
                jsonDoctor.put("nombre", cita.getDoctor().getNombre());
                jsonDoctor.put("especialidad", cita.getDoctor().getEspecialidad());
                jsonCita.put("doctor", jsonDoctor);
            }

            if (cita.getPaciente() != null) {
                JSONObject jsonPaciente = new JSONObject();
                jsonPaciente.put("identificador", cita.getPaciente().getIdentificador());
                jsonPaciente.put("nombre", cita.getPaciente().getNombre());
                jsonCita.put("paciente", jsonPaciente);
            }

            jsonCitas.add(jsonCita);
        }
        jsonDatos.put("citas", jsonCitas);

        // Escribir los datos en un archivo JSON
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonDatos.toJSONString());
            System.out.println(Colores.GREEN_BOLD + "Datos guardados correctamente en el archivo 'datos.json'.\n");
        } catch (IOException e) {
            System.out.println(Colores.RED_BACKGROUND + "Error al guardar los datos en el archivo.\n");
        }
    }
    public void Cargar() {
        JSONParser parser = new JSONParser();

        try (FileReader fileReader = new FileReader(fileName)) {
            Object obj = parser.parse(fileReader);

            JSONObject jsonDatos = (JSONObject) obj;

            JSONArray jsonusuarios = (JSONArray) jsonDatos.get("usuarios");
            if (jsonusuarios != null) {
                for (Object userObj : jsonusuarios) {
                    JSONObject jsonUsuario = (JSONObject) userObj;
                    String identificador = jsonUsuario.get("identificador").toString();
                    String contraseña = jsonUsuario.get("contraseña").toString();
                    Usuario usuario = new Usuario(identificador, contraseña);
                    usuarios.add(usuario);
                }
            }
            JSONArray jsonDoctores = (JSONArray) jsonDatos.get("doctores");
            if (jsonDoctores != null) {
                for (Object doctObj : jsonDoctores) {
                    JSONObject jsonDoctor = (JSONObject) doctObj;
                    String identificador = jsonDoctor.get("identificador").toString();
                    String nombre = jsonDoctor.get("nombre").toString();
                    String especialidad = jsonDoctor.get("especialidad").toString();

                    Doctor doctor = new Doctor(identificador, nombre, especialidad);
                    doctores.add(doctor);
                }
            }

            JSONArray jsonPacientes = (JSONArray) jsonDatos.get("pacientes");
            if (jsonPacientes != null) {
                for (Object pacObj : jsonPacientes) {
                    JSONObject jsonPaciente = (JSONObject) pacObj;
                    String identificador = jsonPaciente.get("identificador").toString();
                    String nombre = jsonPaciente.get("nombre").toString();

                    Paciente paciente = new Paciente(identificador, nombre);
                    pacientes.add(paciente);
                }
            }

            JSONArray jsonCitas = (JSONArray) jsonDatos.get("citas");
            if (jsonCitas != null) {
                for (Object citaObj : jsonCitas) {
                    JSONObject jsonCita = (JSONObject) citaObj;
                    String fechaHora = jsonCita.get("fechaHora").toString();

                    Cita cita = new Cita(fechaHora);

                    JSONObject jsonDoctor = (JSONObject) jsonCita.get("doctor");
                    if (jsonDoctor != null) {
                        String identificadorDoctor = jsonDoctor.get("identificador").toString();
                        Doctor doctor = buscarDoctor(identificadorDoctor);
                        cita.setDoctor(doctor);
                    }

                    JSONObject jsonPaciente = (JSONObject) jsonCita.get("paciente");
                    if (jsonPaciente != null) {
                        String identificadorPaciente = jsonPaciente.get("identificador").toString();
                        Paciente paciente = buscarPaciente(identificadorPaciente);
                        cita.setPaciente(paciente);
                    }

                    citas.add(cita);
                }
            }
            System.out.print(Colores.GREEN_BOLD + "Datos cargados desde el archivo 'datos.json'. ");
            System.out.println("[ " + doctores.size() + " Doctores, " + pacientes.size() + " Pacientes, " + citas.size() + " Citas ]");
        } catch (IOException | org.json.simple.parser.ParseException e) {
            System.out.println(Colores.RED_BACKGROUND + "Error al cargar los datos desde el archivo.");
            System.out.print(e);
        }
    }
    public List<Doctor> getDoctores() {
        return doctores;
    }
    public List<Paciente> getPacientes() {
        return pacientes;
    }
    public List<Cita> getCitas() {
        return citas;
    }

    public void addDoctor(Doctor doctor) {
        doctores.add(doctor);
    }
    public void addUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    public void addPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }
    public void addCita(Cita cita) {
        citas.add(cita);
    }
    public Usuario buscarUsuario(String identificador) {
        for (Usuario usuario : usuarios) {
            if (usuario.getIdentificador().equals(identificador)) {
                return usuario;
            }
        }
        return null;
    }
    public Doctor buscarDoctor(String identificador) {
        for (Doctor doctor : doctores) {
            if (doctor.getIdentificador().equals(identificador)) {
                return doctor;
            }
        }
        return null;
    }

    public Paciente buscarPaciente(String identificador) {
        for (Paciente paciente : pacientes) {
            if (paciente.getIdentificador().equals(identificador)) {
                return paciente;
            }
        }
        return null;
    }
}
