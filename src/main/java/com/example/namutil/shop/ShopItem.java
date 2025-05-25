package com.example.namutil.shop;

public class ShopItem {
    public String itemId; // 예: minecraft:diamond
    public int price;
    public int amount;

    public ShopItem(String itemId, int price, int amount) {
        this.itemId = itemId;
        this.price = price;
        this.amount = amount;
    }
}
