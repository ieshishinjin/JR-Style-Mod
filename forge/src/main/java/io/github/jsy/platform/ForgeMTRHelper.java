package io.github.jsy.platform;

import io.github.jsy.platform.services.IMTRHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ForgeMTRHelper implements IMTRHelper {

    @Override
    @Nullable
    public Integer getLineColor(Level level, BlockPos pos) {
        return null;
    }

    @Override
    @Nullable
    public String getLineId(Level level, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isNearMTRFacility(Level level, BlockPos pos, int radius) {
        return false;
    }
}
