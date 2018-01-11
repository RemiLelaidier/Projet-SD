package main.java.blockchain;

import java.util.Date;

public class Transaction {
    private int id;
    private int idOwner;
    private int idBuyer;
    private String date;
    private boolean direct;
    private double price;

    public Transaction(int id, int idOwner, int idBuyer, String date, boolean direct, double price) {
        this.id = id;
        this.idOwner = idOwner;
        this.idBuyer = idBuyer;
        this.date = date;
        this.direct = direct;
        this.price = price;
    }

    public String toString(){
        return "Transaction" + id +
                "[own" + idOwner +
                "buy" + idBuyer +
                "date" + date +
                "direct" + direct +
                "price" + price + "]";
    }
}
