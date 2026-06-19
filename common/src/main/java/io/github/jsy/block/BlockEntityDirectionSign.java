package io.github.jsy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityDirectionSign extends BlockEntity {

    private String directionText = "東京方面";

    public BlockEntityDirectionSign(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DIRECTION_SIGN_BLOCK_ENTITY, pos, state);
    }

    public String getDirectionText() {
        return directionText;
    }

    public void setDirectionText(String text) {
        this.directionText = text != null && !text.trim().isEmpty() ? text.trim() : "東京方面";
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
        tag.putString("DirectionText", directionText);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        directionText = tag.contains("DirectionText") ? tag.getString("DirectionText") : "東京方面";
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
