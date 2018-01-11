package main.java.blockchain;

/**
 * Transaction class
 */
public class Transaction {
    private int id;
    private int idOwner;
    private int idBuyer;
    private int idObject;
    private String date;
    private boolean direct;
    private double price;

    public Transaction(int id, int idOwner, int idBuyer, int idObject, String date, boolean direct, double price) {
        this.id = id;
        this.idOwner = idOwner;
        this.idBuyer = idBuyer;
        this.idObject = idObject;
        this.date = date;
        this.direct = direct;
        this.price = price;
    }

    public String toString(){
        return "Transaction" + id +
                "[own" + idOwner +
                "buy" + idBuyer +
                "obj" + idObject +
                "date" + date +
                "direct" + direct +
                "price" + price + "]";
    }
}
