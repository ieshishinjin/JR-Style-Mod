package io.github.jsy.platform;

import io.github.jsy.Constants;
import io.github.jsy.platform.services.IMTRHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ForgeMTRHelper implements IMTRHelper {

    public ForgeMTRHelper() {
        if (MTRHelperUtil.isMTRAvailable()) {
            Constants.LOG.info("MTR mod detected (Forge)");
        } else {
            Constants.LOG.info("MTR mod not available (Forge)");
        }
    }

    @Override
    @Nullable
    public Integer getLineColor(Level level, BlockPos pos) {
        if (!level.isClientSide) return null; // 只在客户端检测
        return MTRHelperUtil.getNearbyLineColor(pos);
    }

    @Override
    @Nullable
    public String getLineId(Level level, BlockPos pos) {
        if (!level.isClientSide) return null;
        return MTRHelperUtil.getNearbyLineId(pos);
    }

    @Override
    public boolean isNearMTRFacility(Level level, BlockPos pos, int radius) {
        if (!level.isClientSide) return false;
        return MTRHelperUtil.isNearMTRFacility(pos, radius);
    }
}
