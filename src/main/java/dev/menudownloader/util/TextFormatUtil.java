package dev.menudownloader.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class TextFormatUtil {
    private TextFormatUtil() {}

    public static MutableText prefix(String message) {
        return Text.literal("[")
            .formatted(Formatting.DARK_PURPLE)
            .append(Text.literal("MenuDownloader").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD))
            .append(Text.literal("] ").formatted(Formatting.DARK_PURPLE))
            .append(Text.literal(message).formatted(Formatting.WHITE));
    }

    public static String yaml(String value) {
        if (value == null) value = "";
        return "'" + value.replace("\\", "\\\\")
            .replace("'", "''")
            .replace("\r", "")
            .replace("\n", "\\n") + "'";
    }

    public static String safeFileName(String value) {
        String cleaned = value == null ? "unnamed" : value.replaceAll("[^a-zA-Z0-9._-]", "_");
        return cleaned.isBlank() ? "unnamed" : cleaned;
    }
}
