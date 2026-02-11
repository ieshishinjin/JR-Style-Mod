package io.github.jsy.item;

import net.minecraft.world.item.Item; // 注意：使用Minecraft官方映射名称（mojang）
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class Moditems extends Item {
    public Moditems() {
        // 调用父类构造器，设置基础属性
        super(new Properties()
                .stacksTo(16)
                .rarity(Rarity.COMMON)
        );
    }

    // 这里可以添加更多与加载器无关的自定义逻辑
}