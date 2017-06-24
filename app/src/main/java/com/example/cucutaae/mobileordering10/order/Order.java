package com.example.cucutaae.mobileordering10.order;

import com.example.cucutaae.mobileordering10.menu.MenuProduct;
import com.example.cucutaae.mobileordering10.signin.User;
import java.io.Serializable;
import java.util.List;

/**
 * Created by cucut on 5/1/2017.
 */

public class Order implements Serializable{
    private String userName;
    private String table;
    private String data;
    private Integer state;
    private String confirmed;
    private String paymentType;

    public Order( String userName, Integer state, String data, String table, String confirmed, String paymentType) {
        this.userName = userName;
        this.state = state;
        this.data = data;
        this.table = table;
        this.confirmed = confirmed;
        this.paymentType = paymentType;
    }

    public Order(Integer state, String data, String table, List<MenuProduct> productList) {
        this.state = state;
        this.data = data;
        this.table = table;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    //    public Order(List<User> userList, Integer state, String data, String table) {
//        this.userList = userList;
//        this.state = state;
//        this.data = data;
//        this.table = table;
//
//    }

    public Order() {
    }


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public List<User> getUserList() {
//        return userList;
//    }
//
//    public void setUser(List<User> userList) {
//        this.userList = userList;
//    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

//    public List<MenuProduct> getProductList() {
//        return productList;
//    }
//
//    public void setProductList(List<MenuProduct> productList) {
//        this.productList = productList;
//    }

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
                "userName='" + userName + '\'' +
                ", table='" + table + '\'' +
                ", data='" + data + '\'' +
                ", state=" + state +
                ", confirmed='" + confirmed + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}
