package com.example.namutil.shop;

import com.example.namutil.gui.ScreenOpener;
import com.example.namutil.shop.Shop;
import com.example.namutil.shop.ShopManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ShopCommand {

    // 이 메서드를 ExampleMod에서 호출해야 명령어가 등록됨
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("shopadmin")
                    .then(CommandManager.literal("create")
                            .then(CommandManager.argument("name", StringArgumentType.word())
                                    .executes(ctx -> {
                                        String name = StringArgumentType.getString(ctx, "name");
                                        Shop shop = new Shop(name);
                                        ShopManager.saveShop(shop);
                                        ctx.getSource().sendFeedback(() ->
                                                Text.literal("📦 상점 '" + name + "'이(가) 생성되었습니다.").formatted(Formatting.GREEN), false);
                                        return 1;
                                    })
                            )
                    )
                    .then(CommandManager.literal("edit")
                            .then(CommandManager.argument("name", StringArgumentType.word())
                                    .executes(ctx -> {
                                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                                        if (player == null) {
                                            ctx.getSource().sendError(Text.literal("❌ 플레이어만 사용할 수 있습니다."));
                                            return 0;
                                        }

                                        String name = StringArgumentType.getString(ctx, "name");
                                        Shop shop = ShopManager.loadShop(name);
                                        if (shop == null) {
                                            player.sendMessage(Text.literal("❌ 상점을 찾을 수 없습니다: " + name).formatted(Formatting.RED));
                                            return 0;
                                        }

                                        ScreenOpener.openEditor(player, shop);
                                        return 1;
                                    })
                            )
                    )
            );
        });

    }
}
