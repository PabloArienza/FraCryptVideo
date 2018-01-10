package transformador;

import java.io.IOException;
import java.security.MessageDigest;

import fractal.Fractal;
import fractal.Mandelbrot2DModificado;
import fractal.Punto;

public class TransformadorVideo {
	
	boolean encriptando;
	Fractal fractal;
	
	public TransformadorVideo(String password, boolean encriptando) throws IOException {
		String sha = creaSha256(password);
		int[][] parametros = setParametros(sha);
		this.encriptando = encriptando;
		fractal = new Mandelbrot2DModificado(parametros[0], parametros[1], parametros[3][0], parametros[4][0],
				parametros[5][0]);
		fractal.setPuntoDeInicio(parametros[2][0], parametros[2][1]);
	}// fin del constructor
	
	protected String creaSha256(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}// fin creaSha256

	
	public byte[] transformarEnBloque(byte[] origen) {
		// Las transformaciones se almacenan en un buffer secundario
		byte[] salida = new byte[origen.length];
		Punto punto = fractal.leePunto();
		for (int i = 0; i < origen.length; i++) {			
				int operadorXOR = 0;
				for (int p = 0; p < punto.getCoordenadas().length; p++) {
					operadorXOR += punto.getCoordenadas()[p];
				}
				salida[i] = (byte) (origen[i] ^ (operadorXOR) % 256);
				int avance = 0;
				if (encriptando) {
					avance = Math.abs(origen[i]);
				} else {
					avance = Math.abs(salida[i]);
				}
				fractal.mutarElPunto(avance);
				fractal.pasaAlSiguienteConjunto(avance);
				punto = fractal.leePunto();
		}
		return salida;
	}// fin transformarEnBloque
	
	protected int[][] setParametros(String sha) {
		int[][] shaCortado = new int[6][2];
		int multiplicador = Character.getNumericValue(sha.charAt(63)) + 10;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 14; j++) {
				shaCortado[i * 14][j / 7] += Character.getNumericValue(sha.charAt(j));
			}
		}
		for (int i = 42; i < 63; i++) {
			shaCortado[(i - 21 / 7)][0] = Character.getNumericValue(sha.charAt(i));
		}
		for (int i = 1; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				if (shaCortado[i][j] > shaCortado[0][j])
					shaCortado[i][j] /= 2;
			}
		}
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				shaCortado[i][j] *= multiplicador;
			}
		}
		return shaCortado;
	}// fin setParametros
	
	
}
