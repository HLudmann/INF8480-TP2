package ca.polymtl.inf8480.tp2.shared;

import java.rmi.RemoteException;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * ComputeServerInterface
 */
public interface ComputeServerInterface extends Remote {

    Results myLookup() throws RemoteException;

    Results capacities() throws RemoteException;

    Results computePrime(int x) throws RemoteException;

    Results computePell(int x) throws RemoteException;
}