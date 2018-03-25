package ca.polymtl.inf8480.tp2.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * ComputeServerInterface
 */
public interface ComputeServerInterface extends Remote {

    boolean myLookup() throws RemoteException;

    Results getCapacity() throws RemoteException;

    Results compute(ArrayList<Integer> pells, ArrayList<Integer> primes) throws RemoteException;
}
