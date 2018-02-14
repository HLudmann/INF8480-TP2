package ca.polymtl.inf8480.tp2.distributor;

import ca.polymtl.inf8480.tp2.shared.*;
import sun.reflect.generics.tree.VoidDescriptor;

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
			stub = (ServerInterface) registry.lookup("server");
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

	}

	/*
	 * Demande la liste des serveurs de calculs disponibles
	 */
	private void listAvailableServers() {

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

	}
}