package Cliente;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Hilo_ActualizarVista implements Runnable {

    private JTextArea textArea;
    private JLabel activos, desconectados;
    private int total, contador;
    private boolean enCurso;

    public Hilo_ActualizarVista() {
    }

    @Override
    public void run() {

        //Bool enCurso = false para parar el hilo
        enCurso = true;
        while (enCurso) {
            activos.setText("Clientes activos: " + Modelo_Clientes.nClientes);
            desconectados.setText("Clientes desconectados: " + 
                    (total - Modelo_Clientes.nClientes.intValue()));
            //Actualizar el area de texto solo si hay nuevos mensajes
            while (contador < Modelo_Clientes.infoEjecucion.size()) {
                textArea.append(Modelo_Clientes.infoEjecucion.get(contador));
                contador++;
            }
            //Actualizar vista
            textArea.revalidate();
            textArea.repaint();
        }
    }

    /**
     * Prepara y carga los datos en la clase hilo
     *
     * @param textArea Area de texto para los detalles
     * @param activos Clientes activos
     * @param desconectados Clientes descoenctados
     * @param totales Clientes totales
     */
    public void cargarDatos
        (JTextArea textArea, JLabel activos, JLabel desconectados, String totales) {
            
        this.textArea = textArea;
        this.activos = activos;
        this.desconectados = desconectados;
        total = Integer.parseInt(totales);
        textArea.setText("");
        contador = 0;
    }

    public void setEnCurso(boolean enCurso) {
        this.enCurso = enCurso;
    }

    public void cleanTextArea() {
        textArea.setText("");
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
}
