package Cliente;

public class Main_Clientes {

    public static void main(String args[]) {

        Modelo_Clientes modelo = new Modelo_Clientes();
        Vista_Clientes vista = new Vista_Clientes();
        Controlador_Clientes controlador = new Controlador_Clientes(modelo, vista);
        controlador.iniciar();
    }
}
