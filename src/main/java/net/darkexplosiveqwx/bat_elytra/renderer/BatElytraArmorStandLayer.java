package net.darkexplosiveqwx.bat_elytra.renderer;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.darkexplosiveqwx.bat_elytra.item.ModItems;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class BatElytraArmorStandLayer extends ElytraLayer<ArmorStand, ArmorStandArmorModel> {
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation(BatElytra.MODID,
            "textures/entity/bat_elytra.png");

    public BatElytraArmorStandLayer(ArmorStandRenderer armorStandRenderer,
                                    EntityModelSet entityModelSet) {
        super(armorStandRenderer, entityModelSet);
    }

    @Override
    public boolean shouldRender(ItemStack stack, ArmorStand entity) {
        return stack.getItem() == ModItems.BAT_ELYTRA.get() || stack.getItem() == ModItems.ARMORED_BAT_ELYTRA.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, ArmorStand entity) {
        return TEXTURE_ELYTRA;
    }
}
