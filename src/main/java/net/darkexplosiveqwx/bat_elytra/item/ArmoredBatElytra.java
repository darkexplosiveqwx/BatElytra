package net.darkexplosiveqwx.bat_elytra.item;

import net.darkexplosiveqwx.bat_elytra.BatElytra;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArmoredBatElytra extends ArmorItem {
    public ArmoredBatElytra(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 10;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == BatElytra.BAT_WING.get();
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        EquipmentSlot slot = player.getEquipmentSlotForItem(held);
        ItemStack equipped = player.getItemBySlot(slot);

        if (equipped.isEmpty()) {
            player.setItemSlot(slot, held.copy());
            held.setCount(0);
            return InteractionResultHolder.sidedSuccess(held, world.isClientSide());
        }

        return InteractionResultHolder.fail(held);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return isUsable(stack);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide && (flightTicks + 1) % 20 == 0) {
            stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
        }
        return isUsable(stack);
    }
}
