package ca.polymtl.inf8480.tp2.distributor;

import ca.polymtl.inf8480.tp2.shared.*;
import sun.reflect.generics.tree.VoidDescriptor;
import java.io.*;

public class Distributor {
	public static void main(String[] args) {
		
		// TODO parsing des arguments

		if (args.length == 1) {
			....
		}

		Distributor client = new Distributor(hostname, fun, arg);
		client.run();
	}

	private ServerInterface stub = null;
	private String clientID;
	private Path rootPath = Paths.get(System.getenv("HOME") + "/");
	private String fun;
	private String arg;
	private Undefined[] availableServers;
	private String[][] operations = null;

	public Distributor(...) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if (hostname != null) {
			stub = loadServerStub(hostname);
		}
	}

	private void run() {

	}

	private ServerInterface loadServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup(hostname);
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
		return stub;
	}

	/*
	 * Connexion au server de noms
	 */
	private void login() {
		stub.lookup();
	}

	/*
	 * Demande la liste des serveurs de calculs disponibles
	 */
	private void listAvailableServers() {
		Results res = stub.listAvailableServers();
		System.out.println(res.availableServers);
	}

	/*
	 * Demande les ressources disponibles (au moment de la demande)
	 */
	private int getServerResources(String hostname) {
		return null;
	}

	/*
	 * Effectue le calcul a partir du fichier donné
	 */
	private void compute(String path) {
		int sum = 0;

		//step1: Open and read the file


		//beginning of a loop that calls all servers in succession
		(for int i=0; i<availableServers.length(); i++ ){
			//step2 Check with the server[i] how many computing blocks they can take
			Results res = availableServers[i].capacities();
			int capacity = res.getCapacity();
			//step3 send the correct amount of blocks from the file (will have to be done in separate threads)
			//?? should we make operations successively or send them all in one call??
			operations = null;
			for(int j = 0; j<= capacity; j++){
				//read the file and add the operations to the operations[][]
			}
			//send the operations to the server
			Results res = availableServer[i].compute(operations);
			//step4 Add the result to the sum
			sum += res.getResult();
		}


		//step5 verifiy that the file is read fully and that all the sums have been added...
		System.out.println(sum);
	}

	
}