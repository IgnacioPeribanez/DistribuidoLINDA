package practicaLinda;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

// Clase que actua como el constructor de un servidor y controla sus conexiones.
public class Servidor {
	protected ServerSocket serverSocket; 
	protected Socket clientSocket;
	public ArrayList<String> lista = new ArrayList<String>(); // Lista que actua como una base de datos.

	/*
	 * Pre: -- 
	 * Post: Este metodo lanza un servidor y gestiona las conexiones recibidas por linda.
	 */
	public void startServer(int opcion) throws IOException {
		
		if (opcion == 1) {
			serverSocket = new ServerSocket(1235); // 1-3
		} else if (opcion == 2) {
			serverSocket = new ServerSocket(1236); // RESERVA
		} else if (opcion == 3) {
			serverSocket = new ServerSocket(1237); // 4-5
		} else if (opcion == 4) {
			serverSocket = new ServerSocket(1238); // 6
		}
		try {
			while (true) {
				System.out.println("Esperando...");
				clientSocket = serverSocket.accept();
				System.out.println("Linda en línea");
				LindaHandler LindaSock = new LindaHandler(clientSocket, lista);
				new Thread(LindaSock).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Pre: -- 
	 * Post: Este metodo se encarga de eliminar la tupla deseada por el usuario.
	 */
	public synchronized static String eliminar(String[] partesTuplas, ArrayList<String> lista, boolean eliminar) {
		String regex = "[A-Z]";
		// Se comparan las tuplas almacenadas con la tupla enviada.
		for (int i = 0; i < lista.size(); i++) {
			// Si acaba en true se devuelve la tupla.
			boolean borrar = true;
			int variable = 1;
			// Lista que almacena las similitudes.
			ArrayList<Boolean> booleanList = new ArrayList<Boolean>();
			// Tabla que almacena las variables.
			String comparacion[] = lista.get(i).split("\"");
			// Si tiene el mismo numero de variables se compara.
			if (comparacion.length == partesTuplas.length) {
				while (true) {
					// Si la variable pedida por el usuario es igual a la comparacion o se detecta como una variable cualquiera se apunta la similitud.
					if (partesTuplas[variable].equals(comparacion[variable]) || partesTuplas[variable].substring(0, 1).equalsIgnoreCase("?") && Pattern.matches(regex, partesTuplas[variable].substring(1, 2))) {
						booleanList.add(true);
					} else {
						booleanList.add(false);
						break;
					}
					// Variable recorrera los numeros impares hasta acabar el analisis. 
					// Las variables simpre se encuentran en la posicion impar de la tabla.
					variable = variable + 2;
					if (variable >= partesTuplas.length) {
						break;
					}
				}
				// Bucle que comprueba la similitud de las tuplas.
				for (int j = 0; j < booleanList.size(); j++) {
					if (booleanList.get(j) == false) {
						borrar = false;
						break;
					} 
				}
				// Si la tupla es igual se borra.
				if (borrar == true) {
					String eliminada = lista.get(i);
					lista.remove(i);
					return "Tupla " + eliminada + " eliminada";
				} 
			}
		}
		return "No se ha encontrado una tupla similar";
	}

}