package ca.polymtl.inf8480.tp2.distributor;

import ca.polymtl.inf8480.tp2.shared.*;
import sun.reflect.generics.tree.VoidDescriptor;
import java.io.*;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Map;

public class Distributor {

	public static void main(String[] args) {
		
		// TODO parsing des arguments

		if (args.length == 1) {
			....
		}

		Distributor client = new Distributor(hostname, fun, arg);
		client.run();
	}

	private NameRepoInterface nameRepoStub = null;
	private UID token;
	private Path rootPath = Paths.get(System.getenv("HOME") + "/");
	private ArrayList<String> availableServers;
	private ArrayList<ComputeServerInterface> stubs;
	private ArrayList<Integer> pellList = null;
	private ArrayList<Integer> primeList = null;
	private boolean secureMode = false;

	public Distributor(...) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if (hostname != null) {
			nameRepoStub = loadNameRepoStub(hostname);
		}
	}

	private void run() {

	}

	private NameRepoInterface loadNameRepoStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("NameRepo04");
		} catch (NotBoundException e) {
			System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
		} catch (AccessException e) {
			System.out.println("Erreur: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Erreur: " + e.getMessage());
		}
		return stub;
	}

	private ComputeServerInterface loadComputeServerStub(String hostname) {
		ServerInterface stub = null;

		try {
			Registry registry = LocateRegistry.getRegistry(hostname);
			stub = (ServerInterface) registry.lookup("ComputeServeur04");
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
	 * Passage à mode sécurisé
	 */
	private void setSecureMode() {
		secureMode = true;
	}

	/*
	 * Passage à mode non-sécurisé
	 */
	private void setNonSecureMode() {
		secureMode = false;
	}

	/*
	 * Demande la liste des serveurs de calculs disponibles
	 */
	private void listAvailableServers() {
		Results res = stub.listAvailableServers();
		System.out.println(res.getAvailableServers());
		stubs = new ArrayList<ComputeServerInterface>();
		ComputeServerInterface stub;
		for (String serv : res.getAvailableServers()) {
			stub = loadComputeServerStub(serv);
			stubs.add(stub);
		}
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
		ArrayList<Ops> opList = new ArrayList<Ops>();

		//step1: Open and read the file

		while (!pellList.isEmpty() && !primeList.isEmpty()) {
			//beginning of a loop that calls all servers in succession
			for (ComputeServerInterface stub : stubs) {
				//step2 Check with the server[i] how many computing blocks they can take
				Results res = stub.capacities();
				int capacity = res.getCapacity();
				//step3 send the correct amount of blocks from the file (will have to be done in separate threads)
				res = new Results();
				int ops = capacity;
				int[] pells;
				int[] primes;
				int i = 0;
				while (!pellList.isEmpty() && i < Math.ceil((double) ops / 2)) {
					pells[i] = pellList.remove(0);
					i++;
				}
				i = 0;
				while (!primeList.isEmpty() && i < Math.floor((double) ops / 2)) {
					primes[i] = primeList.remove(0);
					i++;
				}
				//send the operations to the server
				Ops o = new Ops(stub, pells, primes);
				o.start();
				opList.add(o);
			}
			for (Ops o : opList) {
				o.join();
				res = o.getRes();
				if (res.getIsSuccess()) {
					//step4 Add the result to the sum
					sum += res.getResult() % 4000;
				} else {
					//step4 Or if it has failed, put the ops back in the lists
					pellList.addAll(res.getPells());
					primeList.addAll(res.getPrimes());
				}
			}
		}
		//step5 verifiy that the file is read fully and that all the sums have been added...
		System.out.println(sum);
	}

}