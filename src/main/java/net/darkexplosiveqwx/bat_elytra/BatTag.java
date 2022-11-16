package net.darkexplosiveqwx.bat_elytra;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class BatTag {
    public static final TagKey<Item> BAT_ITEM = ItemTags.create(new ResourceLocation("bat_elytra", "bat_elytra_items"));
    public static final TagKey<Item> BAT_ELYTRAS = ItemTags.create(new ResourceLocation("bat_elytra", "bat_elytras"));
}