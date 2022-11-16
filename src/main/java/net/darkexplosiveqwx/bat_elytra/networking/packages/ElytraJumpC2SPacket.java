package net.darkexplosiveqwx.bat_elytra.networking.packages;

import net.darkexplosiveqwx.bat_elytra.item.ModItems;
import net.minecraft.network.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.*;

import java.util.function.Supplier;

public class ElytraJumpC2SPacket {
    public ElytraJumpC2SPacket(){

    }

    public ElytraJumpC2SPacket(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()->{
            //SERVER SIDE NOW
            ServerPlayer player = context.getSender();
            assert player != null;
            ServerLevel level = player.getLevel();

             //if(player.getInventory().armor.get(2) != ModItems.BAT_ELYTRA.get().getDefaultInstance() || player.getInventory().armor.get(2) != ModItems.ARMORED_BAT_ELYTRA.get().getDefaultInstance()) return;
            if(!player.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.ARMORED_BAT_ELYTRA.get()) || !player.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.BAT_ELYTRA.get())) return;


            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 75, false, false, false));


        });
    }


}
