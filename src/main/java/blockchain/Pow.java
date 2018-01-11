package main.java.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

public class Pow implements Callable<byte[]>{

    private static int COMPLEXITY = 2;
    private Block block;

    public Pow(Block block){
        this.block = block;
    }

    @Override
    public byte[] call() throws Exception {
        return mineBlock();
    }

    /**
     * MineBlock method
     * @return
     */
    private byte[] mineBlock() throws NoSuchAlgorithmException {
        String stringBloc = block.toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash;
        int i = 0;
        // Resolve
        do {
            String preHashBloc = stringBloc + i;
            System.out.println(i + " : " + preHashBloc);
            hash = digest.digest(preHashBloc.getBytes(StandardCharsets.UTF_8));
            i++;
            // Check resolved
        }while (!checkBlock(hash));
        return hash;
    }

    /**
     * Check if the 10 first byte are 0
     * @param hash
     * @return
     */
    private boolean checkBlock(byte[] hash){
        for (int i = 0; i < COMPLEXITY; i++) {
            if(hash[i] != 0x00){
                return  false;
            }
        }
        return true;
    }
}
