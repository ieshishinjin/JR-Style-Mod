package io.github.jsy.network;

import io.github.jsy.Constants;
import io.github.jsy.block.*;
import io.github.jsy.client.screen.DirectionSignScreen;
import io.github.jsy.client.screen.JRStationSignScreen;
import io.github.jsy.client.screen.PlatformSignScreen;
import io.github.jsy.client.screen.SignSaveData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class JRStationSignNetworking {

    public static final ResourceLocation OPEN_SCREEN_PACKET = new ResourceLocation(Constants.MOD_ID, "jr_station_sign/open");
    public static final ResourceLocation SAVE_PACKET = new ResourceLocation(Constants.MOD_ID, "jr_station_sign/save");
    public static final ResourceLocation UPDATE_LINE_COLOR_PACKET = new ResourceLocation(Constants.MOD_ID, "line_color/update");
    public static final ResourceLocation OPEN_SIMPLE_SIGN_PACKET = new ResourceLocation(Constants.MOD_ID, "simple_sign/open");
    public static final ResourceLocation SAVE_SIMPLE_SIGN_PACKET = new ResourceLocation(Constants.MOD_ID, "simple_sign/save");

    public enum SimpleSignType { PLATFORM, DIRECTION }

    private JRStationSignNetworking() {}

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

        ServerPlayNetworking.registerGlobalReceiver(UPDATE_LINE_COLOR_PACKET, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int lineColor = buf.readInt();
            String lineId = buf.readUtf(64);
            server.execute(() -> {
                if (player.level().getBlockEntity(pos) instanceof io.github.jsy.block.LineColorBlockEntity blockEntity
                        && player.distanceToSqr(pos.getCenter()) <= 256) {
                    blockEntity.setLineColor(lineColor);
                    blockEntity.setLineId(lineId);
                    blockEntity.setNeedsDetection(false);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(SAVE_SIMPLE_SIGN_PACKET, (server, player, handler, buf, responseSender) -> {
            SimpleSignType type = buf.readEnum(SimpleSignType.class);
            BlockPos pos = buf.readBlockPos();
            String text = buf.readUtf(32);
            server.execute(() -> {
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be == null || player.distanceToSqr(pos.getCenter()) > 64) return;
                switch (type) {
                    case PLATFORM -> {
                        if (be instanceof BlockEntityPlatformSign sign) sign.setPlatformNumber(text);
                    }
                    case DIRECTION -> {
                        if (be instanceof BlockEntityDirectionSign sign) sign.setDirectionText(text);
                    }
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
            client.execute(() -> client.setScreen(new JRStationSignScreen(
                    pos, stationName, lineName, stationNumber, lineColor, variant,
                    data -> sendSavePacket(data)
            )));
        });

        ClientPlayNetworking.registerGlobalReceiver(OPEN_SIMPLE_SIGN_PACKET, (client, handler, buf, responseSender) -> {
            SimpleSignType type = buf.readEnum(SimpleSignType.class);
            BlockPos pos = buf.readBlockPos();
            String initialText = buf.readUtf(32);
            client.execute(() -> {
                switch (type) {
                    case PLATFORM -> client.setScreen(new PlatformSignScreen(pos, initialText,
                            result -> sendSaveSimpleSign(SimpleSignType.PLATFORM, result.pos(), result.value())));
                    case DIRECTION -> client.setScreen(new DirectionSignScreen(pos, initialText,
                            result -> sendSaveSimpleSign(SimpleSignType.DIRECTION, result.pos(), result.value())));
                }
            });
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

    public static void sendSavePacket(SignSaveData data) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(data.pos());
        buf.writeUtf(data.stationName(), 32);
        buf.writeUtf(data.lineName(), 16);
        buf.writeUtf(data.stationNumber(), 8);
        buf.writeInt(data.lineColor() & 0xFFFFFF);
        buf.writeUtf(data.variant().getSerializedName(), 16);
        ClientPlayNetworking.send(SAVE_PACKET, buf);
    }

    public static void sendLineColorUpdate(BlockPos pos, int lineColor, String lineId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeInt(lineColor);
        buf.writeUtf(lineId, 64);
        ClientPlayNetworking.send(UPDATE_LINE_COLOR_PACKET, buf);
    }

    public static void sendOpenSimpleSignPacket(net.minecraft.server.level.ServerPlayer player, BlockPos pos, SimpleSignType type, String initialText) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(type);
        buf.writeBlockPos(pos);
        buf.writeUtf(initialText, 32);
        ServerPlayNetworking.send(player, OPEN_SIMPLE_SIGN_PACKET, buf);
    }

    private static void sendSaveSimpleSign(SimpleSignType type, BlockPos pos, String text) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(type);
        buf.writeBlockPos(pos);
        buf.writeUtf(text, 32);
        ClientPlayNetworking.send(SAVE_SIMPLE_SIGN_PACKET, buf);
    }
}
