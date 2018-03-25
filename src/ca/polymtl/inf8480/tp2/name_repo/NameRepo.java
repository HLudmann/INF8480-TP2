package ca.polymtl.inf8480.tp2.name_repo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.polymtl.inf8480.tp2.shared.*;

/**
 * NameRepo
 */
public class NameRepo implements NameRepoInterface {

    Map<String, String> users = new HashMap<String, String>();
    ArrayList<UID> tokens = new ArrayList<UID>();
    ArrayList<String> availableServers = new ArrayList<String>();
    long epochNow = System.currentTimeMillis();

    public static void main(String[] args) {
        NameRepo repo = new NameRepo();
        repo.run();
    }

    public NameRepo() {
        super();
        loadUsers();
        checkServers();
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            NameRepoInterface stub = (NameRepoInterface) UnicastRemoteObject.exportObject(this, 5043);

            Registry registry = LocateRegistry.getRegistry(5044);
            registry.rebind("NameRepo04", stub);
            System.out.println("Server ready.");
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private ComputeServerInterface loadServerStub(String hostname) {
        ComputeServerInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname, 5040);
            stub = (ComputeServerInterface) registry.lookup("ComputeServer04");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return stub;
    }

    private void checkServers() {
        Path path = Paths.get("all_server_names");
        ArrayList<String> available = new ArrayList<String>();
        List<String> servers = new ArrayList<String>();
        try {
            servers = Files.readAllLines(path);
        } catch (Exception e) {
            System.out.println("Erreure: " + e.getMessage());
        }
        for (String s : servers) {
			try {
				System.out.println(s);
				// Test connexion serveur
				ComputeServerInterface stub = loadServerStub(s);
				stub.myLookup();
				available.add(s);
			} catch (Exception e) {}
		}
		availableServers = available;
    }

    private void loadUsers() {
        try {
            Path path = Paths.get("users");
            List<String> servers = Files.readAllLines(path);
            for (String line : servers) {
                String[] split = line.split(";");
                users.put(split[0], split[1]);
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public boolean myLookup() throws RemoteException {
        return true;
    }

    @Override
    public Results login(String username, String password) throws RemoteException {
        Results res = new Results();
        if (users.containsKey(username)) {
            if (users.get(username).compareTo(password) == 0) {
                res.setIsSuccess(true);
                UID token = new UID();
                res.setToken(token);
                tokens.add(token);
            }
        }
        return res;
    }

    @Override
    public Results listAvailableServers(UID token) throws RemoteException {
        checkServers();
        Results res = new Results();
        if (tokens.contains(token)) {
            if (System.currentTimeMillis() - epochNow > 60000) {
                checkServers();
            }
            res.setAvailableServers(availableServers);
            res.setIsSuccess(true);
        }
        return res;
    }
}
