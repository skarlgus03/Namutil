package com.example.namutil.shop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ShopManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File SHOP_DIR = new File("config/namutil/shops");

    public static void saveShop(Shop shop) {
        if (!SHOP_DIR.exists()) SHOP_DIR.mkdirs();
        File file = new File(SHOP_DIR, shop.name + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(shop, writer);
        } catch (IOException e) {
            System.err.println("❌ 상점 저장 실패: " + e.getMessage());
        }
    }
    public static Shop loadShop(String name) {
        File file = new File(SHOP_DIR, name + ".json");
        if (!file.exists()) return null;
        try (Reader reader = new FileReader(file)) {
            return GSON.fromJson(reader, Shop.class);
        } catch (IOException e) {
            System.err.println("❌ 상점 로딩 실패: " + e.getMessage());
            return null;
        }
    }
}