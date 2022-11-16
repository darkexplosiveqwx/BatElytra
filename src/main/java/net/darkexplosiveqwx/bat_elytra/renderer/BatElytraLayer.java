package net.darkexplosiveqwx.bat_elytra.renderer;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.darkexplosiveqwx.bat_elytra.item.ModItems;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.player.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.*;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BatElytraLayer
        extends ElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation(BatElytra.MODID,
            "textures/entity/bat_elytra.png");

    public BatElytraLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> rendererIn,
            EntityModelSet modelSet) {
        super(rendererIn, modelSet);
    }

    @Override
    public boolean shouldRender(ItemStack stack, @NotNull AbstractClientPlayer entity) {
        return stack.getItem() == ModItems.BAT_ELYTRA.get() || stack.getItem() == ModItems.ARMORED_BAT_ELYTRA.get();
    }

    @Override
    public @NotNull ResourceLocation getElytraTexture(@NotNull ItemStack stack, @NotNull AbstractClientPlayer entity) {
        return TEXTURE_ELYTRA;
    }

}