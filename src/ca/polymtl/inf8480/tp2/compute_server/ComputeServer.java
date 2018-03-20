package ca.polymtl.inf8480.tp2.compute_server;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import ca.polymtl.inf8480.tp2.shared.*;

public class ComputeServer implements ComputeServerInterface {

	private int fiability;
	private int capacity;

	public static void main(String[] args) {
		ComputeServer server = new ComputeServer();
		server.run();
	}

	public ComputeServer() {
		super();
	}

	private void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			ComputeServerInterface stub = (ComputeServerInterface) UnicastRemoteObject.exportObject(this, 0);

			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("ComputeServeur04", stub);
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
	@Override
	public boolean myLookup() throws RemoteException {
		return true;
	}

	@Override
	public Results getCapacity() throws RemoteException {
		Results res = new Results();
		res.setCapacity(capacity);
		return res;
	}

	@Override
	public Results compute(ArrayList<Integer> pells, ArrayList<Integer> primes) throws RemoteException {

		Results res = new Results();
		int sum = 0;

		//test de la capacité
		if (isOperationAccepted(pells.size() + primes.size()) == false) {
			return res;
		}

		//calcul des pells et addition à la somme
		for (int x : pells) {
			sum += (computePell(x) % 4000);
			sum %= 4000;
		}

		//calcul des primes et addition à la somme
		for (int x : primes) {
			sum += (computePrime(x) % 4000);
			sum %= 4000;
		}

		//vérification si le serveur est de mauvaise foi. Si le nombre généré est supérieur à la fiabilité il l'est.
		if ((int) Math.ceil(Math.random() * 100) <= fiability)
			res.setResult(sum);
		else
			res.setResult((int) Math.ceil(Math.random() * 4000));

		res.setIsSuccess(true);
		return res;
	}

	private int computePell(int x) throws RemoteException {
		return Operations.pell(x);
	}

	private int computePrime(int x) throws RemoteException {
		return Operations.prime(x);
	}

	private boolean isOperationAccepted(int operationsQuantity) throws RemoteException {
		//tests if a random number from 0 to 100 is smaller than the given equation to simulate refusal probability
		if ((int) Math.ceil(Math.random() * 100) <= ((operationsQuantity - capacity) / (5 * capacity) * 100)) {
			return true;
		} else {
			return false;
		}
	}
}