package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import modelo.Modelo;
import vista.Vista;

public class Controlador implements ActionListener {

	private InetAddress ip;

	private Vista vista;
	private Modelo modelo;

	private boolean camaraEncendida = false;
	private boolean transmitiendo = false;
	private boolean encriptando = false;

	public Controlador(Vista vista, Modelo modelo) {
		this.vista = vista;
		this.modelo = modelo;
		try {
			this.ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void arrancarServidor() {
		vista.setNombreVentana("Servidor");
		vista.inicializar();
		vista.setVisible(true);
		vista.addMensajeAPantalla(
				"Iniciando como servidor. IP:\n" + ip.getHostAddress() + "\nAbriendo puerto " + modelo.getPuertoTxt());
		modelo.abrirModoServidor();
		modelo.esperarAlCliente();
		vista.addMensajeAPantalla("Cliente conectado desde:\n" + modelo.getIPCliente());
		modelo.crearFlujosTexto();
		vista.habilitarEnviar();
		vista.addMensajeAPantalla("Conectando canales de video...");
		modelo.conectarAlServidorParaVideo();
		vista.addMensajeAPantalla("Entrada conectada...");
		modelo.esperarAlClienteParaVideo();
		vista.addMensajeAPantalla("Salida conectada. Todo preparado.");
		modelo.start();
	}

	public void arancarCliente() {
		String h = (String) JOptionPane.showInputDialog(null, "Introduzca una IP para conectarse:");
		vista.inicializar();
		vista.setNombreVentana("Cliente");
		vista.setVisible(true);
		vista.addMensajeAPantalla("Iniciando como cliente.");
		modelo.abrirModoCliente(h);
		vista.addMensajeAPantalla("Conectado al servidor");
		modelo.crearFlujosTexto();
		vista.habilitarEnviar();
		vista.addMensajeAPantalla("Conectando canales de video...");
		modelo.esperarAlClienteParaVideo();
		vista.addMensajeAPantalla("Salida conectada...");
		modelo.conectarAlServidorParaVideo();
		vista.addMensajeAPantalla("Entrada conectada.Todo preparado.");
		modelo.start();
	}

	public void terminar() {
		modelo.enviarMensaje("Interlocutor desconectado.\nFin de la conexión.");
	}

	public void parar() {
		modelo.suspend();
	}

	public void addMensajeAPantalla(String mensaje) {
		vista.addMensajeAPantalla(mensaje);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case Vista.ENVIAR:
			try {
				vista.deshabilitarEnviar();
				String msj = vista.getMensajeAEnviar();
				modelo.enviarMensaje(msj);
				vista.addMensajeAPantalla("Enviado: " + msj);
				vista.borrarTexto();
			} catch (NullPointerException ne) {
				vista.addMensajeAPantalla("No hay interlocutor.");
			} finally {
				vista.habilitarEnviar();
			}
			break;
		case Vista.CAMARA:
			camaraEncendida = !camaraEncendida;
			if(camaraEncendida) {
				
			}
			break;
		case Vista.TRANSMITIR:
			transmitiendo = !transmitiendo;

			break;
		case Vista.ENCRIPTAR:
			encriptando = !encriptando;

			break;
		}
		ajustarBotones();
	}

	private void ajustarBotones() {
		if (camaraEncendida) {
			vista.habilitarTransmitir();
			vista.cambiarTextoBtnCamara("Parar vídeo");
		} else {
			vista.deshabilitarTransmitir();
			vista.cambiarTextoBtnCamara("Encender c\u00E1mara");
		}
		if (transmitiendo) {
			vista.habilitarEncriptar();
			vista.cambiarTextoBtnTransmitir("Terminar transmisión");
		} else {
			vista.deshabilitarEncriptar();
			vista.cambiarTextoBtnTransmitir("Transmitir");
		}
		if (encriptando) {
			vista.cambiarTextoBtnEncriptar("Dejar de encriptar");
		} else {
			vista.cambiarTextoBtnEncriptar("Encriptar");
		}
	}
}
