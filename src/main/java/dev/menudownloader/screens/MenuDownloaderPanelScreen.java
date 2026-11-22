package dev.menudownloader.screens;

import dev.menudownloader.dumpers.HologramDumper;
import dev.menudownloader.dumpers.MenuDumper;
import dev.menudownloader.dumpers.TabDumper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class MenuDownloaderPanelScreen extends Screen {
    private static final int PURPLE = 0xFFA855F7;
    private static final int PANEL = 0xE6160A28;
    private static final int CARD = 0xFF28143F;

    public MenuDownloaderPanelScreen() {
        super(Text.literal("MenuDownloader Panel"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int w = 310, h = 246;
        int x = (this.width - w) / 2, y = (this.height - h) / 2;
        context.fill(x - 2, y - 2, x + w + 2, y + h + 2, PURPLE);
        context.fill(x, y, x + w, y + h, PANEL);
        context.drawCenteredTextWithShadow(this.textRenderer, "✦ MenuDownloader Panel ✦",
            this.width / 2, y + 14, 0xFFD8B4FE);
        context.drawCenteredTextWithShadow(this.textRenderer, "NEON PURPLE • FABRIC 1.21.1",
            this.width / 2, y + 31, 0xFFA855F7);

        String[] titles = {"Menu export", "TAB player list", "Hologram text", "Close"};
        String[] descriptions = {"Save visible container contents", "Save client-visible player list",
            "Export nearby visible text", "Return to game"};
        for (int i = 0; i < titles.length; i++) {
            int cy = y + 52 + i * 45;
            boolean hover = mouseX >= x + 16 && mouseX <= x + w - 16 && mouseY >= cy && mouseY <= cy + 37;
            context.fill(x + 16, cy, x + w - 16, cy + 37, hover ? 0xFF432060 : CARD);
            context.drawTextWithShadow(this.textRenderer, titles[i], x + 27, cy + 6, 0xFFD8B4FE);
            context.drawText(this.textRenderer, descriptions[i], x + 27, cy + 20, 0xFFB89ACF, false);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int w = 310, h = 246;
        int x = (this.width - w) / 2, y = (this.height - h) / 2;
        if (mouseX < x + 16 || mouseX > x + w - 16) return super.mouseClicked(mouseX, mouseY, button);
        int index = (int) ((mouseY - (y + 52)) / 45);
        if (index < 0 || index > 3 || mouseY > y + 52 + 4 * 45) return super.mouseClicked(mouseX, mouseY, button);
        MinecraftClient client = MinecraftClient.getInstance();
        switch (index) {
            case 0 -> MenuDumper.dump(client);
            case 1 -> TabDumper.dump(client);
            case 2 -> HologramDumper.dump(client);
            case 3 -> client.setScreen(null);
        }
        if (index != 3) client.setScreen(null);
        return true;
    }

    @Override
    public boolean shouldPause() { return false; }
}
