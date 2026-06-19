package io.github.jsy;

import com.mojang.logging.LogUtils;
import io.github.jsy.block.*;
import io.github.jsy.item.ModCreativeModTabForge;
import io.github.jsy.item.ModItemForge;
import io.github.jsy.network.ForgeNetworkHandler;
import io.github.jsy.network.UpdateLineColorC2SPacket;
import io.github.jsy.platform.LineColorSyncHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Constants.MOD_ID)
public class jsy {

    public static final String MOD_ID = "jsy";
    private static final Logger LOGGER = LogUtils.getLogger();

    public jsy(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register items (DeferredRegister)
        ModItemForge.ITEMS.register(modEventBus);

        // Register creative tab (DeferredRegister)
        ModCreativeModTabForge.register(modEventBus);

        // Register network packets
        ForgeNetworkHandler.register();

        // Register blocks directly (vanilla Registry API, same as Fabric)
        ModBlocks.registerBlocks((name, block) -> {
            ResourceLocation id = new ResourceLocation(Constants.MOD_ID, name);
            Registry.register(BuiltInRegistries.BLOCK, id, block);
            Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties()));
        });

        // Register block entities directly
        ModBlockEntities.LINE_COLOR_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "line_color_block_entity"),
                BlockEntityType.Builder.of(LineColorBlockEntity::new, ModBlocks.LINE_COLOR_BLOCK).build(null)
        );
        ModBlockEntities.JR_STATION_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "jr_station_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityJRStationSign::new,
                        ModBlocks.JR_STATION_SIGN_HANGING, ModBlocks.JR_STATION_SIGN_POLE).build(null)
        );
        ModBlockEntities.PLATFORM_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "platform_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityPlatformSign::new, ModBlocks.PLATFORM_SIGN).build(null)
        );
        ModBlockEntities.DIRECTION_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "direction_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityDirectionSign::new, ModBlocks.DIRECTION_SIGN).build(null)
        );

        // 设置线路颜色同步处理器（客户端检测到MTR颜色后发回服务端）
        LineColorBlockEntity.setSyncHandler((pos, color, lineId) -> {
            ForgeNetworkHandler.getChannel().send(
                    new UpdateLineColorC2SPacket(pos, color, lineId != null ? lineId : ""),
                    net.minecraftforge.network.PacketDistributor.SERVER.noArg()
            );
            LOGGER.debug("Syncing line color to server: {} at {}", String.format("0x%06X", color), pos);
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.LINE_COLOR_BLOCK);
            event.accept(ModBlocks.JR_STATION_SIGN_HANGING);
            event.accept(ModBlocks.JR_STATION_SIGN_POLE);
            event.accept(ModBlocks.PLATFORM_SIGN);
            event.accept(ModBlocks.DIRECTION_SIGN);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
