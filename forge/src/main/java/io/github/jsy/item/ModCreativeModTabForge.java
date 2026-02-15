package io.github.jsy.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabForge {
    //  创建物品栏注册器
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "jsy");
    // 4. 注册物品栏
    public static final RegistryObject<CreativeModeTab> JR_STYLE_TAB =
            TABS.register("main", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.jsy.main"))
//                            .icon(() -> Apple.get().getDefaultInstance())
//                            .displayItems((params, output) -> {
//                                output.accept(King.get());
//                                output.accept(Prince.get());
//                                output.accept(Queen.get());
//                                output.accept(Kingdom.get());
//                                // 可继续添加其他物品
//                            })
                            .build()
            );

    // 5. 将注册器连接到事件总线
    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}