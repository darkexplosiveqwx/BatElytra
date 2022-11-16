package net.darkexplosiveqwx.bat_elytra.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final CreativeModeTab BAT_TAB = new CreativeModeTab("bat_elytra") {
        @Override
        public ItemStack makeIcon() {
            return ModItems.BAT_ELYTRA.get().getDefaultInstance();
        }
    };
}
