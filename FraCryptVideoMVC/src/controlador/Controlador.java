package controlador;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import modelo.Modelo;
import vista.Vista;

public class Controlador implements ActionListener {

	private InetAddress ip;

	private Vista vista;
	private Modelo modelo;
	
	private byte[] imagenCamara;

	private boolean camaraEncendida = false;
	private boolean transmitiendo = false;
	private boolean encriptando = false;
	
	Thread hiloCamara = new Thread() {
		@Override
		public void run() {
			vista.encenderCamara();
			while(camaraEncendida) {
				Image img = vista.capturarImagen();
				pintaVentanaCamara(img);
			    BufferedImage bufImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			    Graphics2D bGr = bufImg.createGraphics();
			    bGr.drawImage(img, 0, 0, null);
			    bGr.dispose();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					ImageIO.write(bufImg, "jpg", baos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imagenCamara = baos.toByteArray();
				if(transmitiendo) {
					modelo.enviaImagen(imagenCamara);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {}
			}
		}

		private void pintaVentanaCamara(Image img) {
			vista.pintaVentanaCamara(img);
		}
	};

	public Controlador(Vista vista, Modelo modelo) {
		this.vista = vista;
		this.modelo = modelo;
		try {
			this.ip = InetAddress.getLocalHost();
			hiloCamara.start();
			hiloCamara.suspend();
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
				hiloCamara.resume();
			}else {
				hiloCamara.suspend();
				vista.pintaVentanaCamara(null);
			}
			break;
		case Vista.TRANSMITIR:
			transmitiendo = !transmitiendo;
			if(!transmitiendo)
				modelo.enviaImagen(null);
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

	public void pintaVentanaInterlocutor(BufferedImage img) {
		vista.pintaVentanaInterlocutor(img);
		
	}

	public void pintaVentanaTransmision(byte[] imagen) {
		try {
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imagen));
			vista.pintaVentanaTransmision(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
