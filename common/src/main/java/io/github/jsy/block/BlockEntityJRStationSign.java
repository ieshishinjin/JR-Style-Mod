package io.github.jsy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityJRStationSign extends BlockEntity {

    private static final int MAX_STATION_NAME_LENGTH = 32;
    private static final int MAX_LINE_NAME_LENGTH = 16;
    private static final int MAX_STATION_NUMBER_LENGTH = 8;

    private String stationName = "駅名";
    private int lineColor = 0x333333;
    private String lineName = "JR線";
    private String stationNumber = "";
    private String variant = JRStationSignVariant.HANGING.getSerializedName();

    public BlockEntityJRStationSign(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JR_STATION_SIGN_BLOCK_ENTITY, pos, state);
        syncVariantFromState(state);
    }

    public String getStationName() {
        return stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public int getLineColor() {
        return lineColor;
    }

    public JRStationSignVariant getVariant() {
        return JRStationSignVariant.fromName(variant);
    }

    public void setData(String stationName, String lineName, String stationNumber, int lineColor, JRStationSignVariant variant) {
        this.stationName = trim(stationName, MAX_STATION_NAME_LENGTH, "駅名");
        this.lineName = trim(lineName, MAX_LINE_NAME_LENGTH, "JR線");
        this.stationNumber = trim(stationNumber, MAX_STATION_NUMBER_LENGTH, "");
        this.lineColor = lineColor & 0xFFFFFF;
        this.variant = variant.getSerializedName();
        markUpdated();
    }

    public void syncVariantFromState(BlockState state) {
        if (state.hasProperty(BlockJRStationSign.VARIANT)) {
            this.variant = state.getValue(BlockJRStationSign.VARIANT).getSerializedName();
        }
    }

    public void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private static String trim(String value, int maxLength, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return fallback;
        }
        return trimmed.length() > maxLength ? trimmed.substring(0, maxLength) : trimmed;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("station_name", stationName);
        tag.putInt("line_color", lineColor);
        tag.putString("line_name", lineName);
        tag.putString("station_number", stationNumber);
        tag.putString("variant", variant);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        stationName = trim(tag.getString("station_name"), MAX_STATION_NAME_LENGTH, "駅名");
        lineColor = tag.contains("line_color") ? (tag.getInt("line_color") & 0xFFFFFF) : 0x333333;
        lineName = trim(tag.getString("line_name"), MAX_LINE_NAME_LENGTH, "JR線");
        stationNumber = trim(tag.getString("station_number"), MAX_STATION_NUMBER_LENGTH, "");
        variant = tag.contains("variant") ? JRStationSignVariant.fromName(tag.getString("variant")).getSerializedName() : variant;
        syncVariantFromState(getBlockState());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
