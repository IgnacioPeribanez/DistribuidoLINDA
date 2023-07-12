package practicaLinda;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Clase que actua como un controlador para las peciciones de linda sobre cualquier servidor.
public class ClientHandler implements Runnable {
	private Socket clientSocket;
	private Socket server;
	private Socket serverReserva;
	private ArrayList<String> enEspera1 = new ArrayList<String>();
	private ArrayList<String> enEspera1Reserva = new ArrayList<String>();

	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
	}

	/*
	 * Pre: -- 
	 * Post: Este metodo se encarga de insertar una instruccion en el servidor adecuado
	 */
	public String insertarInstruccion(Socket servidorDestino, DataOutputStream out, String instruccion) {
		String mensaje = "";
		try {
			DataInputStream inServer = new DataInputStream(servidorDestino.getInputStream());
			DataOutputStream outServer = new DataOutputStream(servidorDestino.getOutputStream());
			outServer.writeUTF(instruccion);
			mensaje = inServer.readUTF();
			System.out.println(mensaje);
		} catch (Exception e) {}
		return mensaje;
	}

	/*
	 * Pre: -- 
	 * Post: Este metodo se encarga de insertar las instrucciones que tiene un servidor 
	 * 		 en espera una vez este se levante.
	 */
	public void insertarEspera(ArrayList<String> listaEnEspera, Socket servidorDestino) {
		try {
			for (int i = 0; i < listaEnEspera.size(); i++) {
				DataInputStream inServer = new DataInputStream(servidorDestino.getInputStream());
				DataOutputStream outServer = new DataOutputStream(servidorDestino.getOutputStream());
				outServer.writeUTF(listaEnEspera.get(i));
				String mensaje = inServer.readUTF();
				System.out.println(mensaje);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			out.writeUTF("BIENVENIDO AL SERVICIO LINDA");
			while (true) {
				boolean server1 = false;
				boolean server1Reserva = false;
				String instruccion = in.readUTF();
				String partes[] = instruccion.split("\"");
				// Depende de la longitud de la tupla se redirecciona a uno u otro servidor.
				if (partes.length <= 7) {
					String resultado = "";
					try {
						// Se intenta la conexion.
						server = new Socket("localhost", 1235);
						// Se indica que el servidor esta en funcionamiento.
						server1 = true;
						// Se insertan las instrucciones en espera.
						if (!enEspera1.isEmpty()) {
							insertarEspera(enEspera1, server);
							enEspera1.clear();
						}
						// Se inserta la nueva instruccion.
						resultado = insertarInstruccion(server, out, instruccion);
					} catch (Exception e) {
						// Si no se puede conectar con el servidor se añade a la espera
						enEspera1.add(instruccion);
					}
					try {
						// Se intenta la conexion.
						serverReserva = new Socket("localhost", 1236);
						// Se indica que el servidor esta en funcionamiento.
						server1Reserva = true;
						// Se insertan las instrucciones en espera.
						if (!enEspera1Reserva.isEmpty()) {
							insertarEspera(enEspera1Reserva, serverReserva);
							enEspera1Reserva.clear();
						}
						// Se inserta la nueva instruccion.
						resultado = insertarInstruccion(serverReserva, out, instruccion);
					} catch (Exception e) {
						// Si no se puede conectar con el servidor se añade a la espera
						enEspera1Reserva.add(instruccion);
					}
					// Si los 2 servidores estan apagados se notifica
					if (!server1 && !server1Reserva) {
						out.writeUTF("El servidor al que intenta acceder no esta operativo");
					} else {
						out.writeUTF(resultado);
					}
				} else if (partes.length <= 11 && partes.length >= 8) {
					try {
						// Se intenta la conexion.
						server = new Socket("localhost", 1237);
						// Se inserta la nueva instruccion.
						out.writeUTF(insertarInstruccion(server, out, instruccion));
					} catch (Exception e) {
						out.writeUTF("El servidor al que intenta acceder no esta operativo");
					}
				} else if (partes.length == 13) {
					try {
						// Se intenta la conexion.
						server = new Socket("localhost", 1238);
						// Se inserta la nueva instruccion.
						out.writeUTF(insertarInstruccion(server, out, instruccion));
					} catch (Exception e) {
						out.writeUTF("El servidor al que intenta acceder no esta operativo");
					}
				}
				// Prueba Tuplas:
				// [“Alberto”, “20”, “Suspenso”] lenght 7
				// [“Alberto”, “20”, “Suspenso”, "coliflor", "lechuga"] 11
				// [“Alberto”, “20”, “Suspenso”, "coliflor", "lechuga", "macarrones"] 13
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
