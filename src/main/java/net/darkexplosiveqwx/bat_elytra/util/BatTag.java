package net.darkexplosiveqwx.bat_elytra.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;

public class BatTag {
    public static final TagKey<Item> BAT_ITEM = ItemTags.create(new ResourceLocation("bat_elytra", "bat_elytra_items"));
    public static final TagKey<Item> BAT_ELYTRAS = ItemTags.create(new ResourceLocation("bat_elytra", "bat_elytras"));
}