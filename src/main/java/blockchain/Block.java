package main.java.blockchain;

public class Block {
    private int index;
    private int previousBlock;
    private Transaction[] transactions = new Transaction[5];

    public Block(int index, int previousBlock, Transaction[] transactions) {
        this.index = index;
        this.previousBlock = previousBlock;
        this.transactions = transactions;
    }

    /**
     * ToString method
     * @return
     */
    public String toString(){
        String string = "BLOCK:[cur:" + index + "prev:" + previousBlock + "transactions:[";
        for (Transaction trans: transactions) {
            string += trans.toString();
        }
        string += "]]";
        return string;
    }
}
