package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloServidor implements Runnable {

    private final Socket socket;
    private int id, idCliente;
    private int x, y, z = 0;
    private int iteraciones;
    private long tiempo = (long) 20;
    //private Contenedor contenedor;

    //int id1, id2;
    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public int getIteraciones() {
        return iteraciones;
    }

    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones;
    }

    private PrintWriter output;
    private BufferedReader input;

    /**
     * Asigna los valores de las variables iniciales
     *
     * @param socket
     * @param id
     * @param c
     */
    /*public HiloServidor(Socket socket, int id, Contenedor c) {
        this.socket = socket;
        this.id = id;
        this.contenedor = c;
    } */
    public HiloServidor(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {

        try {
            output = new PrintWriter(socket.getOutputStream(), true); //consigo el output del cliente
            input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //consigo el input del cliente

            x = Integer.parseInt(input.readLine()); //1: recojo X
            y = Integer.parseInt(input.readLine()); //2: recojo Y
            z = Integer.parseInt(input.readLine()); //3: recojo Z
            idCliente = Integer.parseInt(input.readLine());
            System.out.println("Coordenadas del cliente " + idCliente + " recibidas: " + x + " " + y + " " + z +" en hilo servidor " + id); //mostrar coordenadas por pantalla

            output.println("El hilo servidor " + id + " ha recibido las coordenadas: " + x + " " + y + " " + z);// 4: confirma coordenadas
            output.flush();

        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void coordenadasVecinos(ArrayList<ArrayList<Integer>> arrayCoordenadas) throws IOException { //funcion para mandar coordenadas a cliente
        output.println(iteraciones); // 5: pasar cuantas iteraciones se van a realizar
        output.println(arrayCoordenadas.size()); //6: pasar cuantas coordenadas se van a pasar
        
        for (int j = 0; j < iteraciones; j++) {
            for (int i = 0; i < arrayCoordenadas.size(); i++) {
                // if(i != id){//para no pasar las coordenadas propias (TAL VEZ SEA PEOR PARA EL RENDIMIENTO)
                output.println(arrayCoordenadas.get(i).get(0)); // 7: pasar la coordenada X
                output.println(arrayCoordenadas.get(i).get(1));// 8: pasar la coordenada Y
                output.println(arrayCoordenadas.get(i).get(2));// 9: pasar la coordenada Z
            }
            //id2 = Integer.parseInt(input.readLine()); 
            tiempo = Long.parseLong(input.readLine()); // 10: recoger tiempo empleado por hilo
            System.out.println("Hilo Servidor " + id + ": Iteracion " + j + " realizada");
            //System.out.println("id1: "+id1+" id2: "+id2);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
