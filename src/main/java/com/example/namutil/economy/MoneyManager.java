package com.example.namutil.economy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.UUID;

public class MoneyManager {

    private static final HashMap<UUID, Integer> moneyMap = new HashMap<>();
    private static final Gson GSON = new Gson();
    private static final File SAVE_FILE = Path.of("money_data.json").toFile();


    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("money")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayer();
                        if (player == null) {
                            ctx.getSource().sendError(
                                    Text.literal("❌ ").formatted(Formatting.RED)
                                            .append(
                                                    Text.literal("이 명령어는 플레이어만 사용 가능해요.")
                                                            .formatted(Formatting.WHITE,Formatting.BOLD)
                                            )
                            );
                            return 0;
                        }
                        int money = moneyMap.getOrDefault(player.getUuid(), 0);
                        ctx.getSource().sendFeedback(() ->
                                net.minecraft.text.Text.of("💰 당신의 잔액: " + money + "G"), false);
                        return 1;
                    }));

            dispatcher.register(CommandManager.literal("addmoney")
                    .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                            .executes(ctx -> {
                                ServerPlayerEntity player = ctx.getSource().getPlayer();
                                if (player == null) {
                                    ctx.getSource().sendError(
                                            Text.literal("❌ ").formatted(Formatting.RED)
                                                    .append(
                                                            Text.literal("이 명령어는 플레이어만 사용 가능해요.")
                                                                    .formatted(Formatting.WHITE,Formatting.BOLD)
                                                    )
                                    );
                                    return 0;
                                }
                                int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                UUID uuid = player.getUuid();
                                int current = moneyMap.getOrDefault(uuid, 0);
                                moneyMap.put(uuid, current + amount);
                                ctx.getSource().sendFeedback(
                                        () -> Text.literal("💰 ")
                                                .formatted(Formatting.GOLD,Formatting.BOLD)
                                                .append(Text.literal(amount+"를 획득했습니다!").formatted(Formatting.WHITE,Formatting.BOLD)),
                                        false
                                );
                                return 1;
                            }))

            );
            dispatcher.register(CommandManager.literal("pay")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                            .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                    .executes(ctx -> {
                                        ServerPlayerEntity sender = ctx.getSource().getPlayer();
                                        if (sender == null) {
                                            ctx.getSource().sendError(
                                                    Text.literal("❌ ").formatted(Formatting.RED)
                                                            .append(
                                                                    Text.literal("이 명령어는 플레이어만 사용 가능해요.")
                                                                            .formatted(Formatting.WHITE,Formatting.BOLD)
                                                            )
                                            );
                                            return 0;
                                        }

                                        ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "target");
                                        int amount = IntegerArgumentType.getInteger(ctx, "amount");

                                        if (sender.getUuid().equals(target.getUuid())) {
                                            ctx.getSource().sendError(
                                                    Text.literal("❌ ").formatted(Formatting.RED,Formatting.BOLD)
                                                            .append(
                                                                    Text.literal("자기자신에게는 보낼 수 없어요")
                                                                            .formatted(Formatting.WHITE,Formatting.BOLD)
                                                            )
                                            );
                                            return 0;
                                        }

                                        int senderMoney = moneyMap.getOrDefault(sender.getUuid(), 0);
                                        if (senderMoney < amount) {
                                            ctx.getSource().sendError(
                                                    Text.literal("❌ ").formatted(Formatting.RED,Formatting.BOLD)
                                                            .append(
                                                                    Text.literal("잔액이 부족해요")
                                                                            .formatted(Formatting.WHITE,Formatting.BOLD)
                                                            )
                                            );
                                            return 0;
                                        }

                                        // 돈 이동
                                        moneyMap.put(sender.getUuid(), senderMoney - amount);
                                        moneyMap.put(target.getUuid(),
                                                moneyMap.getOrDefault(target.getUuid(), 0) + amount);

                                        ctx.getSource().sendFeedback(() ->
                                                        Text.literal("✅ ")
                                                                .formatted(Formatting.GREEN,Formatting.BOLD)
                                                                .append(Text.literal(target.getName().getString() + "에게 " + amount + "G 보냈습니다.").formatted(Formatting.WHITE,Formatting.BOLD)),
                                                false);
                                        target.sendMessage(
                                                Text.literal("💸 ")
                                                        .formatted(Formatting.GREEN,Formatting.BOLD)
                                                        .append(Text.literal(sender.getName().getString() + "로부터 " + amount + "G 받았습니다!").formatted(Formatting.WHITE,Formatting.BOLD))
                                        );

                                        return 1;
                                    })))
            );
        });
    }
    public static void loadMoneyData() {
        if (!SAVE_FILE.exists()) return;
        try (Reader reader = new FileReader(SAVE_FILE)) {
            Type type = new TypeToken<HashMap<UUID, Integer>>(){}.getType();
            HashMap<UUID, Integer> data = GSON.fromJson(reader, type);
            if (data != null) moneyMap.putAll(data);
        } catch (IOException e) {
            System.err.println("❌ 돈 데이터 불러오기 실패: " + e.getMessage());
        }
    }

    public static void saveMoneyData() {
        try (Writer writer = new FileWriter(SAVE_FILE)) {
            GSON.toJson(moneyMap, writer);
        } catch (IOException e) {
            System.err.println("❌ 돈 데이터 저장 실패: " + e.getMessage());
        }
    }


}
