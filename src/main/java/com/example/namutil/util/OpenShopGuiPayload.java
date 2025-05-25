package com.example.namutil.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenShopGuiPayload(String json) implements CustomPayload {

    public static final Id<OpenShopGuiPayload> ID = new Id<>(Identifier.of("namutil", "open_shop_gui"));

    public OpenShopGuiPayload(PacketByteBuf buf) {
        this(buf.readString());
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(json);
    }

    @Override
    public Id<OpenShopGuiPayload> getId() {
        return ID;
    }
}