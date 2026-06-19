package io.github.jsy.network;

import io.github.jsy.block.LineColorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * 客户端→服务端：客户端检测到 MTR 线路颜色后更新到服务端
 */
public class UpdateLineColorC2SPacket {

    private final BlockPos pos;
    private final int lineColor;
    private final String lineId;

    public UpdateLineColorC2SPacket(BlockPos pos, int lineColor, String lineId) {
        this.pos = pos;
        this.lineColor = lineColor;
        this.lineId = lineId;
    }

    public UpdateLineColorC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.lineColor = buf.readInt();
        this.lineId = buf.readUtf(64);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(lineColor);
        buf.writeUtf(lineId, 64);
    }

    public void handle(net.minecraftforge.event.network.CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player == null) return;

        if (player.level().getBlockEntity(pos) instanceof LineColorBlockEntity blockEntity
                && player.distanceToSqr(pos.getCenter()) <= 256) {
            blockEntity.setLineColor(lineColor);
            blockEntity.setLineId(lineId);
            blockEntity.setNeedsDetection(false);
        }

        ctx.setPacketHandled(true);
    }
}
