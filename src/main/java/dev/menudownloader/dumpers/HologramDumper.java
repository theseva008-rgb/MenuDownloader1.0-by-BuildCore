package dev.menudownloader.dumpers;

import dev.menudownloader.MenuDownloader;
import dev.menudownloader.util.FileUtil;
import dev.menudownloader.util.TextFormatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HologramDumper {
    private HologramDumper() {}

    private record Line(double y, String text) {}

    public static void dump(MinecraftClient client) {
        try {
            if (client == null || client.player == null || client.world == null) {
                MenuDownloader.notify(client, "Мир ещё не загружен.", false);
                return;
            }
            Box area = client.player.getBoundingBox().expand(3.0);
            List<Line> lines = new ArrayList<>();
            for (Entity entity : client.world.getOtherEntities(client.player, area)) {
                if (entity instanceof ArmorStandEntity stand && stand.hasCustomName()) {
                    Text name = stand.getCustomName();
                    if (name != null) lines.add(new Line(stand.getY(), name.getString()));
                } else if (entity instanceof DisplayEntity.TextDisplayEntity display) {
                    Text text = display.getText();
                    if (text != null && !text.getString().isBlank()) {
                        lines.add(new Line(display.getY(), text.getString()));
                    }
                }
            }
            if (lines.isEmpty()) {
                MenuDownloader.notify(client, "В радиусе 3 блоков видимые текстовые голограммы не найдены.", false);
                return;
            }
            lines.sort(Comparator.comparingDouble(Line::y).reversed());
            var pos = client.player.getBlockPos();
            StringBuilder yml = new StringBuilder("location: world:")
                .append(pos.getX()).append(":").append(pos.getY()).append(":").append(pos.getZ())
                .append("\nenabled: true\ndisplay-range: 48\npages:\n  page1:\n    lines:\n");
            for (Line line : lines) {
                yml.append("      - content: ").append(TextFormatUtil.yaml(line.text()))
                    .append("\n        height: 0.3\n");
            }
            Path dir = FileUtil.gameDir().resolve("mods").resolve("Holograms");
            Path target = dir.resolve("holo_1.yml");
            FileUtil.writeAsync(target, yml.toString(),
                () -> MenuDownloader.notify(client, "Текст голограммы сохранён в mods/Holograms/holo_1.yml.", true),
                e -> MenuDownloader.notify(client, "Ошибка сохранения голограммы: " + e.getMessage(), false));
        } catch (Exception e) {
            MenuDownloader.notify(client, "Ошибка обработки голограммы: " + e.getMessage(), false);
        }
    }
}
