// ModItems.java - 存放于 fabric/
package io.github.jsy.items;

import io.github.jsy.item.Moditems; // 导入libs中的核心物品类
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModitemsFabric {
    // 声明物品实例
    public static final Moditems Queen = new Moditems();
    public static final Moditems King = new Moditems();
    public static final Moditems Prince = new Moditems();

    public static void register() {
        // 将物品注册到游戏注册表中
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation("jsy", "queen"), // 物品ID: bmltransit:blue_moon
                Queen
        );
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation("jsy", "king"), // 物品ID: bmltransit:blue_moon
                King
        );
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation("jsy", "prince"), // 物品ID: bmltransit:blue_moon
                Prince
        );
    }

}