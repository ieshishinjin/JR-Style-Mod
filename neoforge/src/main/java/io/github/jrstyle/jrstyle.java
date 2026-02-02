package io.github.jrstyle;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import io.github.jrstyle.items.ModitemsNeoforge;
import io.github.jrstyle.items.ModCreativeModTabNeoforge;

@Mod(Constants.MOD_ID)
public class jrstyle {

    public jrstyle(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        ModitemsNeoforge.register(eventBus);
        ModCreativeModTabNeoforge.register(eventBus);
        CommonClass.init();

    }
}