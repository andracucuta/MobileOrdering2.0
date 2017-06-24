package com.example.cucutaae.mobileordering10.menu;

import java.io.Serializable;

/**
 * Created by cucut on 4/22/2017.
 */

public class MenuProduct implements Serializable {

    private String productId;
    private String name;
    private String category;
    private String ingredients;
    private String description;
    private float price;
    private int raiting;
    private String review;
    private String imageURL;
    private int cal;
    private int quantity;

    public MenuProduct(String productId, String name, String category, String ingredients, String description,
                       float price, int raiting, String review, String imageURL, int cal, int quantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.description = description;
        this.price = price;
        this.raiting = raiting;
        this.review = review;
        this.imageURL = imageURL;
        this.cal = cal;
        this.quantity = quantity;
    }

    public MenuProduct(String name, String category, String ingredients, String description,
                       float price, int raiting, String review, String imageURL, int cal, int quantity) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.description = description;
        this.price = price;
        this.raiting = raiting;
        this.review = review;
        this.imageURL = imageURL;
        this.cal = cal;
        this.quantity = quantity;
    }

    public MenuProduct() {

    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getRaiting() {
        return raiting;
    }

    public void setRaiting(int raiting) {
        this.raiting = raiting;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getCal() {
        return cal;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", raiting=" + raiting +
                ", review='" + review + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", cal=" + cal +
                ", quantity=" + quantity +
                '}';
    }
}
