public class Usuario {
    private String identificador;
    private String contraseña;

    public Usuario(String identificador, String contraseña) {
        this.identificador = identificador;
        this.contraseña = contraseña;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getContraseña() {
        return contraseña;
    }
}
