package ca.polymtl.inf8480.tp2.compute_server;

import ca.polymtl.inf8480.tp2.shared.*;

public class Distributor {

	private Path clientIDsPath = Paths.get(System.getenv("HOME") + "/IDs.txt");
	private Map<String, DistantFiles> fileMap = new HashMap<String, DistantFiles>();

	public static void main(String[] args) {
		Distributor server = new Distributor();
		server.run();
	}

	public Distributor() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			DistributorInterface stub = (DistributorInterface) UnicastRemoteObject.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("server", stub);
			System.out.println("Distributor ready.");
		} catch (ConnectException e) {
			System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
			System.err.println();
			System.err.println("Erreur: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erreur: " + e.getMessage());
		}
	}

	/*
	 * Méthode accessible par RMI. Additionne les deux nombres passés en
	 * paramètre.
	 */
}