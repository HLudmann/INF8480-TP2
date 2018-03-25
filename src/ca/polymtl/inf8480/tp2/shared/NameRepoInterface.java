package ca.polymtl.inf8480.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.List;

public interface NameRepoInterface extends Remote {

	boolean myLookup() throws RemoteException;

	Results login(String username, String password) throws RemoteException;

	Results listAvailableServers(UID token) throws RemoteException;
}
