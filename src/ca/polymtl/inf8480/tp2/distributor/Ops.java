package ca.polymtl.inf8480.tp2.distributor;

import ca.polymtl.inf8480.tp2.shared.*;

/**
 * Ops
 */
public class Ops extends Thread {

    ComputeServerInterface stub;
    ArrayList<Integer> pells;
    ArrayList<Integer> primes;
    Results res = new Results();

    public Ops(ComputeServerInterface stub, ArrayList<Integer> pells, ArrayList<Integer> primes) {
        this.stub = stub;
        this.pells = pells;
        this.primes = primes;
        res.setPells(pells);
        res.setPrimes(primes);
    }

    public void run() {
        try {
            res = stub.compute(pells, primes);
        } catch (Exception e) {
        }
    }

    /**
     * @return the res
     */
    public Results getRes() {
        return res;
    }
}