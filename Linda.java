package practicaLinda;

import java.net.ServerSocket;
import java.net.Socket;

// Clase que actua como el constructor de un servidor Linda y controla sus acciones.
public class Linda {
	private final int PUERTO = 1234;
	protected ServerSocket serverSocket;
	protected Socket clientSocket;

	/*
	 * Pre: -- 
	 * Post: Este metodo gestiona las acciones del servidor Linda.
	 */
	public void startServer() {
			try {
				// Se lanza el servidor en el puerto 1234.
				serverSocket = new ServerSocket(PUERTO);
				while (true) {
					System.out.println("Esperando..."); 
					clientSocket = serverSocket.accept();
					System.out.println("Cliente en línea");
					// Se crea un hilo por cada conexion para manejar las peticiones del clientes.
	                ClientHandler clientSock = new ClientHandler(clientSocket);
	                new Thread(clientSock).start();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

}
