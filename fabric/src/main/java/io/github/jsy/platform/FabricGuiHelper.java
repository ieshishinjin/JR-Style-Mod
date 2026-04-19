package io.github.jsy.platform;

import io.github.jsy.block.BlockEntityJRStationSign;
import io.github.jsy.network.JRStationSignNetworking;
import io.github.jsy.platform.services.IGuiHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class FabricGuiHelper implements IGuiHelper {

    @Override
    public void openJRStationSignScreen(ServerPlayer player, BlockPos pos) {
        if (player.level().getBlockEntity(pos) instanceof BlockEntityJRStationSign blockEntity) {
            JRStationSignNetworking.sendOpenScreenPacket(player, pos, blockEntity);
        }
    }
}
