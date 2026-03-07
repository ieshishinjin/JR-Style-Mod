package io.github.jsy.platform;

import io.github.jsy.Constants;
import io.github.jsy.platform.services.IMTRHelper;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.mtr.mod.data.RailwayData;
import org.mtr.mod.data.Route;
import org.mtr.mod.data.Station;

/**
 * Fabric 端 MTR 辅助实现
 * 使用正常 API 调用访问 MTR 数据
 */
public class FabricMTRHelper implements IMTRHelper {

    private boolean mtrAvailable = false; 

    public FabricMTRHelper() {
        try {
            // 检查 MTR 类是否存在
            Class.forName("mtr.data.RailwayData");
            mtrAvailable = true;
            Constants.LOG.info("MTR API detected and loaded successfully");
        } catch (ClassNotFoundException e) {
            Constants.LOG.warn("MTR API not available: {}", e.getMessage());
            mtrAvailable = false;
        }
    }

    @Override
    @Nullable
    public Integer getLineColor(Level level, BlockPos pos) {
        if (!mtrAvailable) {
            return null;
        }

        try {
            RailwayData railwayData = RailwayData.getInstance(level);
            if (railwayData == null) {
                return null;
            }

            Station nearestStation = findNearestStation(railwayData.stations, pos);
            if (nearestStation != null && !nearestStation.routeIds.isEmpty()) {
                long routeId = nearestStation.routeIds.get(0);
                Route route = railwayData.dataCache.routeIdMap.get(routeId);
                if (route != null) {
                    int color = route.color;
                    // MTR 颜色是 RGB 格式，转换为 ARGB
                    return 0xFF000000 | color;
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Error getting MTR line color", e);
        }

        return null;
    }

    @Override
    @Nullable
    public String getLineId(Level level, BlockPos pos) {
        if (!mtrAvailable) {
            return null;
        }

        try {
            RailwayData railwayData = RailwayData.getInstance(level);
            if (railwayData == null) {
                return null;
            }

            Station nearestStation = findNearestStation(railwayData.stations, pos);
            if (nearestStation != null && !nearestStation.routeIds.isEmpty()) {
                long routeId = nearestStation.routeIds.get(0);
                Route route = railwayData.dataCache.routeIdMap.get(routeId);
                if (route != null) {
                    return route.name;
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Error getting MTR line ID", e);
        }

        return null;
    }

    @Override
    public boolean isNearMTRFacility(Level level, BlockPos pos, int radius) {
        if (!mtrAvailable) {
            return false;
        }

        try {
            RailwayData railwayData = RailwayData.getInstance(level);
            if (railwayData == null) {
                return false;
            }

            for (Station station : railwayData.stations) {
                double distance = Math.sqrt(
                        Math.pow(station.getMidPos().x - pos.getX(), 2) +
                        Math.pow(station.getMidPos().y - pos.getY(), 2) +
                        Math.pow(station.getMidPos().z - pos.getZ(), 2)
                );

                if (distance <= radius) {
                    return true;
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Error checking MTR facility proximity", e);
        }

        return false;
    }

    @Nullable
    private Station findNearestStation(List<Station> stations, BlockPos pos) {
        Station nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Station station : stations) {
            double distance = Math.sqrt(
                    Math.pow(station.getMidPos().x - pos.getX(), 2) +
                    Math.pow(station.getMidPos().y - pos.getY(), 2) +
                    Math.pow(station.getMidPos().z - pos.getZ(), 2)
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearest = station;
            }
        }

        return nearest;
    }
}
