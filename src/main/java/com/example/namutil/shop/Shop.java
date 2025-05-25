package com.example.namutil.shop;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Shop {
    public String name;
    public List<ShopItem> items = new ArrayList<>();

    public Shop(String name){
        this.name = name;
    }
}
