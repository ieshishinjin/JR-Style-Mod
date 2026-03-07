package io.github.jsy;

import io.github.jsy.items.ModCreativeModTabFabric;
import io.github.jsy.registry.FabricModBlockEntities;
import io.github.jsy.registry.FabricModBlocks;
import net.fabricmc.api.ModInitializer;

public class jsy implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");

        // 注册方块
        FabricModBlocks.register();

        // 注册方块实体
        FabricModBlockEntities.register();

        // 注册创造模式标签页
        ModCreativeModTabFabric.register();

        CommonClass.init();
    }
}
