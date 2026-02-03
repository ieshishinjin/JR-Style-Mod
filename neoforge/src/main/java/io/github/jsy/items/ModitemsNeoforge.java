// neoforge/src/main/java/io/github/jrstyle/neoforge/ModItemsNeoForge.java
package io.github.jsy.items;

import io.github.jsy.item.Moditems;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModitemsNeoforge {
    // NeoForge使用新的DeferredRegister API
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("jrstyle");

    // 存储注册的物品（NeoForge使用DeferredHolder）
    public static final Map<String, DeferredHolder<Item, Item>> REGISTERED_ITEMS = new HashMap<>();

    // 具体的物品定义
    public static final DeferredHolder<Item, Item> QUEEN;
    public static final DeferredHolder<Item, Item> KING;
    public static final DeferredHolder<Item, Item> PRINCE;

    static {
        // 初始化common模块
        Moditems.init();

        // 注册物品（NeoForge简化了语法）
        QUEEN = ITEMS.registerSimpleItem(
                Moditems.QUEEN_ID,
                () -> new Item(Moditems.QUEEN_PROPERTIES)
        );

        KING = ITEMS.registerSimpleItem(
                Moditems.KING_ID,
                () -> new Item(Moditems.KING_PROPERTIES)
        );

        PRINCE = ITEMS.registerSimpleItem(
                Moditems.PRINCE_ID,
                () -> new Item(Moditems.PRINCE_PROPERTIES)
        );

        // 存入映射
        REGISTERED_ITEMS.put(Moditems.QUEEN_ID, QUEEN);
        REGISTERED_ITEMS.put(Moditems.KING_ID, KING);
        REGISTERED_ITEMS.put(Moditems.PRINCE_ID, PRINCE);
    }

    // 获取物品实例的方法
    public static Item getQueen() {
        return QUEEN.get();
    }

    public static Item getKing() {
        return KING.get();
    }

    public static Item getPrince() {
        return PRINCE.get();
    }

    // NeoForge注册方法
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}