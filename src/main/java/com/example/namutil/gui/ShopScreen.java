package com.example.namutil.gui;

import com.example.namutil.shop.Shop;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ShopScreen extends Screen {
    private final Shop shop;

    public ShopScreen(Shop shop) {
        super(Text.literal("📦 상점: " + shop.name));
        this.shop = shop;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 배경 그리기
        this.renderBackground(context);

        // 텍스트 출력
        context.drawText(this.textRenderer, "상점 이름: " + shop.name, this.width / 2 - 50, 40, 0xFFFFFF, false);
        context.drawText(this.textRenderer, "아이템 개수: " + shop.items.size(), this.width / 2 - 50, 60, 0xAAAAAA, false);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false; // 게임 멈추지 않음
    }
}
