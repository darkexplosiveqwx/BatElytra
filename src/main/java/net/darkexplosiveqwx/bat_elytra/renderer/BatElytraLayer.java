package net.darkexplosiveqwx.bat_elytra.renderer;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BatElytraLayer extends ElytraLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation TEXTURE_ELYTRA = ResourceLocation.fromNamespaceAndPath(BatElytra.MODID, "textures/entity/bat_elytra.png");

    public BatElytraLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> rendererIn, EntityModelSet modelSet) {
        super(rendererIn, modelSet);
    }

    @Override
    public boolean shouldRender(ItemStack stack, @NotNull AbstractClientPlayer entity) {
        return stack.getItem() == BatElytra.BAT_ELYTRA.get() || stack.getItem() == BatElytra.ARMORED_BAT_ELYTRA.get();
    }

    @Override
    public @NotNull ResourceLocation getElytraTexture(@NotNull ItemStack stack, @NotNull AbstractClientPlayer entity) {
        return TEXTURE_ELYTRA;
    }
}
