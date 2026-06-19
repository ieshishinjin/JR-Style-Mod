package io.github.jsy.network;

import io.github.jsy.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ForgeNetworkHandler {

    private static final int PROTOCOL_VERSION = 1;
    private static final SimpleChannel CHANNEL = ChannelBuilder
            .named(new ResourceLocation(Constants.MOD_ID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel();

    private static int packetId = 0;

    public static void register() {
        // S2C: Open station sign screen
        CHANNEL.messageBuilder(OpenScreenS2CPacket.class, packetId++)
                .encoder(OpenScreenS2CPacket::encode)
                .decoder(OpenScreenS2CPacket::new)
                .consumerMainThread(OpenScreenS2CPacket::handle)
                .add();

        // C2S: Save station sign data
        CHANNEL.messageBuilder(SaveStationSignC2SPacket.class, packetId++)
                .encoder(SaveStationSignC2SPacket::encode)
                .decoder(SaveStationSignC2SPacket::new)
                .consumerMainThread(SaveStationSignC2SPacket::handle)
                .add();

        // C2S: Update line color from MTR detection
        CHANNEL.messageBuilder(UpdateLineColorC2SPacket.class, packetId++)
                .encoder(UpdateLineColorC2SPacket::encode)
                .decoder(UpdateLineColorC2SPacket::new)
                .consumerMainThread(UpdateLineColorC2SPacket::handle)
                .add();

        // S2C: Open simple sign editor (platform / direction)
        CHANNEL.messageBuilder(OpenSimpleSignS2CPacket.class, packetId++)
                .encoder(OpenSimpleSignS2CPacket::encode)
                .decoder(OpenSimpleSignS2CPacket::new)
                .consumerMainThread(OpenSimpleSignS2CPacket::handle)
                .add();

        // C2S: Save simple sign data
        CHANNEL.messageBuilder(SaveSimpleSignC2SPacket.class, packetId++)
                .encoder(SaveSimpleSignC2SPacket::encode)
                .decoder(SaveSimpleSignC2SPacket::new)
                .consumerMainThread(SaveSimpleSignC2SPacket::handle)
                .add();
    }

    public static SimpleChannel getChannel() {
        return CHANNEL;
    }
}
