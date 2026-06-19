package io.github.jsy.platform;

import io.github.jsy.block.BlockEntityDirectionSign;
import io.github.jsy.block.BlockEntityJRStationSign;
import io.github.jsy.block.BlockEntityPlatformSign;
import io.github.jsy.network.ForgeNetworkHandler;
import io.github.jsy.network.OpenScreenS2CPacket;
import io.github.jsy.network.OpenSimpleSignS2CPacket;
import io.github.jsy.platform.services.IGuiHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ForgeGuiHelper implements IGuiHelper {

    @Override
    public void openJRStationSignScreen(ServerPlayer player, BlockPos pos) {
        if (player.level().getBlockEntity(pos) instanceof BlockEntityJRStationSign blockEntity) {
            ForgeNetworkHandler.getChannel().send(
                    new OpenScreenS2CPacket(
                            pos,
                            blockEntity.getStationName(),
                            blockEntity.getLineName(),
                            blockEntity.getStationNumber(),
                            blockEntity.getLineColor(),
                            blockEntity.getVariant()
                    ),
                    PacketDistributor.PLAYER.with(player)
            );
        }
    }

    @Override
    public void openPlatformSignScreen(ServerPlayer player, BlockPos pos) {
        if (player.level().getBlockEntity(pos) instanceof BlockEntityPlatformSign blockEntity) {
            ForgeNetworkHandler.getChannel().send(
                    new OpenSimpleSignS2CPacket(
                            OpenSimpleSignS2CPacket.SignType.PLATFORM,
                            pos,
                            blockEntity.getPlatformNumber()
                    ),
                    PacketDistributor.PLAYER.with(player)
            );
        }
    }

    @Override
    public void openDirectionSignScreen(ServerPlayer player, BlockPos pos) {
        if (player.level().getBlockEntity(pos) instanceof BlockEntityDirectionSign blockEntity) {
            ForgeNetworkHandler.getChannel().send(
                    new OpenSimpleSignS2CPacket(
                            OpenSimpleSignS2CPacket.SignType.DIRECTION,
                            pos,
                            blockEntity.getDirectionText()
                    ),
                    PacketDistributor.PLAYER.with(player)
            );
        }
    }
}
