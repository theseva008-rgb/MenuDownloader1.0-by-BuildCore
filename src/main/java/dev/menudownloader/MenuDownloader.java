package dev.menudownloader;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.menudownloader.dumpers.MenuDumper;
import dev.menudownloader.dumpers.TabDumper;
import dev.menudownloader.dumpers.HologramDumper;
import dev.menudownloader.screens.MenuDownloaderPanelScreen;
import dev.menudownloader.util.TextFormatUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.text.Text;

public final class MenuDownloader implements ClientModInitializer {
    public static final String MOD_NAME = "MenuDownloader";

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(".panel")
                .executes(ctx -> {
                    if (ctx.getSource().getClient().player != null) {
                        ctx.getSource().getClient().setScreen(new MenuDownloaderPanelScreen());
                    }
                    return 1;
                }));

            dispatcher.register(ClientCommandManager.literal(".menu")
                .then(ClientCommandManager.literal("download")
                    .executes(ctx -> {
                        MenuDumper.dump(ctx.getSource().getClient());
                        return 1;
                    })));

            dispatcher.register(ClientCommandManager.literal(".tab")
                .then(ClientCommandManager.literal("download")
                    .executes(ctx -> {
                        TabDumper.dump(ctx.getSource().getClient());
                        return 1;
                    })));

            dispatcher.register(ClientCommandManager.literal(".holo")
                .then(ClientCommandManager.literal("download")
                    .executes(ctx -> {
                        HologramDumper.dump(ctx.getSource().getClient());
                        return 1;
                    })));
        });
    }

    public static void notify(net.minecraft.client.MinecraftClient client, String message, boolean success) {
        if (client == null || client.player == null) return;
        client.player.sendMessage(TextFormatUtil.prefix(message), false);
        if (success && client.world != null) {
            client.player.playSound(net.minecraft.sound.SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                0.45f, 1.35f);
        }
    }
}
