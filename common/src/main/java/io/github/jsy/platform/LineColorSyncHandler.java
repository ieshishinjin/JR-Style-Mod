package io.github.jsy.platform;

import net.minecraft.core.BlockPos;

/**
 * 线路颜色同步回调接口
 * <p>
 * 当客户端检测到 MTR 线路颜色时，由各平台实现此接口，
 * 将颜色数据通过各自的网络系统发送回服务端。
 */
@FunctionalInterface
public interface LineColorSyncHandler {

    /**
     * 同步线路颜色到服务端
     *
     * @param pos      方块位置
     * @param color    线路颜色 (ARGB)
     * @param lineId   线路 ID
     */
    void syncColor(BlockPos pos, int color, String lineId);
}
