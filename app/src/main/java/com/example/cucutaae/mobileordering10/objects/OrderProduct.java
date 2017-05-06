package com.example.cucutaae.mobileordering10.objects;

/**
 * Created by cucut on 5/3/2017.
 */

public class OrderProduct {
    private Integer quantity;
    private String productName;

    public OrderProduct(Integer quantity, String productName) {
        this.quantity = quantity;
        this.productName = productName;
    }

    public OrderProduct() {
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

    @Override
    public String toString() {
        return "OrderProduct{" +
                "quantity='" + quantity + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
