package io.github.jsy.network;

import io.github.jsy.block.JRStationSignVariant;
import io.github.jsy.client.screen.JRStationSignScreen;
import io.github.jsy.client.screen.SignSaveData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;

/**
 * 服务端→客户端：通知客户端打开站名牌编辑界面
 */
public class OpenScreenS2CPacket {

    private final BlockPos pos;
    private final String stationName;
    private final String lineName;
    private final String stationNumber;
    private final int lineColor;
    private final JRStationSignVariant variant;

    public OpenScreenS2CPacket(BlockPos pos, String stationName, String lineName,
                               String stationNumber, int lineColor, JRStationSignVariant variant) {
        this.pos = pos;
        this.stationName = stationName;
        this.lineName = lineName;
        this.stationNumber = stationNumber;
        this.lineColor = lineColor;
        this.variant = variant;
    }

    public OpenScreenS2CPacket(FriendlyByteBuf buf) {
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
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new JRStationSignScreen(
                    pos, stationName, lineName, stationNumber, lineColor, variant,
                    data -> sendSaveToServer(data)
            ));
        });
        ctx.setPacketHandled(true);
    }

    private static void sendSaveToServer(SignSaveData data) {
        ForgeNetworkHandler.getChannel().send(
                new SaveStationSignC2SPacket(
                        data.pos(),
                        data.stationName(),
                        data.lineName(),
                        data.stationNumber(),
                        data.lineColor(),
                        data.variant()
                ),
                PacketDistributor.SERVER.noArg()
        );
    }
}
