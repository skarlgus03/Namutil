//package com.example.namutil.client;
//
//import com.example.namutil.gui.ShopScreen;
//import com.example.namutil.shop.Shop;
//import com.google.gson.Gson;
//import net.fabricmc.api.ClientModInitializer;
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.screen.ingame.HandledScreens;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.util.Identifier;
//
//public class NamutilClient implements ClientModInitializer {
//
//    public static final Identifier OPEN_SHOP_GUI = new Identifier("namutil", "open_shop_gui");
//    private static final Gson GSON = new Gson();
//
//    @Override
//    public void onInitializeClient() {
//        ClientPlayNetworking.registerGlobalReceiver(OPEN_SHOP_GUI, (client, handler, buf, responseSender) -> {
//            String json = buf.readString();
//            Shop shop = GSON.fromJson(json, Shop.class);
//
//            client.execute(() -> {
//                Screen screen = new ShopScreen(shop);
//                client.setScreen(screen);
//            });
//        });
//    }
//}
