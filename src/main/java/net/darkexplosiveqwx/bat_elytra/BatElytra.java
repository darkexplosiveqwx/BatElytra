package net.darkexplosiveqwx.bat_elytra;

import com.mojang.logging.LogUtils;
import net.darkexplosiveqwx.bat_elytra.item.ModItems;
import net.darkexplosiveqwx.bat_elytra.networking.ModMessages;
import net.darkexplosiveqwx.bat_elytra.networking.packages.ElytraJumpC2SPacket;
import net.darkexplosiveqwx.bat_elytra.renderer.*;
import net.darkexplosiveqwx.bat_elytra.util.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.*;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BatElytra.MODID)
public class BatElytra
{
    public static final String MODID = "bat_elytra";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public BatElytra()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModItems.ITEMS.register(modEventBus);
        ModMessages.register();
        KeyBindings.ELYTRA_JUMP.setToDefault();

        MinecraftForge.EVENT_BUS.register(this);
        if(FMLEnvironment.dist.isClient()) modEventBus.addListener(this::registerElytraLayer);
    }

    @OnlyIn(Dist.CLIENT)
    private void registerElytraLayer(EntityRenderersEvent event) {
        if(event instanceof EntityRenderersEvent.AddLayers addLayersEvent){
            EntityModelSet entityModels = addLayersEvent.getEntityModels();
            addLayersEvent.getSkins().forEach(s -> {
                LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> livingEntityRenderer = addLayersEvent.getSkin(s);
                if(livingEntityRenderer instanceof PlayerRenderer playerRenderer){
                    playerRenderer.addLayer(new BatElytraLayer(playerRenderer, entityModels));
                }
            });
            LivingEntityRenderer<ArmorStand, ? extends EntityModel<ArmorStand>> livingEntityRenderer = addLayersEvent.getRenderer(EntityType.ARMOR_STAND);
            if(livingEntityRenderer instanceof ArmorStandRenderer armorStandRenderer){
                armorStandRenderer.addLayer(new BatElytraArmorStandLayer(armorStandRenderer, entityModels));
            }

        }
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    private  void addCreative(CreativeModeTabEvent.BuildContents event){
        if(event.getTab() == CreativeModeTabs.COMBAT){
            event.accept(ModItems.ARMORED_BAT_ELYTRA);
            event.accept(ModItems.BAT_ELYTRA);
        }
        if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.BAT_WING);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());


        }

        @Mod.EventBusSubscriber(modid = BatElytra.MODID, value = Dist.CLIENT)
        public static class ClientForgeEvents {
            @SubscribeEvent
            public static void onKeyInput(InputEvent.Key event) {
                if (KeyBindings.ELYTRA_JUMP.consumeClick()) {
                    assert Minecraft.getInstance().player != null;
                    ModMessages.sendToServer(new ElytraJumpC2SPacket());
                }
            }
        }

        @Mod.EventBusSubscriber(modid = BatElytra.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ClientModBusEvents {
            @SubscribeEvent
            public static void onKeyRegister(RegisterKeyMappingsEvent event) {
                event.register(KeyBindings.ELYTRA_JUMP);
            }
        }
    }
}
