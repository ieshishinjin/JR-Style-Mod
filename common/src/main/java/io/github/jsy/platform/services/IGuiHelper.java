package io.github.jsy.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public interface IGuiHelper {

    void openJRStationSignScreen(ServerPlayer player, BlockPos pos);

    void openPlatformSignScreen(ServerPlayer player, BlockPos pos);

    void openDirectionSignScreen(ServerPlayer player, BlockPos pos);
}
