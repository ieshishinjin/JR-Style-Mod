package io.github.jsy.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item;

public class MagicSwordItem extends SwordItem {
    public MagicSwordItem() {
        // Tiers.DIAMOND 定义了基础属性，4是额外伤害，-2.4f是攻击速度
        super(Tiers.DIAMOND, 4, -2.4f, new Item.Properties());
    }

    // 在这里可以重写方法来实现特殊功能，比如右键发射火球...
}