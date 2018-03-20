package ca.polymtl.inf8480.tp2.name_repo;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.rmi.RemoteException;
import java.util.List;

import ca.polymtl.inf8480.tp2.shared.NameRepoInterface;
import ca.polymtl.inf8480.tp2.shared.Results;

/**
 * NameRepo
 */
public class NameRepo implements NameRepoInterface {

    public NameRepo() {

    }

    private ComputeServerInterface loadServerStub(String hostname) {
        ComputeServerInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            stub = (ComputeServerInterface) registry.lookup(hostname);
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

        // TODO : test des serveurs/registres RMI
        for (String s : servers) {
            // Test connexion serveur
            ComputeServerInterface stub = loadServerStub(s);
            if (stub.myLoockup()) {
                available.add(s);
            }
        }
        path = Paths.get("available_servers");
        Files.write(path, available, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    }

    @Override
    public Results login(String username, String password) throws RemoteException {
        Results res = new Results();
        Path path = Paths.get("users");
        List<String> users = Files.readAllLines(path);
        for (int i = 0; i < users.size(); i = i + 2) {
            if (users.get(i).contentEquals("uid=" + username) & (user.get(i + 1).contains("pwd=" + password))) {
                res.setIsSuccess(true);
            }
        }
        return res;
    }

    @Override
    public Results listAvailableServers() throws RemoteException {
        checkServers();
        Results res = new Results();
        Path path = Paths.get("available_servers");
        res.setAvailableServers(Files.readAllLines(path));
        res.setIsSuccess(true);
        return res;
    }
}