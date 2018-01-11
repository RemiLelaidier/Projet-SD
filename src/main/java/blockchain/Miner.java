package main.java.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Miner {


    public static void main(String[] args) {
        // Transactions
        Transaction[] transactionsB1 = new Transaction[5];
        transactionsB1[0] = new Transaction(0, 0, 1, 0,"01/01/18", false, 12.0);
        transactionsB1[1] = new Transaction(1, 0, 2, 0,"01/01/18", false, 26.0);
        transactionsB1[2] = new Transaction(2, 0, 3, 0,"01/01/18", false, 14.78);
        transactionsB1[3] = new Transaction(3, 0, 4, 0,"01/01/18", false, 1023.21);
        transactionsB1[4] = new Transaction(4, 0, 2, 1,"01/01/18", false, 0.54);
        Transaction[] transactionsB2 = new Transaction[5];
        transactionsB2[0] = new Transaction(5, 0, 1, 1,"01/01/18", false, 9.0);
        transactionsB2[1] = new Transaction(6, 0, 2, 2,"01/01/18", false, 28.48);
        transactionsB2[2] = new Transaction(7, 0, 3, 16,"01/01/18", false, 12.0);
        transactionsB2[3] = new Transaction(8, 0, 4, 1,"01/01/18", false, 10.50);
        transactionsB2[4] = new Transaction(9, 0, 2, 0,"01/01/18", false, 2000.0);

        // Blocks
        Block genesis = new Block(0, null,transactionsB1);
        Block block1 = new Block(1, genesis, transactionsB2);

        // Pow
        Pow pow = new Pow(block1);

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future future = es.submit(pow);

        byte[] result;
            try {
                result = (byte[])future.get();
                System.out.println("Block validé");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
    }

}
