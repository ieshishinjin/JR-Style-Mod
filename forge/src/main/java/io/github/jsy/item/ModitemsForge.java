// ModItems.java - 存放于 forge/
package io.github.jsy.item;

import io.github.jsy.item.Moditems; // 导入libs中的核心物品类
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static io.github.jsy.Constants.MOD_ID;

public class ModitemsForge {
    // 创建延迟注册器
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID); // 你的模组ID

    // 注册物品，并持有其引用
    public static final RegistryObject<Item> Queen = ITEMS.register("queen",
            Moditems::new // 使用构造函数引用
    );
    public static final RegistryObject<Item> King = ITEMS.register("king",
            Moditems::new // 使用构造函数引用
    );
    public static final RegistryObject<Item> Prince = ITEMS.register("prince",
            Moditems::new // 使用构造函数引用
    );

    // 将注册器添加到模组事件总线
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}