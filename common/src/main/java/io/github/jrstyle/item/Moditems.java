// common/src/main/java/io/github/jrstyle/item/ModItems.java
package io.github.jrstyle.item;

import net.minecraft.world.item.Item;

public class Moditems {
    // 物品ID定义（所有加载器共享）
    public static final String QUEEN_ID = "queen";
    public static final String KING_ID = "king";
    public static final String PRINCE_ID = "prince"; // 修改：去掉路径，只用名字

    // 物品属性定义（使用Vanilla API）
    private static Item.Properties createDefaultProperties() {
        return new Item.Properties();
    }

    public static final Item.Properties QUEEN_PROPERTIES = createDefaultProperties();
    public static final Item.Properties KING_PROPERTIES = createDefaultProperties();
    public static final Item.Properties PRINCE_PROPERTIES = createDefaultProperties();

    /**
     * 获取所有物品ID的数组
     * 供加载器模块注册时使用
     */
    public static String[] getAllItemIds() {
        return new String[]{QUEEN_ID, KING_ID, PRINCE_ID};
    }

    /**
     * 根据ID获取对应的属性配置
     */
    public static Item.Properties getPropertiesForId(String itemId) {
        switch (itemId) {
            case QUEEN_ID:
                return QUEEN_PROPERTIES;
            case KING_ID:
                return KING_PROPERTIES;
            case PRINCE_ID:
                return PRINCE_PROPERTIES;
            default:
                return createDefaultProperties();
        }
    }

    /**
     * 初始化方法 - 可以在所有加载器中调用
     */
    public static void init() {
        // 这里可以添加物品的通用初始化逻辑
        // 例如：注册CreativeModeTab、设置其他通用属性等
    }
}