package io.github.jsy.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class PlatformSignScreen extends Screen {

    private final BlockPos pos;
    private final String initialValue;
    private final Consumer<SaveResult> onSave;
    private EditBox textBox;

    public record SaveResult(BlockPos pos, String value) {}

    public PlatformSignScreen(BlockPos pos, String initialValue, Consumer<SaveResult> onSave) {
        super(Component.translatable("screen.jsy.platform_sign.title"));
        this.pos = pos;
        this.initialValue = initialValue;
        this.onSave = onSave;
    }

    @Override
    protected void init() {
        int left = width / 2 - 80;
        int top = height / 2 - 40;

        textBox = addRenderableWidget(new EditBox(font, left, top + 20, 160, 20,
                Component.translatable("screen.jsy.platform_sign.number")));
        textBox.setMaxLength(8);
        textBox.setValue(initialValue);

        addRenderableWidget(Button.builder(Component.translatable("gui.save"), button -> saveAndClose())
                .bounds(left, top + 50, 75, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(left + 85, top + 50, 75, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, title, width / 2, height / 2 - 60, 0xFFFFFF);
        guiGraphics.drawString(font, Component.translatable("screen.jsy.platform_sign.number"),
                width / 2 - 80, height / 2 - 20, 0xA0A0A0);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void saveAndClose() {
        onSave.accept(new SaveResult(pos, textBox.getValue()));
        onClose();
    }
}
