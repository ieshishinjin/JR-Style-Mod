package io.github.jsy;

import io.github.jsy.block.LineColorBlockEntity;
import io.github.jsy.items.ModCreativeModTabFabric;
import io.github.jsy.network.JRStationSignNetworking;
import io.github.jsy.registry.FabricModBlockEntities;
import io.github.jsy.registry.FabricModBlocks;
import net.fabricmc.api.ModInitializer;

public class jsy implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.LOG.info("Hello Fabric world!");

        // 注册方块
        FabricModBlocks.register();

        // 注册方块实体
        FabricModBlockEntities.register();

        // 注册网络包
        JRStationSignNetworking.registerServer();

        // 注册创造模式标签页
        ModCreativeModTabFabric.register();

        // 设置线路颜色同步处理器（客户端检测到 MTR 颜色后发回服务端）
        LineColorBlockEntity.setSyncHandler((pos, color, lineId) -> {
            JRStationSignNetworking.sendLineColorUpdate(pos, color, lineId != null ? lineId : "");
            Constants.LOG.debug("Syncing line color to server: {} at {}", String.format("0x%06X", color), pos);
        });

        CommonClass.init();
    }
}
