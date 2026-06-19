package io.github.jsy.network;

import io.github.jsy.client.screen.DirectionSignScreen;
import io.github.jsy.client.screen.PlatformSignScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;

public class OpenSimpleSignS2CPacket {

    public enum SignType { PLATFORM, DIRECTION }

    private final SignType type;
    private final BlockPos pos;
    private final String initialText;

    public OpenSimpleSignS2CPacket(SignType type, BlockPos pos, String initialText) {
        this.type = type;
        this.pos = pos;
        this.initialText = initialText;
    }

    public OpenSimpleSignS2CPacket(FriendlyByteBuf buf) {
        this.type = buf.readEnum(SignType.class);
        this.pos = buf.readBlockPos();
        this.initialText = buf.readUtf(32);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeBlockPos(pos);
        buf.writeUtf(initialText, 32);
    }

    public void handle(net.minecraftforge.event.network.CustomPayloadEvent.Context ctx) {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            switch (type) {
                case PLATFORM -> mc.setScreen(new PlatformSignScreen(pos, initialText,
                        result -> {
                            ForgeNetworkHandler.getChannel().send(
                                    new SaveSimpleSignC2SPacket(SignType.PLATFORM, result.pos(), result.value()),
                                    PacketDistributor.SERVER.noArg()
                            );
                        }));
                case DIRECTION -> mc.setScreen(new DirectionSignScreen(pos, initialText,
                        result -> {
                            ForgeNetworkHandler.getChannel().send(
                                    new SaveSimpleSignC2SPacket(SignType.DIRECTION, result.pos(), result.value()),
                                    PacketDistributor.SERVER.noArg()
                            );
                        }));
            }
        });
        ctx.setPacketHandled(true);
    }
}
