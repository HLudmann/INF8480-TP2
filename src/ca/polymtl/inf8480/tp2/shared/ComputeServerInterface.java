package ca.polymtl.inf8480.tp2.shared;

import java.rmi.RemoteException;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * ComputeServerInterface
 */
public interface ComputeServerInterface extends Remote {

    boolean myLookup() throws RemoteException;

    Results getCapacity() throws RemoteException;

    Results compute(ArrayList<int> pells, ArrayList<int> primes) throws RemoteException;
}
