package practicaLinda;

import java.io.IOException;
import java.util.Scanner;

import practicaAvionBien.Cliente;

//Clase principal que hará la funcion de lanzar los servidores
public class MainServidor {
	
	public static void main(String[] args) throws IOException {
		Scanner entrada = new Scanner(System.in);
		while (true) {
			System.out.println("Que accion quieres realizar?");
			System.out.println(
					"------------------------------------------------------------------------------------------------------");
			System.out.println("1.- Lanzar Linda");
			System.out.println("2.- Lanzar Serv1");
			System.out.println("3.- Lanzar Serv1Reserva");
			System.out.println("4.- Lanzar Serv2");
			System.out.println("5.- Lanzar Serv3");
			System.out.print("\n¿Que opcion deseas ejecutar?: ");
			int numero = entrada.nextInt();
			System.out.println("");
			if (numero > 5 || numero < 1) { // Mostramos el error en caso de introducir una opcion inexistente
				System.out.println("Error, elija una de las opciones validas");
			} else if (numero == 1) {
				Linda linda = new Linda(); // Se crea Linda
				linda.startServer(); // Se inicia Linda
				break;
			} else if (numero == 2) {
				Servidor servidor = new Servidor(); // Se crea el servidor1
				servidor.startServer(1);
			} else if (numero == 3) {
				Servidor servidor = new Servidor(); // Se crea el servidor1Reserva
				servidor.startServer(2);
			} else if (numero == 4) {
				Servidor servidor = new Servidor(); // Se crea el servidor2
				servidor.startServer(3);
			} else if (numero == 5) {
				Servidor servidor = new Servidor(); // Se crea el servidor3
				servidor.startServer(4);
			}
		}
	}
	
}