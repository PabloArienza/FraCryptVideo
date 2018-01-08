package vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.sarxos.webcam.Webcam;

import controlador.Controlador;
import interfaz.Acciones;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Vista extends JFrame implements Acciones {

	Controlador controlador;

	public Vista() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					controlador.terminar();
				} catch (NullPointerException ne) {
				} finally {
					System.exit(0);
				}
			}
		});
		setBounds(100, 100, 896, 629);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		lblInterlocutor = new JLabel("");
		lblInterlocutor.setBounds(250, 120, 640, 480);
		lblInterlocutor.setOpaque(true);
		lblInterlocutor.setBackground(Color.GRAY);
		getContentPane().add(lblInterlocutor);

		taMensajes = new JTextArea();
		taMensajes.setBackground(Color.LIGHT_GRAY);
		JScrollPane scroll = new JScrollPane(taMensajes);
		scroll.setBounds(0, 0, 250, 520);
		getContentPane().add(scroll);

		txtMensaje = new JTextField();
		txtMensaje.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtMensaje.setBounds(0, 520, 250, 40);
		getContentPane().add(txtMensaje);
		txtMensaje.setColumns(10);

		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(0, 560, 250, 40);
		getContentPane().add(btnEnviar);

		lblMiCamara = new JLabel("");
		lblMiCamara.setBounds(250, 0, 160, 120);
		lblMiCamara.setOpaque(true);
		lblMiCamara.setBackground(Color.GRAY);
		getContentPane().add(lblMiCamara);

		lblTransmision = new JLabel("");
		lblTransmision.setOpaque(true);
		lblTransmision.setBackground(Color.GRAY);
		lblTransmision.setBounds(730, 0, 160, 120);
		getContentPane().add(lblTransmision);

		lblFractal = new JLabel("");
		lblFractal.setOpaque(true);
		lblFractal.setBackground(Color.LIGHT_GRAY);
		lblFractal.setBounds(570, 0, 160, 120);
		getContentPane().add(lblFractal);

		btnCamara = new JButton("Encender c\u00E1mara");
		btnCamara.setBounds(410, 0, 160, 40);
		getContentPane().add(btnCamara);

		btnTransmitir = new JButton("Transmitir");
		btnTransmitir.setBounds(410, 40, 160, 40);
		getContentPane().add(btnTransmitir);

		btnEncriptar = new JButton("Encriptar");
		btnEncriptar.setBounds(410, 80, 160, 40);
		getContentPane().add(btnEncriptar);

		camara = Webcam.getDefault();
		camara.setViewSize(new Dimension(640, 480));
		btnEnviar.setEnabled(false);
		btnTransmitir.setEnabled(false);
		btnEncriptar.setEnabled(false);
	}

	private JLabel lblInterlocutor;
	private JTextArea taMensajes;
	private JTextField txtMensaje;
	private JButton btnEnviar;
	private JLabel lblMiCamara;
	private JLabel lblTransmision;
	private JLabel lblFractal;
	private JButton btnCamara;
	private JButton btnTransmitir;
	private JButton btnEncriptar;

	private Webcam camara;
	private BufferedImage imagen;

	public void addMensajeAPantalla(String mensaje) {
		taMensajes.append(mensaje + "\n");
	}

	public void deshabilitarEnviar() {
		btnEnviar.setEnabled(false);
	}

	public String getMensajeAEnviar() {
		return txtMensaje.getText();
	}

	public void borrarTexto() {
		txtMensaje.setText("");
	}

	public void habilitarEnviar() {
		btnEnviar.setEnabled(true);
	}

	public void habilitarTransmitir() {
		btnTransmitir.setEnabled(true);
	}

	public void cambiarTextoBtnCamara(String texto) {
		btnCamara.setText(texto);
	}

	public void deshabilitarTransmitir() {
		btnTransmitir.setEnabled(false);
	}

	public void habilitarEncriptar() {
		btnEncriptar.setEnabled(true);
	}

	public void cambiarTextoBtnTransmitir(String texto) {
		btnTransmitir.setText(texto);
	}

	public void deshabilitarEncriptar() {
		btnEncriptar.setEnabled(false);
	}

	public void cambiarTextoBtnEncriptar(String texto) {
		btnEncriptar.setText(texto);
	}

	public void setNombreVentana(String titulo) {
		setTitle(titulo);
	}

	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
	}

	public void inicializar() {
		btnEnviar.setActionCommand(ENVIAR);
		btnEnviar.addActionListener(controlador);
		btnCamara.setActionCommand(CAMARA);
		btnCamara.addActionListener(controlador);
		btnTransmitir.setActionCommand(TRANSMITIR);
		btnTransmitir.addActionListener(controlador);
		btnEncriptar.setActionCommand(ENCRIPTAR);
		btnEncriptar.addActionListener(controlador);
	}
}
