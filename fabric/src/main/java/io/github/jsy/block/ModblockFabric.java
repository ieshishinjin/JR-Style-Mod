package io.github.jsy.block;

import io.github.jsy.block.Modblock;
import io.github.jsy.item.Moditems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.intellij.lang.annotations.Identifier;

public class ModblockFabric {
    //创建方块
    public static final Block Kingdom = new Modblock();
    public static final Item Kingdom_ITEM = new BlockItem(Kingdom,  new FabricItemSettings());

    // 注册方块
    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK,
                new ResourceLocation("jsy", "kingdom"),
                Kingdom
        );
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation("jsy", "kingdom"),
                Kingdom_ITEM
        );
    }
}