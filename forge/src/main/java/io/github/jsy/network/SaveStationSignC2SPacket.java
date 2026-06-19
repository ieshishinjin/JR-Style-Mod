package io.github.jsy.network;

import io.github.jsy.block.BlockEntityJRStationSign;
import io.github.jsy.block.BlockJRStationSign;
import io.github.jsy.block.JRStationSignVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 客户端→服务端：保存站名牌数据
 */
public class SaveStationSignC2SPacket {

    private final BlockPos pos;
    private final String stationName;
    private final String lineName;
    private final String stationNumber;
    private final int lineColor;
    private final JRStationSignVariant variant;

    public SaveStationSignC2SPacket(BlockPos pos, String stationName, String lineName,
                                    String stationNumber, int lineColor, JRStationSignVariant variant) {
        this.pos = pos;
        this.stationName = stationName;
        this.lineName = lineName;
        this.stationNumber = stationNumber;
        this.lineColor = lineColor;
        this.variant = variant;
    }

    public SaveStationSignC2SPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.stationName = buf.readUtf(32);
        this.lineName = buf.readUtf(16);
        this.stationNumber = buf.readUtf(8);
        this.lineColor = buf.readInt();
        this.variant = JRStationSignVariant.fromName(buf.readUtf(16));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(stationName, 32);
        buf.writeUtf(lineName, 16);
        buf.writeUtf(stationNumber, 8);
        buf.writeInt(lineColor);
        buf.writeUtf(variant.getSerializedName(), 16);
    }

    public void handle(net.minecraftforge.event.network.CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player == null) return;

        if (player.level().getBlockEntity(pos) instanceof BlockEntityJRStationSign blockEntity
                && player.distanceToSqr(pos.getCenter()) <= 64) {

            BlockState currentState = player.level().getBlockState(pos);
            BlockState updatedState = currentState;

            // 如果样式（悬挂/立柱）变了，更新方块状态
            if (currentState.getBlock() instanceof BlockJRStationSign
                    && currentState.getValue(BlockJRStationSign.VARIANT) != variant) {
                BlockState candidateState = currentState.setValue(BlockJRStationSign.VARIANT, variant);
                if (candidateState.canSurvive(player.level(), pos)) {
                    player.level().setBlock(pos, candidateState, 3);
                    updatedState = candidateState;
                }
            }

            blockEntity.setData(stationName, lineName, stationNumber, lineColor, variant);
            blockEntity.syncVariantFromState(updatedState);
            blockEntity.markUpdated();
        }

        ctx.setPacketHandled(true);
    }
}
