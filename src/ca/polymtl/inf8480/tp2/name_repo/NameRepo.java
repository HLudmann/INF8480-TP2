package ca.polymtl.inf8480.tp2.name_repo;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.polymtl.inf8480.tp2.shared.NameRepoInterface;
import ca.polymtl.inf8480.tp2.shared.Results;

/**
 * NameRepo
 */
public class NameRepo implements NameRepoInterface {

    Map<String, String> users = new HashMap<String, String>();
    ArrayList<UID> tokens = new Arraylist<UID>();

    public static void main(String[] args) {
        NameRepo repo = new Server();
        repo.run();
    }

    public NameRepo() {
        super();
        loadUsers();
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("NameReop04", stub);
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
            Registry registry = LocateRegistry.getRegistry(hostname);
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
        Results res = new Results();
        Path path = Paths.get("all_server_names");
        List<String> servers = Files.readAllLines(path);
        List<String> available = new ArrayList<String>();

        for (String s : servers) {
            // Test connexion serveur
            ComputeServerInterface stub = loadServerStub(s);
            try {
                stub.myLookup();
                available.add(s);
            } catch (RemoteException e) {
            } catch (Exception e) {
                System.err.println("Error " + s + ": " + e.getMessage());
            }
        }
        path = Paths.get("available_servers");
        Files.write(path, available, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    }

    private void loadUsers() {
        Path path = Pahts.get("users");
        List<String> servers = Files.readAllLines(path);
        for (String line : servers) {
            String[] split = line.split(";");
            users.put(split[0], split[1]);
        }
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
            Path path = Paths.get("available_servers");
            res.setAvailableServers(Files.readAllLines(path));
            res.setIsSuccess(true);
        }
        return res;
    }
}