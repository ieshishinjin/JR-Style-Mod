package io.github.jsy.platform;

import io.github.jsy.Constants;
import io.github.jsy.platform.services.IMTRHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FabricMTRHelper implements IMTRHelper {

    private final boolean mtrAvailable;

    public FabricMTRHelper() {
        boolean available;
        try {
            Class.forName("org.mtr.mod.Init");
            available = true;
            Constants.LOG.info("MTR mod detected");
        } catch (ClassNotFoundException e) {
            available = false;
            Constants.LOG.warn("MTR mod not available: {}", e.getMessage());
        }
        this.mtrAvailable = available;
    }

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
        return mtrAvailable;
    }
}
