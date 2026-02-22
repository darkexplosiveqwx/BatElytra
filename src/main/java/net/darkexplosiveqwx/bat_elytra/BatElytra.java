package net.darkexplosiveqwx.bat_elytra;

import com.mojang.logging.LogUtils;
import net.darkexplosiveqwx.bat_elytra.item.ArmoredBatElytra;
import net.darkexplosiveqwx.bat_elytra.networking.ModMessages;
import net.darkexplosiveqwx.bat_elytra.networking.packages.ElytraJumpC2SPacket;
import net.darkexplosiveqwx.bat_elytra.renderer.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.*;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.*;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BatElytra.MODID)
public class BatElytra
{
    public static final String MODID = "bat_elytra";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CreativeModeTab BAT_TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return BAT_ELYTRA.get().getDefaultInstance();
        }
    };
    public static final TagKey<Item> BAT_ITEM_TAG = ItemTags.create(new ResourceLocation(MODID, "bat_elytra_items"));
    public static final TagKey<Item> BAT_ELYTRAS_TAG = ItemTags.create(new ResourceLocation(MODID, "bat_elytras"));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> BAT_ELYTRA = ITEMS.register(
            "bat_elytra",
            () -> new ArmoredBatElytra(
                    ArmorMaterials.LEATHER,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(BAT_TAB).stacksTo(1)
            )
    );
    public static final RegistryObject<Item> BAT_WING = ITEMS.register(
            "bat_wing",
            () -> new ElytraItem(new Item.Properties().tab(BAT_TAB))
    );
    public static final RegistryObject<Item> ARMORED_BAT_ELYTRA = ITEMS.register(
            "armored_bat_elytra",
            () -> new ArmoredBatElytra(
                    ArmorMaterials.NETHERITE,
                    EquipmentSlot.CHEST,
                    new Item.Properties().tab(BAT_TAB).fireResistant().stacksTo(1)
            )
    );

    private static final KeyMapping ELYTRA_JUMP_KEYBIND = new KeyMapping(
            "key.bat_elytra.elytra_jump",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.category.bat_elytra.main"
    );
    public BatElytra()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        ModMessages.register();
        ELYTRA_JUMP_KEYBIND.setToDefault();

        MinecraftForge.EVENT_BUS.register(this);
        if(FMLEnvironment.dist.isClient()) modEventBus.addListener(this::registerElytraLayer);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent(priority = EventPriority.LOW)
        public static void renderPlayer(final EntityRenderersEvent.AddLayers event) {
            //default
            LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin("default");
            ElytraRenderer<Player, PlayerModel<Player>> layer = new ElytraRenderer<>(renderer, event.getEntityModels());
            if (renderer != null) {

                renderer.addLayer(layer);
            } else {
                LOGGER.error("Couldn't get renderer");
            }
            //Slim
            renderer = event.getSkin("slim");
            layer = new ElytraRenderer<>(renderer, event.getEntityModels());
            if (renderer != null) {

                renderer.addLayer(layer);
            } else {
                LOGGER.error("Couldn't get renderer");
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

        private static <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
            R renderer = evt.getRenderer(entityType);

            if (renderer != null) {
                renderer.addLayer(new ElytraRenderer<>(renderer, evt.getEntityModels()));
            }
        }
    }
    @Mod.EventBusSubscriber(modid = BatElytra.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents{
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event){
            if(ELYTRA_JUMP_KEYBIND.consumeClick()){
                assert Minecraft.getInstance().player != null;
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
}
