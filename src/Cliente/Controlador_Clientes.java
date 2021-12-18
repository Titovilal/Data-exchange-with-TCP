package Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controlador_Clientes {

    private Modelo_Clientes modelo;
    private Vista_Clientes vista;

    public Controlador_Clientes(Modelo_Clientes modelo, Vista_Clientes vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    class VistaListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {

            String command = ae.getActionCommand();
            switch (command) {
                case "Ini":
                    modelo.hiloAct.setEnCurso(false);
                    vista.textDetalles.setText("");

                    if (vista.checkboxMostrar.isSelected()) {
                        //Cargar datos en el hilo que actualiza la vista
                        modelo.hiloAct.cargarDatos(vista.textDetalles,
                                vista.labelActivos, vista.labelDesconectados, vista.textNumClientes.getText());
                        //Se puede hacer con una función en la vista y declarando las variables como privadas
                        vista.labelDesconectados.setText("Clientes desconectados: " + vista.textNumClientes.getText());
                        vista.labelTotales.setText("Clientes totales: " + vista.textNumClientes.getText());
                        vista.labelActivos.setText("Clientes activos: 0");
                        vista.labelDetalles.setText("Detalles: Visibles");
                    } else {
                        vista.labelDesconectados.setText("Clientes desconectados: Oculto");
                        vista.labelActivos.setText("Clientes activos: Oculto");
                        vista.labelTotales.setText("Clientes totales: " + vista.textNumClientes.getText());
                        vista.labelDetalles.setText("Detalles: Ocultos");
                    }
                    //Iniciar simulación
                    modelo.iniciarSimulacion(Integer.parseInt(vista.textNumClientes.getText()), vista.textIp.getText(),
                            Integer.parseInt(vista.textPuerto.getText()), vista.checkboxMostrar.isSelected());
                    break;

                case "Res":
                    vista.textIp.setText("127.0.0.1");
                    vista.textPuerto.setText("5000");
                    vista.revalidate();
                    vista.repaint();
                    break;
            }
        }
    }

    public void iniciar() {
        vista.setActionListener(new VistaListener());
    }
}
