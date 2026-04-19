package io.github.jsy.network;

import io.github.jsy.Constants;
import io.github.jsy.block.BlockEntityJRStationSign;
import io.github.jsy.block.BlockJRStationSign;
import io.github.jsy.block.JRStationSignVariant;
import io.github.jsy.client.screen.JRStationSignScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public final class JRStationSignNetworking {

    public static final ResourceLocation OPEN_SCREEN_PACKET = new ResourceLocation(Constants.MOD_ID, "jr_station_sign/open");
    public static final ResourceLocation SAVE_PACKET = new ResourceLocation(Constants.MOD_ID, "jr_station_sign/save");

    private JRStationSignNetworking() {
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(SAVE_PACKET, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            String stationName = buf.readUtf(32);
            String lineName = buf.readUtf(16);
            String stationNumber = buf.readUtf(8);
            int lineColor = buf.readInt();
            JRStationSignVariant variant = JRStationSignVariant.fromName(buf.readUtf(16));

            server.execute(() -> {
                if (player.level().getBlockEntity(pos) instanceof BlockEntityJRStationSign blockEntity && player.distanceToSqr(pos.getCenter()) <= 64) {
                    BlockState currentState = player.level().getBlockState(pos);
                    BlockState updatedState = currentState;
                    if (currentState.getBlock() instanceof BlockJRStationSign && currentState.getValue(BlockJRStationSign.VARIANT) != variant) {
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
            });
        });
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN_PACKET, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            String stationName = buf.readUtf(32);
            String lineName = buf.readUtf(16);
            String stationNumber = buf.readUtf(8);
            int lineColor = buf.readInt();
            JRStationSignVariant variant = JRStationSignVariant.fromName(buf.readUtf(16));
            client.execute(() -> client.setScreen(new JRStationSignScreen(pos, stationName, lineName, stationNumber, lineColor, variant)));
        });
    }

    public static void sendOpenScreenPacket(net.minecraft.server.level.ServerPlayer player, BlockPos pos, BlockEntityJRStationSign blockEntity) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeUtf(blockEntity.getStationName(), 32);
        buf.writeUtf(blockEntity.getLineName(), 16);
        buf.writeUtf(blockEntity.getStationNumber(), 8);
        buf.writeInt(blockEntity.getLineColor());
        buf.writeUtf(blockEntity.getVariant().getSerializedName(), 16);
        ServerPlayNetworking.send(player, OPEN_SCREEN_PACKET, buf);
    }

    public static void sendSavePacket(BlockPos pos, String stationName, String lineName, String stationNumber, int lineColor, JRStationSignVariant variant) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeUtf(stationName, 32);
        buf.writeUtf(lineName, 16);
        buf.writeUtf(stationNumber, 8);
        buf.writeInt(lineColor & 0xFFFFFF);
        buf.writeUtf(variant.getSerializedName(), 16);
        ClientPlayNetworking.send(SAVE_PACKET, buf);
    }
}
