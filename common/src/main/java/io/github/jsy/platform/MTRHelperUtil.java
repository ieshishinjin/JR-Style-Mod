package io.github.jsy.platform;

import io.github.jsy.Constants;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * MTR API 辅助工具类
 * <p>
 * 使用反射访问 MTR 的客户端数据（MinecraftClientData），
 * 因为 MTR 只在运行时可用，编译期不需要依赖。
 */
public class MTRHelperUtil {

    private static Boolean mtrAvailable = null;

    /**
     * 检查 MTR 模组是否已加载
     */
    public static boolean isMTRAvailable() {
        if (mtrAvailable == null) {
            try {
                Class.forName("org.mtr.mod.Init");
                mtrAvailable = true;
                Constants.LOG.info("MTR mod detected");
            } catch (ClassNotFoundException e) {
                mtrAvailable = false;
            }
        }
        return mtrAvailable;
    }

    /**
     * 获取 MinecraftClientData 实例
     */
    @Nullable
    private static Object getClientData() {
        try {
            Class<?> clazz = Class.forName("org.mtr.mod.client.MinecraftClientData");
            return clazz.getMethod("getInstance").invoke(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 platforms 集合
     */
    @Nullable
    private static Object getPlatforms(Object clientData) {
        if (clientData == null) return null;
        try {
            return clientData.getClass().getField("platforms").get(clientData);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将 Minecraft BlockPos 转为 MTR Position
     */
    @Nullable
    private static Object toMTRPosition(BlockPos pos) {
        try {
            Class<?> positionClass = Class.forName("org.mtr.core.data.Position");
            return positionClass.getConstructor(long.class, long.class, long.class)
                    .newInstance((long) pos.getX(), (long) pos.getY(), (long) pos.getZ());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查 MTR Platform 是否包含指定位置
     */
    private static boolean platformContainsPos(Object platform, Object mtrPos) {
        try {
            Class<?> savedRailBaseClass = Class.forName("org.mtr.core.data.SavedRailBase");
            java.lang.reflect.Method containsPos = savedRailBaseClass.getMethod("containsPos",
                    Class.forName("org.mtr.core.data.Position"));
            return (boolean) containsPos.invoke(platform, mtrPos);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Platform 获取第一个线路颜色
     */
    @Nullable
    private static Integer getFirstRouteColor(Object platform) {
        try {
            java.lang.reflect.Field routeColorsField = Class.forName("org.mtr.core.data.Platform")
                    .getField("routeColors");
            Object routeColors = routeColorsField.get(platform);
            if (routeColors == null) return null;

            Class<?> intSetClass = Class.forName("org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet");
            boolean empty = (boolean) intSetClass.getMethod("isEmpty").invoke(routeColors);
            if (!empty) {
                return (int) intSetClass.getMethod("firstInt").invoke(routeColors);
            }
            // 如果没有 routeColors，从 routes 获取第一个线路的颜色
            java.lang.reflect.Field routesField = Class.forName("org.mtr.core.data.Platform")
                    .getField("routes");
            Object routes = routesField.get(platform);
            if (routes instanceof Iterable<?> routeIter) {
                for (Object route : routeIter) {
                    if (route != null) {
                        try {
                            return (int) route.getClass().getMethod("getColor").invoke(route);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            Constants.LOG.debug("Could not get route color: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 遍历所有 MTR Platform，查找包含指定位置的平台并返回线路颜色
     */
    @Nullable
    public static Integer getNearbyLineColor(BlockPos pos) {
        if (!isMTRAvailable()) return null;

        Object clientData = getClientData();
        Object platforms = getPlatforms(clientData);
        if (platforms == null) return null;

        if (!(platforms instanceof Iterable<?> iterable)) return null;

        Object mtrPos = toMTRPosition(pos);
        if (mtrPos == null) return null;

        for (Object platform : iterable) {
            if (platform == null) continue;
            if (platformContainsPos(platform, mtrPos)) {
                Integer color = getFirstRouteColor(platform);
                if (color != null) {
                    Constants.LOG.debug("Found MTR line color 0x{} at {}",
                            String.format("%06X", color & 0xFFFFFF), pos);
                    return color;
                }
            }
        }

        return null;
    }

    /**
     * 查找指定位置附近的线路 ID
     */
    @Nullable
    public static String getNearbyLineId(BlockPos pos) {
        if (!isMTRAvailable()) return null;

        Object clientData = getClientData();
        Object platforms = getPlatforms(clientData);
        if (platforms == null) return null;

        if (!(platforms instanceof Iterable<?> iterable)) return null;

        Object mtrPos = toMTRPosition(pos);
        if (mtrPos == null) return null;

        for (Object platform : iterable) {
            if (platform == null) continue;
            if (platformContainsPos(platform, mtrPos)) {
                try {
                    java.lang.reflect.Field routesField = Class.forName("org.mtr.core.data.Platform")
                            .getField("routes");
                    Object routes = routesField.get(platform);
                    if (routes instanceof Iterable<?> routeIter) {
                        for (Object route : routeIter) {
                            if (route != null) {
                                long id = (long) route.getClass().getMethod("getId").invoke(route);
                                return String.valueOf(id);
                            }
                        }
                    }
                } catch (Exception e) {
                    Constants.LOG.debug("Could not get route ID: {}", e.getMessage());
                }
                return "unknown";
            }
        }

        return null;
    }

    /**
     * 检查附近是否有 MTR 设施
     */
    public static boolean isNearMTRFacility(BlockPos pos, int radius) {
        if (!isMTRAvailable()) return false;

        Object clientData = getClientData();
        Object platforms = getPlatforms(clientData);
        if (platforms == null) return false;

        if (!(platforms instanceof Iterable<?> iterable)) return false;

        Object mtrPos = toMTRPosition(pos);
        if (mtrPos == null) return false;

        try {
            Class<?> savedRailBaseClass = Class.forName("org.mtr.core.data.SavedRailBase");
            java.lang.reflect.Method closeTo = savedRailBaseClass.getMethod("closeTo",
                    Class.forName("org.mtr.core.data.Position"), double.class);

            for (Object platform : iterable) {
                if (platform == null) continue;
                boolean close = (boolean) closeTo.invoke(platform, mtrPos, (double) radius);
                if (close) return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
