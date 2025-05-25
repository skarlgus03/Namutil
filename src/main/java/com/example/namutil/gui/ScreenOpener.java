package com.example.namutil.gui;

import com.example.namutil.util.NamutilNetworking;
import com.example.namutil.shop.Shop;
import com.example.namutil.util.OpenShopGuiPayload;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public class ScreenOpener {
    public static void openEditor(ServerPlayerEntity player, Shop shop) {
        PacketByteBuf buf = PacketByteBufs.create();

        String json = new Gson().toJson(shop);
        ServerPlayNetworking.send(player, new OpenShopGuiPayload(json));

    }
}
