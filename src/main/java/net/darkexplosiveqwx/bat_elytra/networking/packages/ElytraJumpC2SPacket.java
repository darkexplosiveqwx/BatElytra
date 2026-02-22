package net.darkexplosiveqwx.bat_elytra.networking.packages;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ElytraJumpC2SPacket {
    public ElytraJumpC2SPacket() {
    }

    public ElytraJumpC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public static void handle(ElytraJumpC2SPacket message, CustomPayloadEvent.Context context) {
        var player = context.getSender();
        if (player == null) {
            return;
        }

        var chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestItem.is(BatElytra.ARMORED_BAT_ELYTRA.get()) || chestItem.is(BatElytra.BAT_ELYTRA.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 75, false, false, false));
        }
    }
}
