package io.github.jsy;

import io.github.jsy.block.Modblock;
import io.github.jsy.block.ModblockFabric;
import net.fabricmc.api.ModInitializer;
import io.github.jsy.items.ModitemsFabric;
import io.github.jsy.items.ModCreativeModTabFabric;

public class jsy implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        ModitemsFabric.register();
        ModCreativeModTabFabric.register();
        ModblockFabric.register();
        CommonClass.init();
    }
}
