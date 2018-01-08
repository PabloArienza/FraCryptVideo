package inicio;

import java.awt.EventQueue;

import javax.swing.JFrame;

import modelo.Modelo;
import vista.Vista;
import controlador.Controlador;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Inicio {

	final static String SERVIDOR = "SERVIDOR";
	final static String CLIENTE = "CLIENTE";

	private JFrame frmFracryptvideomvc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicio window = new Inicio();
					window.frmFracryptvideomvc.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Inicio() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFracryptvideomvc = new JFrame();
		frmFracryptvideomvc.setTitle("FraCryptVideoMVC 1.0");
		frmFracryptvideomvc.setBounds(100, 100, 404, 250);
		frmFracryptvideomvc.setResizable(false);
		frmFracryptvideomvc.setLocationRelativeTo(null);
		frmFracryptvideomvc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFracryptvideomvc.getContentPane().setLayout(new GridLayout(2, 1, 0, 0));

		JButton btnServidor = new JButton("Abrir un canal");
		btnServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vista vista = new Vista();
				Modelo modelo = new Modelo();
				Controlador controlador = new Controlador(vista, modelo);
				vista.setControlador(controlador);
				modelo.setControlador(controlador);
				controlador.arrancarServidor();
			}
		});
		frmFracryptvideomvc.getContentPane().add(btnServidor);

		JButton btnCliente = new JButton("Conectarse a un canal");
		btnCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vista vista = new Vista();
				Modelo modelo = new Modelo();
				Controlador controlador = new Controlador(vista, modelo);
				vista.setControlador(controlador);
				modelo.setControlador(controlador);
				controlador.arancarCliente();
			}
		});
		frmFracryptvideomvc.getContentPane().add(btnCliente);
	}
}
