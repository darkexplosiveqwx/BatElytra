package net.darkexplosiveqwx.bat_elytra.item;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BatElytra.MODID);

    public static final RegistryObject<Item> BAT_ELYTRA = ITEMS.register("bat_elytra",() -> new ArmoredBatElytra(ArmorMaterials.LEATHER, EquipmentSlot.CHEST, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BAT_WING = ITEMS.register("bat_wing", () -> new ElytraItem( new Item.Properties()));
    public static final RegistryObject<Item> ARMORED_BAT_ELYTRA = ITEMS.register("armored_bat_elytra", () -> new ArmoredBatElytra(ArmorMaterials.NETHERITE, EquipmentSlot.CHEST, new Item.Properties().fireResistant().stacksTo(1)));

}
