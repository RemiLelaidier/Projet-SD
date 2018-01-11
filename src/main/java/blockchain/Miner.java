package main.java.blockchain;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Miner {


    public static void main(String[] args) {
        // Transactions
        Transaction[] transactions = new Transaction[5];
        transactions[0] = new Transaction(0, 0, 1, "01/01/18", false, 12.0);
        transactions[1] = new Transaction(1, 0, 2, "01/01/18", false, 12.0);
        transactions[2] = new Transaction(2, 0, 3, "01/01/18", false, 12.0);
        transactions[3] = new Transaction(3, 0, 4, "01/01/18", false, 12.0);
        transactions[4] = new Transaction(4, 0, 2, "01/01/18", false, 12.0);

        // Block
        Block genesis = new Block(0, -1,transactions);

        // Pow
        Pow pow = new Pow(genesis);

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future future = es.submit(pow);

        byte[] result;
            try {
                result = (byte[])future.get();
                String strRes = new String(result);
                System.out.println(strRes);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
    }

}
