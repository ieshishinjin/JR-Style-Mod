package io.github.jsy.client.screen;

import io.github.jsy.block.JRStationSignVariant;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class JRStationSignScreen extends Screen {

    private static final List<LineColorPreset> PRESETS = List.of(
            new LineColorPreset("preset.yamanote", 0x9ACD32),
            new LineColorPreset("preset.chuo", 0xF15A22),
            new LineColorPreset("preset.keihin_tohoku", 0x00BFFF),
            new LineColorPreset("preset.saikyo", 0x2E8B57)
    );

    private final BlockPos pos;
    private final String initialStationName;
    private final String initialLineName;
    private final String initialStationNumber;
    private final int initialLineColor;
    private final JRStationSignVariant initialVariant;
    private final Consumer<SignSaveData> onSave;

    private EditBox stationNameBox;
    private EditBox lineNameBox;
    private EditBox stationNumberBox;
    private EditBox colorBox;
    private CycleButton<JRStationSignVariant> variantButton;

    public JRStationSignScreen(BlockPos pos, String stationName, String lineName,
                               String stationNumber, int lineColor, JRStationSignVariant variant,
                               Consumer<SignSaveData> onSave) {
        super(Component.translatable("screen.jsy.jr_station_sign.title"));
        this.pos = pos;
        this.initialStationName = stationName;
        this.initialLineName = lineName;
        this.initialStationNumber = stationNumber;
        this.initialLineColor = lineColor;
        this.initialVariant = variant;
        this.onSave = onSave;
    }

    @Override
    protected void init() {
        int left = width / 2 - 110;
        int top = height / 2 - 90;

        stationNameBox = addRenderableWidget(new EditBox(font, left, top + 20, 220, 20, Component.translatable("screen.jsy.jr_station_sign.station_name")));
        stationNameBox.setMaxLength(32);
        stationNameBox.setValue(initialStationName);

        lineNameBox = addRenderableWidget(new EditBox(font, left, top + 50, 220, 20, Component.translatable("screen.jsy.jr_station_sign.line_name")));
        lineNameBox.setMaxLength(16);
        lineNameBox.setValue(initialLineName);

        stationNumberBox = addRenderableWidget(new EditBox(font, left, top + 80, 220, 20, Component.translatable("screen.jsy.jr_station_sign.station_number")));
        stationNumberBox.setMaxLength(8);
        stationNumberBox.setValue(initialStationNumber);

        colorBox = addRenderableWidget(new EditBox(font, left, top + 110, 220, 20, Component.translatable("screen.jsy.jr_station_sign.line_color")));
        colorBox.setMaxLength(8);
        colorBox.setValue(String.format("%06X", initialLineColor & 0xFFFFFF));

        addRenderableWidget(CycleButton.<LineColorPreset>builder(preset -> Component.translatable("screen.jsy.jr_station_sign." + preset.translationKey))
                .withValues(PRESETS)
                .withInitialValue(matchPreset(initialLineColor))
                .create(left, top + 140, 220, 20, Component.translatable("screen.jsy.jr_station_sign.color_preset"), (button, preset) ->
                        colorBox.setValue(String.format("%06X", preset.color()))
                ));

        variantButton = addRenderableWidget(CycleButton.<JRStationSignVariant>builder(variant -> Component.translatable("screen.jsy.jr_station_sign.variant." + variant.getSerializedName()))
                .withValues(JRStationSignVariant.values())
                .withInitialValue(initialVariant)
                .create(left, top + 170, 220, 20, Component.translatable("screen.jsy.jr_station_sign.variant"), (button, variant) -> {
                }));

        addRenderableWidget(Button.builder(Component.translatable("gui.save"), button -> saveAndClose())
                .bounds(left, top + 205, 105, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(left + 115, top + 205, 105, 20)
                .build());
        setInitialFocus(stationNameBox);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, title, width / 2, height / 2 - 82, 0xFFFFFF);
        guiGraphics.drawString(font, Component.translatable("screen.jsy.jr_station_sign.station_name"), width / 2 - 110, height / 2 - 61, 0xA0A0A0);
        guiGraphics.drawString(font, Component.translatable("screen.jsy.jr_station_sign.line_name"), width / 2 - 110, height / 2 - 31, 0xA0A0A0);
        guiGraphics.drawString(font, Component.translatable("screen.jsy.jr_station_sign.station_number"), width / 2 - 110, height / 2 - 1, 0xA0A0A0);
        guiGraphics.drawString(font, Component.translatable("screen.jsy.jr_station_sign.line_color"), width / 2 - 110, height / 2 + 29, 0xA0A0A0);
        guiGraphics.fill(width / 2 + 85, height / 2 + 53, width / 2 + 105, height / 2 + 73, 0xFF000000 | parseColor(colorBox.getValue(), initialLineColor));
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void saveAndClose() {
        onSave.accept(new SignSaveData(
                pos,
                stationNameBox.getValue(),
                lineNameBox.getValue(),
                stationNumberBox.getValue(),
                parseColor(colorBox.getValue(), initialLineColor),
                variantButton.getValue()
        ));
        onClose();
    }

    private static int parseColor(String value, int fallback) {
        String normalized = value == null ? "" : value.trim().replace("#", "").replace("0x", "").replace("0X", "");
        if (normalized.isEmpty()) {
            return fallback & 0xFFFFFF;
        }
        try {
            return Integer.parseInt(normalized, 16) & 0xFFFFFF;
        } catch (NumberFormatException ignored) {
            return fallback & 0xFFFFFF;
        }
    }

    private static LineColorPreset matchPreset(int color) {
        for (LineColorPreset preset : PRESETS) {
            if (preset.color() == (color & 0xFFFFFF)) {
                return preset;
            }
        }
        return PRESETS.get(0);
    }

    private record LineColorPreset(String translationKey, int color) {
    }
}
