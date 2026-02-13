package io.github.jsy.block;

import io.github.jsy.block.Modblock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static io.github.jsy.Constants.MOD_ID;

public class ModblockForge {

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "jsy");

    public static final RegistryObject<Block> Kingdom_BLOCK =
            BLOCKS.register("kingdom", Modblock::new);

    public static final RegistryObject<Item> Kingdom =
            ITEMS.register("kingdom",
                    () -> new BlockItem(Kingdom_BLOCK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}