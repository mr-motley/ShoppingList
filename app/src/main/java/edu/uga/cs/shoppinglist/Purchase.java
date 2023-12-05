package edu.uga.cs.shoppinglist;

import java.util.List;

/**
 * POJO Class for purchases
 */
public class Purchase {
    private List<Item> items;
    private String purchaser;
    private Double price;
    private String buyDate;
    private String key;

    public Purchase(){
        this.key = null;
        this.purchaser = null;
        this.buyDate = null;
        this.items = null;
        this.price = -1.0;
    }

    public Purchase(List<Item> items, String uid, String date, double price){
        this.key = null;
        this.items = items;
        this.purchaser = uid;
        this.buyDate = date;
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Item> getItems(){
        return this.items;
    }

    public void setItems(List<Item> list) {
        this.items = list;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser){
        this.purchaser = purchaser;
    }

    public String getBuyDate(){
        return buyDate;
    }

    public void setBuyDate(String buyDate){
        this.buyDate = buyDate;
    }

    public double getPrice(){return price;}

    public void setPrice(double price){this.price = price;}
}

