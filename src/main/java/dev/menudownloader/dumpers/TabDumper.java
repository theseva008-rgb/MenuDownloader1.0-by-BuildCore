package dev.menudownloader.dumpers;

import dev.menudownloader.MenuDownloader;
import dev.menudownloader.util.FileUtil;
import dev.menudownloader.util.TextFormatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.nio.file.Path;

public final class TabDumper {
    private TabDumper() {}

    public static void dump(MinecraftClient client) {
        try {
            if (client == null || client.player == null || client.getNetworkHandler() == null) {
                MenuDownloader.notify(client, "Нет активного подключения к серверу.", false);
                return;
            }
            StringBuilder config = new StringBuilder("# Информация, доступная клиенту; оригинальный TAB config не извлекается.\n")
                .append("players:\n");
            for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
                String name = entry.getProfile().getName();
                String display = entry.getDisplayName() == null ? name : entry.getDisplayName().getString();
                config.append("  - name: ").append(TextFormatUtil.yaml(name)).append("\n")
                    .append("    display: ").append(TextFormatUtil.yaml(display)).append("\n")
                    .append("    latency: ").append(entry.getLatency()).append("\n");
            }
            Path dir = FileUtil.gameDir().resolve("mods").resolve("TAB");
            FileUtil.writeAsync(dir.resolve("client-visible.yml"), config.toString(),
                () -> MenuDownloader.notify(client, "Список игроков сохранён в mods/TAB/client-visible.yml.", true),
                e -> MenuDownloader.notify(client, "Ошибка сохранения TAB: " + e.getMessage(), false));
        } catch (Exception e) {
            MenuDownloader.notify(client, "Ошибка чтения TAB: " + e.getMessage(), false);
        }
    }
}
