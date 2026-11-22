package dev.menudownloader.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

public final class FileUtil {
    private FileUtil() {}

    public static void writeAsync(Path path, String content, Runnable success, java.util.function.Consumer<Exception> failure) {
        CompletableFuture.runAsync(() -> {
            try {
                Files.createDirectories(path.getParent());
                Files.writeString(path, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                if (success != null) success.run();
            } catch (Exception e) {
                if (failure != null) failure.accept(e);
            }
        });
    }

    public static Path gameDir() {
        return net.minecraft.client.MinecraftClient.getInstance().runDirectory.toPath();
    }
}
