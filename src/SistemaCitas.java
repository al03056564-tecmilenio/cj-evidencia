import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SistemaCitas {

    private static DataAccess dataAccess;
    private static Usuario usuarioActual = null;

    public static void main(String[] args) {
        try {
            String dbPath = new File(".").getCanonicalPath() + "/db";
            Path path = Paths.get(dbPath);
            Files.createDirectories(path);
            String dataFilePath = dbPath + "/datos.json";
            File dataJsonFile = new File(dataFilePath);
            Boolean archivoCreado = dataJsonFile.createNewFile();
            dataAccess = new DataAccess(dataFilePath);
            if (archivoCreado) {
                // creando usuario admin.
                dataAccess.addUsuario(new Usuario("admin", MD5.getMd5("admin")));
                dataAccess.Guardar();
            }
            // LimpiaPantalla();
            System.out.println(Colores.RED + "~~~ " + Colores.WHITE_BOLD + "Bienvenido a SISTEMA DE CITAS " + Colores.RED + "~~~");
            System.out.print(Colores.RESET);
            dataAccess.Cargar();
            LimpiaPantalla();
            mostrarMenu();
        } catch (Exception ex) {
            System.out.println(".. Ocurrio un problema cargando los datos.");
            System.out.println(ex);
            System.exit(0);
        }
    }
    private static void SolicitarInicioDeSesion() {
        Scanner scanner = new Scanner(System.in);
        LimpiaPantalla();
        // Solicitar identificador y contraseña al iniciar
        System.out.println(" Por favor, inicia sesion");
        System.out.print(Colores.BLUE + "Identificador de usuario: ");
        String identificador = scanner.nextLine();
        System.out.print(Colores.BLUE + "Contraseña: ");
        String contraseña = scanner.nextLine();
        if (verificarUsuario(identificador, contraseña)) {
            usuarioActual = dataAccess.buscarUsuario(identificador);
            System.out.println(Colores.RESET + "Bienvenido " + Colores.BLUE_BOLD + usuarioActual.getIdentificador() + Colores.RESET + "!");
        } else {
            usuarioActual = null;
            System.out.println("Credenciales inválidas. El programa se cerrará.");
        }
    }
    public static void LimpiaPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }
    private static void mostrarMenu() {
        LimpiaPantalla();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            if (usuarioActual == null) {
                SolicitarInicioDeSesion();
                if (usuarioActual == null){
                    salir = true;
                    break;
                }

            }
            System.out.println(Colores.WHITE + "ALTAS     [" + Colores.GREEN + "1 : Doctores" + Colores.WHITE + "] [" + Colores.GREEN + "2: Pacientes" + Colores.WHITE + "] [" + Colores.GREEN + "3: Citas" + Colores.WHITE + "] [" + Colores.GREEN + "7: Usuarios" + Colores.WHITE + "]");
            System.out.println(Colores.WHITE + "CONSULTA  [" + Colores.GREEN + "4: Doctores" + Colores.WHITE + "] [" + Colores.GREEN + "5: Pacientes" + Colores.WHITE + "] [" + Colores.GREEN + "6: Citas" + Colores.WHITE + "]");
            System.out.println(Colores.WHITE + "SISTEMA   [" + Colores.GREEN + "0: Guardar" + Colores.WHITE + "] [" + Colores.GREEN + "8: Cerrar Sesion" + Colores.WHITE + "] [" + Colores.GREEN + "9: Salir" + Colores.WHITE + "]");
            System.out.print(Colores.RED +"        Ingrese una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el salto de línea

            switch (opcion) {
                case 0:
                    dataAccess.Guardar();
                    break;
                case 1:
                    darDeAltaDoctor(scanner);
                    break;
                case 2:
                    darDeAltaPaciente(scanner);
                    break;
                case 3:
                    crearCita(scanner);
                    break;
                case 6:
                    listarCitas();
                    break;
                case 7:
                    darDeAltaUsuario(scanner);
                    break;
                case 4:
                    listarDoctores();
                    break;
                case 5:
                    listarPacientes();
                    break;
                case 8:
                    usuarioActual = null; // Cerrar sesión
                    break;
                case 9:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.\n");
            }
        }

        // Guardar datos en un archivo JSON al salir del programa
        dataAccess.Guardar();

        System.out.println("¡Hasta luego!");
    }

    private static void darDeAltaDoctor(Scanner scanner) {
        System.out.print("Ingrese el identificador del doctor (ejem: D001): ");
        String identificador = scanner.nextLine();
        if (dataAccess.buscarDoctor(identificador) == null) {
            System.out.print("Ingrese el nombre del doctor: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese la especialidad del doctor: ");
            String especialidad = scanner.nextLine();

            Doctor doctor = new Doctor(identificador, nombre, especialidad);
            dataAccess.addDoctor(doctor);

            System.out.println("Doctor registrado correctamente.\n");
        } else {
            System.out.println("Identificador de doctor ya existe.\n");
        }
    }

    private static void darDeAltaPaciente(Scanner scanner) {
        System.out.print("Ingrese el identificador del paciente (ejem: P001): ");
        String identificador = scanner.nextLine();
        if (dataAccess.buscarPaciente(identificador) == null) {
            System.out.print("Ingrese el nombre del paciente: ");
            String nombre = scanner.nextLine();

            Paciente paciente = new Paciente(identificador, nombre);
            dataAccess.addPaciente(paciente);

            System.out.println("Paciente registrado correctamente.\n");
        } else {
            System.out.println("Identificador de Paciente ya existe.\n");
        }
    }
    private static void darDeAltaUsuario(Scanner scanner) {
        System.out.print("Ingrese el identificador del usuario: ");
        String identificador = scanner.nextLine();

        if (dataAccess.buscarUsuario(identificador) == null) {
            System.out.print("Ingrese la contraseña: ");
            String contra = scanner.nextLine();
            System.out.print("Confirme la contraseña: ");
            String contra2 = scanner.nextLine();
            if (contra.equals(contra2)) {
                Usuario usuario = new Usuario(identificador, MD5.getMd5(contra));
                dataAccess.addUsuario(usuario);
                System.out.println("Paciente registrado correctamente.\n");
            } else {
                System.out.println("Contraseña no coincide.");
            }
        } else {
            System.out.println("Identificador de usuario ya existe.");
        }

    }
    private static void crearCita(Scanner scanner) {
        System.out.print("Ingrese la fecha y hora de la cita (ejemplo: 2023-05-30 09:00): ");
        String fechaHora = scanner.nextLine();
        System.out.print("Identificador de Doctor: ");
        String doctorID = scanner.nextLine();
        System.out.println("Buscando doctor con id: " + doctorID);
        Doctor doctor = dataAccess.buscarDoctor(doctorID);
        if (doctor == null) {
            System.out.println(Colores.RED + " Error: " + Colores.YELLOW + "Doctor no encontrado.");
            return;
        }
        System.out.println(Colores.BLUE + doctor.toString() + Colores.RESET);
        System.out.print("Identificador de Paciente: ");
        String pacienteID = scanner.nextLine();
        Paciente paciente = dataAccess.buscarPaciente(pacienteID);
        if (paciente == null) {
            System.out.println(Colores.RED + " Error: " + Colores.YELLOW + "Paciente no encontrado.");
            return;
        }
        System.out.println(Colores.BLUE + paciente.toString() + Colores.RESET);
        Cita cita = new Cita(fechaHora);
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        dataAccess.addCita(cita);

        System.out.println(Colores.RESET + "Cita creada correctamente.\n");
        System.out.println(Colores.GREEN + cita.toString() + Colores.RESET);
    }

    private static void listarCitas() {
        if (dataAccess.getCitas().isEmpty()) {
            System.out.println(Colores.RED_BACKGROUND + "No hay citas registradas.\n");
        } else {
            System.out.println(Colores.RESET + "Listado de Citas");
            for (Cita cita : dataAccess.getCitas()) {
                System.out.println(Colores.BLUE + cita.toString());
            }
            System.out.println(Colores.RESET + "Total: " + dataAccess.getCitas().size());
            System.out.println();
        }
    }

    private static void listarDoctores() {
        if (dataAccess.getDoctores().isEmpty()) {
            System.out.println(Colores.RED_BACKGROUND + "No hay doctores registrados.\n");
        } else {
            System.out.println(Colores.RESET + "Listado de Doctores");
            for (Doctor doctor : dataAccess.getDoctores()) {
                System.out.println(Colores.BLUE + doctor.toString());
            }
            System.out.println(Colores.RESET + "Total: " + dataAccess.getDoctores().size());
            System.out.println();
        }
    }

    private static void listarPacientes() {
        if (dataAccess.getPacientes().isEmpty()) {
            System.out.println(Colores.YELLOW_BOLD + "No hay pacientes registrados.\n");
        } else {
            System.out.println(Colores.RESET + "Listado de Pacientes");
            for (Paciente paciente : dataAccess.getPacientes()) {
                System.out.println(Colores.BLUE + paciente.toString());
            }
            System.out.println(Colores.RESET + "Total: " + dataAccess.getPacientes().size());
            System.out.println(Colores.RESET);
        }
    }

    private static boolean verificarUsuario(String identificador, String contraseña) {
        // Aquí deberías implementar tu lógica de autenticación de usuarios.
        // Por simplicidad, en este ejemplo se considera válido cualquier identificador y contraseña.
        Usuario usuario = dataAccess.buscarUsuario (identificador);
        if (usuario != null) {
            if (usuario.getContraseña().equals(MD5.getMd5(contraseña))) {
                usuarioActual = usuario;
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Usuario no encontrado");
            return false;
        }
    }

}
