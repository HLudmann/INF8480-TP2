package ca.polymtl.inf8480.tp2.shared;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

/**
 * Results
 * Classe polyvalante pour permettre l'envoie de diférents types de données
 * selon le cas, en réponse à la même requête.
 */
public class Results extends Object implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isSuccess = false;
    private List<String> availableServers = null;
    private int result = -1;
    private UID token = null;
    private int capacity = -1;
    private ArrayList<Integer> pells = null;
    private ArrayList<Integer> primes = null;

    public Results() {
    }

    /**
     * @param isSuccess the isSuccess to set
     */
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * @param availableServers the availableServers to set
     */
    public void setAvailableServers(List<String> availableServers) {
        this.availableServers = availableServers;
    }

    /**
     * @param result the result to set
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @param token the token to set
     */
    public void setToken(UID token) {
        this.token = token;
    }

    /**
     * @param pells the pells to set
     */
    public void setPells(ArrayList<Integer> pells) {
        this.pells = pells;
    }

    /**
     * @param primes the primes to set
     */
    public void setPrimes(ArrayList<Integer> primes) {
        this.primes = primes;
    }

    /**
     * @return the success of the operation
     */
    public boolean getIsSuccess() {
        return isSuccess;
    }

    /**
     * @param result the capacity to set
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @return the availableServers
     */
    public List<String> getAvailableServers() {
        return availableServers;
    }

    /**
     * @return the result
     */
    public int getResult() {
        return result;
    }

    /**
     * @return the token
     */
    public UID getToken() {
        return token;
    }

    /**
     * @return the pells
     */
    public ArrayList<Integer> getPells() {
        return pells;
    }

    /**
     * @return the primes
     */
    public ArrayList<Integer> getPrimes() {
        return primes;
    }
}