package net.darkexplosiveqwx.bat_elytra.renderer;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BatElytraArmorStandLayer extends ElytraLayer<ArmorStand, ArmorStandArmorModel> {
    private static final ResourceLocation TEXTURE_ELYTRA = ResourceLocation.fromNamespaceAndPath(BatElytra.MODID, "textures/entity/bat_elytra.png");

    public BatElytraArmorStandLayer(ArmorStandRenderer armorStandRenderer, EntityModelSet entityModelSet) {
        super(armorStandRenderer, entityModelSet);
    }

    @Override
    public boolean shouldRender(ItemStack stack, ArmorStand entity) {
        return stack.getItem() == BatElytra.BAT_ELYTRA.get() || stack.getItem() == BatElytra.ARMORED_BAT_ELYTRA.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, ArmorStand entity) {
        return TEXTURE_ELYTRA;
    }
}
