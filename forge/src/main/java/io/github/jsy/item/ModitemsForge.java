// forge/src/main/java/io/github/jrstyle/forge/ModItemsForge.java
package io.github.jsy.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ModitemsForge {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "jrstyle");

    // 存储注册的物品
    public static final Map<String, RegistryObject<Item>> REGISTERED_ITEMS = new HashMap<>();

    static {
        // 使用common模块中定义的ID进行注册
        for (String itemId : Moditems.getAllItemIds()) {
            RegistryObject<Item> item = ITEMS.register(
                    itemId,
                    () -> new Item(Moditems.getPropertiesForId(itemId))
            );
            REGISTERED_ITEMS.put(itemId, item);
        }
    }

    // 获取物品实例的方法
    public static Item getQueen() {
        return REGISTERED_ITEMS.get(Moditems.QUEEN_ID).get();
    }

    public static Item getKing() {
        return REGISTERED_ITEMS.get(Moditems.KING_ID).get();
    }

    public static Item getPrince() {
        return REGISTERED_ITEMS.get(Moditems.PRINCE_ID).get();
    }

    public static void register(IEventBus eventBus) {
        // 初始化common模块
        Moditems.init();
        // 注册物品
        ITEMS.register(eventBus);
    }
}