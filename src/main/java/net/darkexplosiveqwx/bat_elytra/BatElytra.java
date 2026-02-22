package net.darkexplosiveqwx.bat_elytra;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.darkexplosiveqwx.bat_elytra.item.ArmoredBatElytra;
import net.darkexplosiveqwx.bat_elytra.networking.ModMessages;
import net.darkexplosiveqwx.bat_elytra.networking.packages.ElytraJumpC2SPacket;
import net.darkexplosiveqwx.bat_elytra.renderer.ElytraRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod(BatElytra.MODID)
public class BatElytra {
    public static final String MODID = "bat_elytra";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final TagKey<Item> BAT_ELYTRAS_TAG = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "bat_elytras"));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> BAT_ELYTRA = ITEMS.register(
            "bat_elytra",
            () -> new ArmoredBatElytra(
                    ArmorMaterials.LEATHER,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1)
            )
    );
    public static final RegistryObject<Item> BAT_WING = ITEMS.register(
            "bat_wing",
            () -> new ElytraItem(new Item.Properties())
    );
    public static final RegistryObject<Item> ARMORED_BAT_ELYTRA = ITEMS.register(
            "armored_bat_elytra",
            () -> new ArmoredBatElytra(
                    ArmorMaterials.NETHERITE,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().fireResistant().stacksTo(1)
            )
    );
    public static final RegistryObject<CreativeModeTab> BAT_TAB = CREATIVE_MODE_TABS.register("bat_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> BAT_ELYTRA.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BAT_ELYTRA.get());
                output.accept(BAT_WING.get());
                output.accept(ARMORED_BAT_ELYTRA.get());
            }).build());


    private static final KeyMapping ELYTRA_JUMP_KEYBIND = new KeyMapping(
            "key.bat_elytra.elytra_jump",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.category.bat_elytra.main"
    );

    public BatElytra(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();
        modEventBus.addListener(this::addCreative);

        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModMessages.register();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(BAT_WING);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(BAT_ELYTRA);
            event.accept(ARMORED_BAT_ELYTRA);
        }
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void renderPlayer(final EntityRenderersEvent.AddLayers event) {
            var wideRenderer = event.getPlayerSkin(PlayerSkin.Model.WIDE);
            if (wideRenderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new ElytraRenderer<>(playerRenderer, event.getEntityModels()));
            }

            var slimRenderer = event.getPlayerSkin(PlayerSkin.Model.SLIM);
            if (slimRenderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new ElytraRenderer<>(playerRenderer, event.getEntityModels()));
            }

            addEntityLayer(event, EntityType.ARMOR_STAND);
            addEntityLayer(event, EntityType.ZOMBIE);
            addEntityLayer(event, EntityType.ZOMBIE_VILLAGER);
            addEntityLayer(event, EntityType.SKELETON);
            addEntityLayer(event, EntityType.HUSK);
            addEntityLayer(event, EntityType.STRAY);
            addEntityLayer(event, EntityType.WITHER_SKELETON);
            addEntityLayer(event, EntityType.DROWNED);
            addEntityLayer(event, EntityType.PIGLIN);
            addEntityLayer(event, EntityType.PIGLIN_BRUTE);
            addEntityLayer(event, EntityType.ZOMBIFIED_PIGLIN);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private static <T extends LivingEntity> void addEntityLayer(EntityRenderersEvent.AddLayers event, EntityType<? extends T> entityType) {
            var renderer = event.getEntityRenderer(entityType);
            if (renderer instanceof LivingEntityRenderer livingRenderer) {
                livingRenderer.addLayer(new ElytraRenderer<>(livingRenderer, event.getEntityModels()));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BatElytra.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (ELYTRA_JUMP_KEYBIND.consumeClick() && Minecraft.getInstance().player != null) {
                ModMessages.sendToServer(new ElytraJumpC2SPacket());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BatElytra.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(ELYTRA_JUMP_KEYBIND);
        }
    }
}
