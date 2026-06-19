package io.github.jsy.network;

import io.github.jsy.block.BlockEntityDirectionSign;
import io.github.jsy.block.BlockEntityPlatformSign;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SaveSimpleSignC2SPacket {

    private final OpenSimpleSignS2CPacket.SignType type;
    private final BlockPos pos;
    private final String text;

    public SaveSimpleSignC2SPacket(OpenSimpleSignS2CPacket.SignType type, BlockPos pos, String text) {
        this.type = type;
        this.pos = pos;
        this.text = text;
    }

    public SaveSimpleSignC2SPacket(FriendlyByteBuf buf) {
        this.type = buf.readEnum(OpenSimpleSignS2CPacket.SignType.class);
        this.pos = buf.readBlockPos();
        this.text = buf.readUtf(32);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeBlockPos(pos);
        buf.writeUtf(text, 32);
    }

    public void handle(net.minecraftforge.event.network.CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player == null) return;

        BlockEntity be = player.level().getBlockEntity(pos);
        if (be == null || player.distanceToSqr(pos.getCenter()) > 64) return;

        switch (type) {
            case PLATFORM -> {
                if (be instanceof BlockEntityPlatformSign sign) {
                    sign.setPlatformNumber(text);
                }
            }
            case DIRECTION -> {
                if (be instanceof BlockEntityDirectionSign sign) {
                    sign.setDirectionText(text);
                }
            }
        }

        ctx.setPacketHandled(true);
    }
}
