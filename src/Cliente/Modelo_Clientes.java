package Cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class Modelo_Clientes {

    private int id;
    //Forma segura de que hilos accedan a una variable
    protected static ArrayList<String> infoEjecucion;
    protected static AtomicInteger nClientes;
    protected Hilo_ActualizarVista hiloAct;

    public Modelo_Clientes() {
        id = 0;
        //Permitir la que los hilos accedan de forma segura al número de clientes
        nClientes = new AtomicInteger(0);
        infoEjecucion = new ArrayList<>();
        //Permitir la que los hilos accedan de forma segura al ArrayList
        Collections.synchronizedList(infoEjecucion);
        hiloAct = new Hilo_ActualizarVista();
    }

    public void iniciarSimulacion
        (int clientes, String host, int puerto, boolean detalles) {
        int n = clientes;
        id = 0;
        //Opción mostrar detalles en el area de texto
        if (detalles) {
            nClientes.set(0);
            infoEjecucion.clear();
            hiloAct.setEnCurso(true);
            new Thread(hiloAct).start();
            infoEjecucion.add("Iniciando Simulacion...");
        }
        //Iniciar simulación
        while (n > 0) {
            id++;
            n--;
            Hilo_Clientes hilo = new Hilo_Clientes(host, puerto, id, detalles);
            new Thread(hilo).start();
        }
    }

    public void clearInfoEjecucion() {
        infoEjecucion.clear();
    }

    public static void setnClientes(int nClientes1) {
        nClientes.set(nClientes1);
    }
}
