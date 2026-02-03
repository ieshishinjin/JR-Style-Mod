// fabric/src/main/java/io/github/jrstyle/fabric/ModItemsFabric.java
package io.github.jsy.items;

import io.github.jsy.item.Moditems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ModitemsFabric {
    // 存储注册的物品
    public static final Map<String, Item> REGISTERED_ITEMS = new HashMap<>();

    public static final Item QUEEN;
    public static final Item KING;
    public static final Item PRINCE;

    static {
        // 初始化common模块
        Moditems.init();

        // 创建物品实例
        QUEEN = new Item(new FabricItemSettings());
        KING = new Item(new FabricItemSettings());
        PRINCE = new Item(new FabricItemSettings());

        // 存入映射
        REGISTERED_ITEMS.put(Moditems.QUEEN_ID, QUEEN);
        REGISTERED_ITEMS.put(Moditems.KING_ID, KING);
        REGISTERED_ITEMS.put(Moditems.PRINCE_ID, PRINCE);
    }

    // 注册方法 - 在Fabric初始化时调用
    public static void register() {
        for (Map.Entry<String, Item> entry : REGISTERED_ITEMS.entrySet()) {
            Registry.register(
                    BuiltInRegistries.ITEM,
                    new ResourceLocation("jrstyle", entry.getKey()),
                    entry.getValue()
            );
        }
    }

    // 获取物品实例的方法（与Forge保持相同API）
    public static Item getQueen() {
        return QUEEN;
    }

    public static Item getKing() {
        return KING;
    }

    public static Item getPrince() {
        return PRINCE;
    }
}