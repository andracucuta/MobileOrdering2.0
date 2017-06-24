package com.example.cucutaae.mobileordering10;

/**
 * Created by cucut on 6/18/2017.
 */

public class Table {
    public String name;
    public String url;

    public Table(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Table(){}

    @Override
    public String toString() {
        return "MenuCategory{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
