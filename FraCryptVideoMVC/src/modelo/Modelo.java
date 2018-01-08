package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controlador.Controlador;

public class Modelo extends Thread {

	private Controlador controlador;
	private String HOST;
	private final int PUERTOTXT = 40080;
	private int PUERTOSALIDA;
	private int PUERTOENTRADA;
	private ServerSocket sk, skVideo;
	private Socket socket, sockEntrada, sockSalida;
	private BufferedReader br;
	private BufferedWriter bw;
	private InputStream isVideo;
	private OutputStream osVideo;

	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
	}

	public void abrirModoServidor() {
		PUERTOSALIDA = 40090;
		PUERTOENTRADA = 40099;
		try {
			sk = new ServerSocket(PUERTOTXT);
			skVideo = new ServerSocket(PUERTOSALIDA);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void esperarAlCliente() {
		try {
			socket = sk.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void esperarAlClienteParaVideo() {
		try {
			sockSalida = skVideo.accept();
			osVideo = sockSalida.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void abrirModoCliente(String HOST) {
		this.HOST = HOST;
		PUERTOSALIDA = 40099;
		PUERTOENTRADA = 40090;
		try {
			controlador.addMensajeAPantalla("conectando al servidor en:\n" + HOST + "\nPor el puerto " + PUERTOTXT);
			socket = new Socket(HOST, PUERTOTXT);
			skVideo = new ServerSocket(PUERTOSALIDA);
		} catch (IOException e) {
			try {
				Thread.sleep(1000);
				abrirModoCliente(HOST);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void conectarAlServidorParaVideo() {
		try {
			sockEntrada = new Socket(HOST, PUERTOENTRADA);
			isVideo = sockEntrada.getInputStream();
		} catch (IOException e) {
			try {
				Thread.sleep(1000);
				conectarAlServidorParaVideo();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void crearFlujosTexto() {
		try {
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osr = new OutputStreamWriter(os);
			bw = new BufferedWriter(osr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enviarMensaje(String mensaje) {
		socket.isConnected();
		try {
			bw.write(mensaje);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
		}
	}

	public String recibirMensaje() {
		try {
			String mensaje = br.readLine();
			return mensaje;
		} catch (IOException e) {
			controlador.parar();
		}
		return "";
	}

	public void run() {
		while (true) {
			String mensaje = recibirMensaje();
			controlador.addMensajeAPantalla("Responde: " + mensaje);
		}
	}

	public int getPuertoTxt() {
		return PUERTOTXT;
	}

	public String getIPCliente() {
		return socket.getInetAddress().getHostAddress();
	}
}
