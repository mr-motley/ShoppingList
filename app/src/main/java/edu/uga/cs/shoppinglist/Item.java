package edu.uga.cs.shoppinglist;

/**
 *  POJO Class for
 */
public class Item {
    private String key;
    private String name;

    private String quantity;

    public Item(){
        this.key = null;
        this.name = null;
        this.quantity = null;
    }
    public Item(String name, String quantity){
        this.key = null;
        this.name = name;
        this.quantity = quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity(){
        return quantity;
    }

    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

    public String toString(){
        return "Item: " + name + " " + quantity;
    }
}
