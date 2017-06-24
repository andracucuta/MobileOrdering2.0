package com.example.cucutaae.mobileordering10.order;

/**
 * Created by cucut on 5/3/2017.
 */

public class OrderProduct {
    private String key;
    private String orderKey;
    private Integer quantity;
    private String productName;
    private String price;
    private String user;
    private String table;


    public OrderProduct(Integer quantity, String productName, String price,String user, String table) {
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.user = user;
        this.table = table;
    }

    public OrderProduct(String key, String orderKey,Integer quantity, String productName, String price,String user, String table) {
        this.key = key;
        this.orderKey = orderKey;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.user = user;
        this.table = table;
    }

    public OrderProduct() {
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "key='" + key + '\'' +
                ", orderKey='" + orderKey + '\'' +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", user='" + user + '\'' +
                ", table='" + table + '\'' +
                '}';
    }
}
