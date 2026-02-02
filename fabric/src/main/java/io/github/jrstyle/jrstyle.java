package io.github.jrstyle;

import net.fabricmc.api.ModInitializer;
import io.github.jrstyle.items.ModitemsFabric;
import io.github.jrstyle.items.ModCreativeModTabFabric;

public class jrstyle implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        ModitemsFabric.register();
        ModCreativeModTabFabric.register();
        CommonClass.init();
    }
}
