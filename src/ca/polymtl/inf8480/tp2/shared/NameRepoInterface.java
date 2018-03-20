package ca.polymtl.inf8480.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NameRepoInterface extends Remote {

	Results login(String username, String password) throws RemoteException;

	Results listAvailableServers() throws RemoteException;
}
