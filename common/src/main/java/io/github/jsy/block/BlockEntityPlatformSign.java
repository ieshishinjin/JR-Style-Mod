package io.github.jsy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityPlatformSign extends BlockEntity {

    private String platformNumber = "1";

    public BlockEntityPlatformSign(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLATFORM_SIGN_BLOCK_ENTITY, pos, state);
    }

    public String getPlatformNumber() {
        return platformNumber;
    }

    public void setPlatformNumber(String number) {
        this.platformNumber = number != null && !number.trim().isEmpty() ? number.trim() : "1";
        markUpdated();
    }

    public void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("PlatformNumber", platformNumber);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        platformNumber = tag.contains("PlatformNumber") ? tag.getString("PlatformNumber") : "1";
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
