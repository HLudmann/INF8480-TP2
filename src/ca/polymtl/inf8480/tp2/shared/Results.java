package ca.polymtl.inf8480.tp2.shared;

import java.io.Serializable;
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
    private int result = null;
    private String err = null;

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
     * @param err the err to set
     */
    public void setErr(String err) {
        this.err = err;
    }

    /**
     * @return the success of the operation
     */
    public boolean getIsSuccess() {
        return isSuccess;
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
     * @return the err
     */
    public String getErr() {
        return err;
    }
}