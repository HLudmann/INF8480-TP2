package ca.polymtl.inf8480.tp2.distributor;

import ca.polymtl.inf8480.tp2.shared.*;
import sun.reflect.generics.tree.VoidDescriptor;
import java.io.*;
import java.nio.file.Files;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import com.sun.tools.corba.se.idl.constExpr.BooleanNot;
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

public class Distributor {

	public static void main(String[] args) {

		if (args.length == 2 || (args.length == 3 && args[2].compareTo("false") == 0)) {
			String hostname = args[0];
			String filePath = args[1];
			boolean insecure = false;

			Distributor client = new Distributor(hostname, filePath, insecure);
			client.run();

		} else if (args.length == 3 && args[2].compareTo("true") == 0) {
			String hostname = args[0];
			String filePath = args[1];
			boolean insecure = true;

			Distributor client = new Distributor(hostname, filePath, insecure);
			client.run();

		} else {
			System.out.println("Erreur : command non reconnue.");
			System.out.println("Commandes valides :");
			System.out.println(" ./distributor IP/hostname FilePath [insecure]");
			System.out.println("	- IP/hostname of the name repository server (case sensitive)");
			System.out
					.println("	- absolute or relative path to file with the operation to be made (case sensitive)");
			System.out.println("	- insecure mode : true or false (case sensitive)");
		}
	}

	private NameRepoInterface nameRepoStub = null;
	private boolean insecure;
	private UID token;
	private ArrayList<ComputeServerInterface> stubs;
	private ArrayList<Integer> pellList = null;
	private ArrayList<Integer> primeList = null;
	private boolean secureMode = false;

	public Distributor(String hostname, String filePath, boolean insecure) {
		super();

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		nameRepoStub = loadNameRepoStub(hostname);
		this.insecure = insecure;
		Path path = Pahts.get(filePath);
		//step1: Open and read the file
		for (String line : Files.readAllLines(path)) {
			String[] split = line.split(" ");
			if (split[0].compareTo("pell")) {
				pellList.add(Integer.parseInt(split[1]));
			} else if (split[0].compareTo("prime")) {
				primeList.add(Integer.parseInt(split[1]));
			}
		}
	}

	private void run() {
		login();
		if (insecure) {
			computeInsecure();
		} else {
			computeSecure();
		}
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
		nameRepoStub.lookup();
		Scanner login = new Scanner(System.in);
		System.out.println("Enter your username: ");
		String username = login.next();
		System.out.println("Enter your password: ");
		String passwd = login.next();
		login.close();
		Results res = nameRepoStub.login(username, passwd);

		if (res.getIsSuccess()) {
			token = res.getToken();
		} else {
			throw new Exception("Wrong username and/or password");
		}
	}

	/*
	 * Demande la liste des serveurs de calculs disponibles
	 */
	private void listAvailableServers() {
		Results res = stub.listAvailableServers(token);
		System.out.println(res.getAvailableServers());
		stubs = new ArrayList<ComputeServerInterface>();
		ComputeServerInterface stub;
		for (String serv : res.getAvailableServers()) {
			stub = loadComputeServerStub(serv);
			stubs.add(stub);
		}
	}

	/*
	 * Effectue le calcul a partir du fichier donné. En mode securise.
	 */
	private void computeSecure() {

		int sum = 0;
		ArrayList<Ops> opList = new ArrayList<Ops>();

		while (!pellList.isEmpty() && !primeList.isEmpty()) {
			listAvailableServers();
			//beginning of a loop that calls all servers in succession
			for (ComputeServerInterface stub : stubs) {
				//step2 Check with the server[i] how many computing blocks they can take
				Results res = stub.getCapacity();
				int capacity = res.getCapacity();
				//step3 send the correct amount of blocks from the file (will have to be done in separate threads)
				res = new Results();
				int ops = capacity;
				ArrayList<Integer> pells;
				ArrayList<Integer> primes;
				int i = 0;
				while (!pellList.isEmpty() && i < Math.ceil((double) ops / 2)) {
					pells.add(pellList.remove(0));
					i++;
				}
				i = 0;
				while (!primeList.isEmpty() && i < Math.floor((double) ops / 2)) {
					primes.add(primeList.remove(0));
					i++;
				}
				//send the operations to the server
				Ops op = new Ops(stub, pells, primes);
				op.start();
				opList.add(op);
			}
			for (Ops o : opList) {
				op.join(5000);
				res = op.getRes();
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

	/*
	 * Effectue le calcul a partir du fichier donné. En mode non securise.
	 */
	private void computeInsecure() {

		int sum = 0;
		ArrayList<Ops[]> opList = new ArrayList<Ops>();

		while (!pellList.isEmpty() && !primeList.isEmpty()) {
			listAvailableServers();
			if (stubs.size() < 3) {
				throw new Exception("Error: not enough compute server.");
			}
			//beginning of a loop that calls all servers in succession
			for (int k = 0; k < stubs.size(); k += 3) {
				//step2 Check with the server[i] how many computing blocks they can take
				Results res = stub.getCapacity();
				int capacity = res.getCapacity();
				//step3 send the correct amount of blocks from the file (will have to be done in separate threads)
				int ops = capacity;
				ArrayList<Integer> pells;
				ArrayList<Integer> primes;
				int i = 0;
				while (!pellList.isEmpty() && i < Math.ceil((double) ops / 2)) {
					pells.add(pellList.remove(0));
					i++;
				}
				i = 0;
				while (!primeList.isEmpty() && i < Math.floor((double) ops / 2)) {
					primes.add(primeList.remove(0));
					i++;
				}
				//send the operations to the server
				Ops op0 = new Ops(stubs.get(i), pells, primes);
				Ops op1 = new Ops(stubs.get(i + 1), pells, primes);
				Ops op2 = new Ops(stubs.get(i + 2), pells, primes);
				op0.start();
				op1.start();
				op2.start();
				opList.add(new Ops[] { op1, op2, op3 });
			}
			for (Ops[] op : opList) {
				op[0].join(5000);
				op[1].join(5000);
				op[2].join(5000);
				Results res0 = op[0].getRes();
				Results res1 = op[1].getRes();
				Results res2 = op[2].getRes();

				int choice = -1;
				// We want at least twice the same result since the probaility of have twice the same wrong answer is really low.
				if ((res0.getIsSuccess() && res1.getIsSuccess())) {
					choice = res0.getResult();
				} else if ((res0.getIsSuccess() && res2.getIsSuccess())) {
					choice = res0.getResult();
				} else if ((res2.getIsSuccess() && res1.getIsSuccess())) {
					choice = res1.getResult();
				}

				if (choice != -1) {
					//step4 Add the result to the sum
					sum += choice % 4000;
				} else {
					//step4 Or if it has failed, put the ops back in the lists
					pellList.addAll(res0.getPells());
					primeList.addAll(res0.getPrimes());
				}
			}
		}
		//step5 verifiy that the file is read fully and that all the sums have been added...
		System.out.println(sum);
	}
}