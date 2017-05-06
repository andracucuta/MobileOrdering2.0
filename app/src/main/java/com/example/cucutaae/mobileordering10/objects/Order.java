package com.example.cucutaae.mobileordering10.objects;

import java.util.List;

/**
 * Created by cucut on 5/1/2017.
 */

public class Order {
    private String user;
    private String data;
    private String table;
    private Integer state;
    private List<MenuProduct> productList;


    public Order(String user, Integer state, String data, String table, List<MenuProduct> productList) {

        this.user = user;
        this.state = state;
        this.data = data;
        this.table = table;
        this.productList = productList;
    }

    public Order() {
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

    public List<MenuProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<MenuProduct> productList) {
        this.productList = productList;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Order{" +
                "user='" + user + '\'' +
                ", data='" + data + '\'' +
                ", table='" + table + '\'' +
                ", state=" + state +
                ", productList=" + productList +
                '}';
    }
}
