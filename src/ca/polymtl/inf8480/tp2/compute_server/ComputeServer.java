package ca.polymtl.inf8480.tp2.compute_server;

import java.rmi.RemoteException;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import ca.polymtl.inf8480.tp2.shared.*;

public class ComputeServer implements ComputeServerInterface {

	private int fiability = 100;

	public static void main(String[] args) {
		Distributor server = new Distributor();
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
			DistributorInterface stub = (DistributorInterface) UnicastRemoteObject.exportObject(this, 0);

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
	public Results capacities() throws RemoteException {
		return null;
	}

	@Override
	public Results computePrime(int x) throws RemoteException {
		if((int) Math.ceil(Math.random() * 100) <= m)
			return Operations.prime(x);
		else
			return (int) Math.ceil(Math.random() * 4000);
	}

	@Override
	public Results computePell(int x) throws RemoteException {
		if((int) Math.ceil(Math.random() * 100) <= m)
			return Operations.pell(x);
		else
			return (int) Math.ceil(Math.random() * 4000);
	}

	@Override
	public boolean setFiability(int m) throws RemoteException {
		this.fiability = m;
		return true;
	}

}