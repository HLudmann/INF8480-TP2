package ca.polymtl.inf8480.tp2.distributor;

import ca.polymtl.inf8480.tp2.shared.*;

/**
 * Ops
 */
public class Ops extends Thread {

    ComputeServerInterface stub;
    int[] pells;
    int[] primes;
    Results res = new Results();

    public Ops(ComputeServerInterface stub, int[] pells, int[] primes) {
        this.stub = stub;
        this.pells = pells;
        this.primes = primes;
    }

    public void run() {
        try {
            res = stub.compute(pells, primes);
            res.setIsSuccess(true);
        } catch (Exception e) {
            res.setPells(pells);
            res.setPrimes(primes);
        }
    }

    /**
     * @return the res
     */
    public Results getRes() {
        return res;
    }
}