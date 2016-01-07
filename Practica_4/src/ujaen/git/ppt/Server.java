package ujaen.git.ppt;

// Se crea un servidor socket y se añade thread para varias conexiones a la vez.
// El servidor debe soportar varias conexiones de manera concurrente.
// Debe soportar los comandos mínimos para recibir un correo sólo con texto ASCII y de cualquier extensión.
// Debe implementar el comando RESET.
// Debe incluir en el correo las cabeceras Message-ID y Received, según la RFC 5322.
// Los correos se reciben en archivos txt en carpetas diferentes para cada usuario.
//  Los ficheros deben seguir el formato de la RFC 5322.  //  Para almacer la clave de usuario, se crea en el directorio de usuario un fichero
//  user.key, donde user es el ID del usuario. Sólo puede contener una línea con una
//  cabecera sin espacios que señala la clave.


import java.io.IOException;
import java.net.*;

public class Server {

	public static final int TCP_SERVICE_PORT = 25;

	static ServerSocket server = null;

	public static void main(String[] args) {
			
		System.out.println("Servidor> Iniciando servidor");
		try {
			server = new ServerSocket(TCP_SERVICE_PORT);
			while (true) {
				final Socket newsocket = server.accept();
				System.out.println("Servidor> Conexión entrante desde "
						+ newsocket.getInetAddress().toString() + ":"
						+ newsocket.getPort());
				new Thread(new Connection(newsocket)).start();
			}
		} catch (IOException e) {
			System.err.println("Server "+e.getMessage());
			e.printStackTrace();
		}

	}

}
