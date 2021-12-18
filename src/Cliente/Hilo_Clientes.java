package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hilo_Clientes implements Runnable {

    private String host;
    private int puerto;
    private int id;
    private int x, y, z;
    Random rand = new Random();

    private PrintWriter output;
    private BufferedReader input;
    private Socket socket;
    private boolean detalles;

    public Hilo_Clientes(String host, int puerto, int id, boolean mostrarDetalles) {
        this.host = host;
        this.puerto = puerto;
        this.id = id;
        detalles = mostrarDetalles;
    }

    @Override
    public void run() {
        
        if (detalles == true) {

            int nCoordenadas, iteraciones;
            String ack = "Cliente " + puerto + ": Se envian las coordenadas x: " + x + " y: " + y + " z: " + z;

            ArrayList<ArrayList<Integer>> arrayCoordenadas = new ArrayList();
            long inicio, fin;

            try {
                socket = new Socket(host, puerto);
                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Modelo_Clientes.nClientes.getAndIncrement();
                GenerarCoordenadas();

                //output.println(id);
                output.println(x); // 1: mando X
                output.println(y); //2: mando Y
                output.println(z); // mando Z
                output.println(id);
                output.flush(); // sin esta linea no escribe

                Modelo_Clientes.infoEjecucion.add("\nCliente " + id
                        + ": Se envian las coordenadas x: " + x + " y: " + y + " z: " + z);

                ack = input.readLine(); //4: recibe respuesta de servidor
                Modelo_Clientes.infoEjecucion.add("\n" + ack);

                inicio = System.currentTimeMillis(); //creo que se empieza a contar aqui
                iteraciones = Integer.parseInt(input.readLine()); //5: recoger cuantas iteraciones se van a pasar
                nCoordenadas = Integer.parseInt(input.readLine()); //6: recoger cuantas coordenadas se van a pasar
                for (int k = 0; k < iteraciones; k++) {
                    for (int i = 0; i < nCoordenadas; i++) {
                        ArrayList<Integer> temporal = new ArrayList();
                        temporal.add(Integer.parseInt(input.readLine())); // 7: recoger coordenada X 
                        temporal.add(Integer.parseInt(input.readLine())); // 8: recoger coordenada Y
                        temporal.add(Integer.parseInt(input.readLine())); // 9: recoger coordenada Z
                        arrayCoordenadas.add(temporal);
                    }
                    //output.println(id);
                    if (k < iteraciones - 1) {
                        output.println((20000)); // 10.1 : confirmar que la iteracion se ha realizado

                    }

                }
                Modelo_Clientes.infoEjecucion.add("Hilo Cliente " + id + " coordenadas de los vecinos recibidas," + iteraciones + " iteraciones realizada");
                fin = System.currentTimeMillis(); // 10.2 : pasar el tiempo empleado
                Modelo_Clientes.infoEjecucion.add("\nHilo Cliente " + id + " media de tiempo calculada");

                output.println((fin - inicio)); // pasa el tiempo que ha tardado en realizar la operacion (está en milesimas, pero sino no se ve diferencia, para ponerlo en segundos dividir entre 1000)
                Modelo_Clientes.nClientes.getAndDecrement();

                input.close();
                output.close();
                socket.close();

                //Modelo_Clientes.infoEjecucion.set("\nHilo Cliente " + id + " se desconecta");
                Modelo_Clientes.infoEjecucion.add("\nHilo Cliente " + id + " se desconecta");
            } catch (IOException ex) {
                Logger.getLogger(Hilo_Clientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            //sc.close();

        } else {
            int nCoordenadas, iteraciones;
            String ack;
            ArrayList<ArrayList<Integer>> arrayCoordenadas = new ArrayList();
            long inicio, fin;

            try {
                socket = new Socket(host, puerto);
                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //while (ejecutando) {
                GenerarCoordenadas();

                //output.println(id);
                output.println(x); // 1: mando X
                output.println(y); //2: mando Y
                output.println(z); // mando Z
                output.println(id);
                output.flush(); // sin esta linea no escribe

                ack = input.readLine(); //4: recibe respuesta de servidor
                //ejecutando = false;

                //}
                inicio = System.currentTimeMillis(); //creo que se empieza a contar aqui
                iteraciones = Integer.parseInt(input.readLine()); //5: recoger cuantas iteraciones se van a pasar
                nCoordenadas = Integer.parseInt(input.readLine()); //6: recoger cuantas coordenadas se van a pasar
                for (int k = 0; k < iteraciones; k++) {
                    for (int i = 0; i < nCoordenadas; i++) {
                        ArrayList<Integer> temporal = new ArrayList();
                        temporal.add(Integer.parseInt(input.readLine())); // 7: recoger coordenada X 
                        temporal.add(Integer.parseInt(input.readLine())); // 8: recoger coordenada Y
                        temporal.add(Integer.parseInt(input.readLine())); // 9: recoger coordenada Z
                        arrayCoordenadas.add(temporal);
                    }
                    //output.println(id);
                    if (k < iteraciones - 1) {
                        output.println((20000)); // 10.1 : confirmar que la iteracion se ha realizado
                    }
                }
                fin = System.currentTimeMillis(); // 10.2 : pasar el tiempo empleado
                output.println((fin - inicio)); // pasa el tiempo que ha tardado en realizar la operacion (está en milesimas, pero sino no se ve diferencia, para ponerlo en segundos dividir entre 1000)

                input.close();
                output.close();
                socket.close();

            } catch (IOException ex) {
                Logger.getLogger(Hilo_Clientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            //sc.close();
        }
    }

    /**
     * Genera coordenadas aleatorias
     */
    private void GenerarCoordenadas() {
        x = rand.nextInt(10);
        y = rand.nextInt(10);
        z = rand.nextInt(10);
    }
}
