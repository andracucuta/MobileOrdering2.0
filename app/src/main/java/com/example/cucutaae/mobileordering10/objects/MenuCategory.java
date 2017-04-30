package com.example.cucutaae.mobileordering10.objects;

/**
 * Created by cucut on 4/18/2017.
 */

public class MenuCategory {
    public String name;
    public String url;

    public MenuCategory(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public MenuCategory(){}

    @Override
    public String toString() {
        return "MenuCategory{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
