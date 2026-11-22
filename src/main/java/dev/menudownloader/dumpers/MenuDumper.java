package dev.menudownloader.dumpers;

import dev.menudownloader.MenuDownloader;
import dev.menudownloader.util.FileUtil;
import dev.menudownloader.util.TextFormatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.nio.file.Path;

public final class MenuDumper {
    private MenuDumper() {}

    public static void dump(MinecraftClient client) {
        try {
            if (client == null || client.player == null || client.currentScreen == null
                || !(client.currentScreen instanceof GenericContainerScreen)) {
                MenuDownloader.notify(client, "Открой обычный сундукоподобный интерфейс перед .menu download.", false);
                return;
            }
            if (!(client.player.currentScreenHandler instanceof GenericContainerScreenHandler handler)) {
                MenuDownloader.notify(client, "Не удалось определить верхний контейнер.", false);
                return;
            }

            int topSlots = Math.min(handler.getRows() * 9, handler.slots.size());
            StringBuilder out = new StringBuilder("# MenuDownloader — client-visible menu export\n")
                .append("menu_title: ").append(TextFormatUtil.yaml(client.currentScreen.getTitle().getString())).append("\n")
                .append("rows: ").append(handler.getRows()).append("\n")
                .append("items:\n");

            for (int i = 0; i < topSlots; i++) {
                Slot slot = handler.slots.get(i);
                ItemStack stack = slot.getStack();
                if (stack.isEmpty()) continue;
                out.append("  - slot: ").append(i).append("\n")
                    .append("    item: ").append(TextFormatUtil.yaml(net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).toString())).append("\n")
                    .append("    count: ").append(stack.getCount()).append("\n")
                    .append("    name: ").append(TextFormatUtil.yaml(stack.getName().getString())).append("\n")
                    .append("    components: ").append(TextFormatUtil.yaml(stack.getComponents().toString())).append("\n");
            }

            String name = TextFormatUtil.safeFileName(client.currentScreen.getTitle().getString());
            Path target = FileUtil.gameDir().resolve("mods").resolve("MenuDownloader").resolve(name + ".yml");
            FileUtil.writeAsync(target, out.toString(),
                () -> MenuDownloader.notify(client, "Меню сохранено: mods/MenuDownloader/" + name + ".yml", true),
                e -> MenuDownloader.notify(client, "Ошибка сохранения меню: " + e.getMessage(), false));
        } catch (Exception e) {
            MenuDownloader.notify(client, "Ошибка обработки меню: " + e.getMessage(), false);
        }
    }
}
