package practicaLinda;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

// Clase que actua como un controlador para las peciciones de linda sobre cualquier servidor.
public class LindaHandler implements Runnable {
	private Socket clientSocket;
	public ArrayList<String> lista;

	public LindaHandler(Socket socket, ArrayList<String> lista) {
		this.clientSocket = socket;
		this.lista = lista;
	}
	
	/*
	 * Pre: -- 
	 * Post: Este metodo se encarga de leer las tuplas almacenadas y compararlas con la tupla enviada por el usuario. 
	 * 		 Dependiendo de los parametros que envie el usuario se eliminara o se devolvera la tupla.
	 */
	public String leer(String[] partesTuplas, boolean eliminar) {
		String regex = "[A-Z]";
		// Se comparan las tuplas almacenadas con la tupla enviada.
		for (int i = 0; i < this.lista.size(); i++) {
			// Si acaba en true se devuelve la tupla.
			boolean devolver = true;
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
						devolver = false;
						break;
					} 
				}
				// Si la tupla es igual se devuelve.
				if (devolver == true) {
					return(lista.get(i));
				} 
			}
		}
		return "No se ha encontrado una tupla similar";
	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			while (true) {
				String instruccion = in.readUTF();
				String partes[] = instruccion.split(" ", 2);
				String partesTuplas[] = partes[1].split("\"");
				System.out.println(instruccion);
				if (partes[0].equals("PostNote")) {
					lista.add(partes[1]);
					System.out.println("Lista actual: ");
					for (int i = 0; i < lista.size(); i++) {
						System.out.println(lista.get(i));
					}
					out.writeUTF("Tupla incluida con exito");
				} else if (partes[0].equals("ReadNote")) {
					out.writeUTF(leer(partesTuplas, false));
				} else if (partes[0].equals("RemoveNote")) {
					out.writeUTF(Servidor.eliminar(partesTuplas, lista, true));
				} else {
					out.writeUTF("Elije una opcion valida");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
