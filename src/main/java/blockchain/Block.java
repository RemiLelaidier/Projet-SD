package main.java.blockchain;

public class Block {
    private int index;
    private Block previousBlock;
    private Transaction[] transactions = new Transaction[5];

    public Block(int index, Block previousBlock, Transaction[] transactions) {
        this.index = index;
        this.previousBlock = previousBlock;
        this.transactions = transactions;
    }

    /**
     * ToString method
     * @return
     */
    public String toString(){
        String string = "BLOCK:[cur:" + index;
        // Previous Block checking;
        if(previousBlock != null){
            string += "prev:" + previousBlock.toString();
        }
        else {
            string += "prev:null";
        }

        // Transactions to String
        string += "transactions:[";
        for (Transaction trans: transactions) {
            string += trans.toString();
        }
        string += "]]";
        return string;
    }
}
