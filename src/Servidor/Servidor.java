package Servidor;

import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {

    static final int PUERTO = 5000;

    public static void main(String[] args) {
        //Inicio variables
        ServerSocket sServidor;
        Socket sCliente;
        long media = 0;
        //Contenedor contenedor;

        int aux = 0;

        ArrayList<ArrayList<ArrayList<Integer>>> arrayCoordenadasPorGrupos = new ArrayList<>();

        int nHilos = 0;
        int nClientes, nGrupos, nIteraciones, clientesPorGrupo;

        //ArrayList<HiloServidor> arrayHilosServidor = new ArrayList<>();
        ArrayList<ArrayList<HiloServidor>> arrayHilosServidorGrupos = new ArrayList<>();

        Scanner in = new Scanner(System.in);

        //Inicio del programa
        System.out.println("-----------------SERVIDOR------------------");

        do {

            System.out.print("Introduce el numero de clientes de la simulacion: ");
            nClientes = in.nextInt();
            System.out.print("Introduce el numero de grupos (multiplo del numero de clientes): ");
            nGrupos = in.nextInt();

            if (!((nClientes % nGrupos) == 0)) {
                System.out.print("\u001B[31mEl numero de grupos tiene que ser multiplo del numero de clientes ej: C = 10 & G = 2\u001B[0m");
            }

        } while (!((nClientes % nGrupos) == 0));

        clientesPorGrupo = nClientes / nGrupos; //calcular cuantos clientes van en cada grupo

        for (int i = 0; i < nGrupos; i++) { // relleno array de servidores
            ArrayList<HiloServidor> arrayTemporal = new ArrayList<>();
            arrayHilosServidorGrupos.add(arrayTemporal);
        }
        System.out.print("Introduce el numero de iteraciones de la simulacion: ");
        nIteraciones = in.nextInt();
        System.out.println("\nEsperando la conexion de los clientes...\n");

        //Se conectan todos los clientes al servidor
        try {
            sServidor = new ServerSocket(PUERTO);

            while (nClientes > nHilos) {
                //Esperar a que entre un cliente
                sCliente = sServidor.accept();
                System.out.println("Nuevo cliente conectado, puerto: " + sCliente.getPort());

                //Crear hilo servidor
                HiloServidor hiloServidor = new HiloServidor(sCliente, nHilos);
                hiloServidor.setIteraciones(nIteraciones);

                if (arrayHilosServidorGrupos.get(aux).size() < clientesPorGrupo) {
                    arrayHilosServidorGrupos.get(aux).add(hiloServidor);

                } else {
                    aux++;
                    arrayHilosServidorGrupos.get(aux).add(hiloServidor);
                }
                nHilos++;
            }

            System.out.println("\n\033[34mTodos los clientes (" + nClientes + ") se han conectado, empieza el intercambio de coordenadas:\n");

            for (int j = 0; j < nGrupos; j++) {
                for (int i = 0; i < clientesPorGrupo; i++) { //activar todos los hilos, una vez se hayan conectado todos //todo wait() + notifyAll()
                    new Thread(arrayHilosServidorGrupos.get(j).get(i)).start();
                }
            }

                sleep(1000); //Para asegurarse de que lo hace todo
                for (int j = 0; j < nGrupos; j++) {
                    ArrayList<ArrayList<Integer>> arrayCoordenadas = new ArrayList<>();
                    for (int i = 0; i < clientesPorGrupo; i++) { // arrayCoordenadas tiene en cada posicion una ArrayList con las 3 coordenadas de cada cliente
                        ArrayList<Integer> temporal = new ArrayList();
                        temporal.add(arrayHilosServidorGrupos.get(j).get(i).getX());
                        temporal.add(arrayHilosServidorGrupos.get(j).get(i).getY());
                        temporal.add(arrayHilosServidorGrupos.get(j).get(i).getZ());
                        arrayCoordenadas.add(temporal);
                        
                    }
                    System.out.println("\n\033[34mCoordenadas del grupo " + j + " recibidas en el servidor");
                    arrayCoordenadasPorGrupos.add(arrayCoordenadas);
                }
                System.out.println("\n\033[34mSe mandan coordenadas a los vecinos\n");
                for (int j = 0; j < nGrupos; j++) {//enviar coordenadas a cada hilo para que realicen el calculo
                    for (int i = 0; i < clientesPorGrupo; i++) {
                        arrayHilosServidorGrupos.get(j).get(i).coordenadasVecinos(arrayCoordenadasPorGrupos.get(j));
                    }
                }
                
                //sleep(20000); // tiempo que ha de esperar a que los hilos acaben (comentado por ahora para no esperar)
                
                for (int j = 0; j < nGrupos; j++) {//recoger el tiempo de ejecucion de cada cliente
                    for (int i = 0; i < clientesPorGrupo; i++) {
                        media += arrayHilosServidorGrupos.get(j).get(i).getTiempo(); //recoger el tiempo que ha tardado cada cliente
                    }
                }
            //media = media / (nClientes * S);
            media = media/nClientes;
            System.out.println("\nLa media de ejecución ha sido: " + media + " milesimas de segundo, o aproximadamente: "+(int)media/1000 +" segundos");

        } catch (Exception e) {
            System.out.println("\u001B[31m error en el servidor");
        }
    }
}
