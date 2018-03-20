package ca.polymtl.inf8480.tp2.shared;

import java.rmi.RemoteException;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * ComputeServerInterface
 */
public interface ComputeServerInterface extends Remote {

    boolean myLookup() throws RemoteException;

    Results capacities() throws RemoteException;

    Results compute(int[] pells, int[] primes) throws RemoteException;
}