package io.github.jsy.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IMTRHelper {

    @Nullable
    Integer getLineColor(Level level, BlockPos pos);

    @Nullable
    String getLineId(Level level, BlockPos pos);

    boolean isNearMTRFacility(Level level, BlockPos pos, int radius);
}
