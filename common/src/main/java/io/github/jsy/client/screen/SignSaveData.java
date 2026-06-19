package io.github.jsy.client.screen;

import io.github.jsy.block.JRStationSignVariant;
import net.minecraft.core.BlockPos;

/**
 * 站名牌 GUI 保存时的数据载体
 */
public record SignSaveData(
        BlockPos pos,
        String stationName,
        String lineName,
        String stationNumber,
        int lineColor,
        JRStationSignVariant variant
) {
}
