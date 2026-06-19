package io.github.jsy.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class DirectionSignScreen extends Screen {

    private final BlockPos pos;
    private final String initialValue;
    private final Consumer<SaveResult> onSave;
    private EditBox textBox;

    public record SaveResult(BlockPos pos, String value) {}

    public DirectionSignScreen(BlockPos pos, String initialValue, Consumer<SaveResult> onSave) {
        super(Component.translatable("screen.jsy.direction_sign.title"));
        this.pos = pos;
        this.initialValue = initialValue;
        this.onSave = onSave;
    }

    @Override
    protected void init() {
        int left = width / 2 - 100;
        int top = height / 2 - 40;

        textBox = addRenderableWidget(new EditBox(font, left, top + 20, 200, 20,
                Component.translatable("screen.jsy.direction_sign.text")));
        textBox.setMaxLength(24);
        textBox.setValue(initialValue);

        addRenderableWidget(Button.builder(Component.translatable("gui.save"), button -> saveAndClose())
                .bounds(left, top + 50, 95, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(left + 105, top + 50, 95, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, title, width / 2, height / 2 - 60, 0xFFFFFF);
        guiGraphics.drawString(font, Component.translatable("screen.jsy.direction_sign.text"),
                width / 2 - 100, height / 2 - 20, 0xA0A0A0);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void saveAndClose() {
        onSave.accept(new SaveResult(pos, textBox.getValue()));
        onClose();
    }
}
