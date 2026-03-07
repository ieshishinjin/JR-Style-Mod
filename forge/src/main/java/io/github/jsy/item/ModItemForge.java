package io.github.jsy.item;

import io.github.jsy.Constants;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemForge {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final RegistryObject<Item> MAGIC_POWDER =
            ITEMS.register("magic_powder",
                    () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGIC_SWORD =
            ITEMS.register("magic_sword", MagicSwordItem::new);
}

//2026.3.7