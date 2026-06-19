package io.github.jsy.block;

import io.github.jsy.Constants;
import io.github.jsy.platform.LineColorSyncHandler;
import io.github.jsy.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 存储线路颜色数据的方块实体
 */
public class LineColorBlockEntity extends BlockEntity {

    // 线路颜色 (ARGB格式，默认蓝色方便调试)
    private int lineColor = 0xFF2196F3;
    // 线路ID
    private String lineId = "";
    // 是否需要重新检测颜色
    private boolean needsDetection = true;

    // 各平台设置的响应式颜色同步处理器
    private static LineColorSyncHandler syncHandler;

    /**
     * 设置颜色同步处理器（由各平台初始化时调用）
     */
    public static void setSyncHandler(LineColorSyncHandler handler) {
        syncHandler = handler;
    }

    public LineColorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LINE_COLOR_BLOCK_ENTITY, pos, state);
    }

    public LineColorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * 获取线路颜色
     */
    public int getLineColor() {
        return lineColor;
    }

    /**
     * 设置线路颜色
     */
    public void setLineColor(int color) {
        this.lineColor = color;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    /**
     * 获取线路ID
     */
    public String getLineId() {
        return lineId;
    }

    /**
     * 设置线路ID
     */
    public void setLineId(String lineId) {
        this.lineId = lineId;
        setChanged();
    }

    /**
     * 检测线路颜色
     * 使用 MTR API 获取附近线路的颜色
     */
    public void detectLineColor(Level level, BlockPos pos) {
        Constants.LOG.debug("Detecting line color at {}", pos);

        Integer color = Services.MTR.getLineColor(level, pos);
        if (color != null) {
            setLineColor(color | 0xFF000000); // 确保ARGB
            Constants.LOG.info("Detected line color {} at {}", String.format("0x%08X", color), pos);
        } else {
            Constants.LOG.debug("No MTR line found near {}, keeping current color", pos);
        }

        String detectedLineId = Services.MTR.getLineId(level, pos);
        if (detectedLineId != null) {
            setLineId(detectedLineId);
        }

        setNeedsDetection(false);

        // 如果在客户端检测到颜色，通过平台网络包同步到服务端
        if (level != null && level.isClientSide && color != null && syncHandler != null) {
            syncHandler.syncColor(pos, color, detectedLineId != null ? detectedLineId : "");
        }
    }

    /**
     * 标记需要重新检测
     */
    public void markForDetection() {
        this.needsDetection = true;
        setChanged();
    }

    public boolean needsDetection() {
        return needsDetection;
    }

    public void setNeedsDetection(boolean needs) {
        this.needsDetection = needs;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("LineColor", lineColor);
        tag.putString("LineId", lineId);
        tag.putBoolean("NeedsDetection", needsDetection);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("LineColor")) {
            this.lineColor = tag.getInt("LineColor");
        } // else keep current default (blue)
        if (tag.contains("LineId")) {
            this.lineId = tag.getString("LineId");
        }
        if (tag.contains("NeedsDetection")) {
            this.needsDetection = tag.getBoolean("NeedsDetection");
        }

        // 客户端加载时（接收数据包或加载区块），尝试检测 MTR 线路颜色
        if (level != null && level.isClientSide && needsDetection) {
            detectLineColor(level, worldPosition);
        }
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
