package net.darkexplosiveqwx.bat_elytra.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BatElytraItem extends ElytraItem {

    public BatElytraItem(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }
    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ModItems.BAT_WING.get();
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        EquipmentSlot equipmentslottype = Mob.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = playerIn.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            playerIn.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Override
    public boolean canElytraFly(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return BatElytraItem.isUsable(stack);
    }

    @Override
    public boolean elytraFlightTick(@NotNull ItemStack stack, LivingEntity entity, int flightTicks) {
        // Adding 1 to ticksElytraFlying prevents damage on the very first tick.
        if (!entity.level.isClientSide && (flightTicks + 1) % 25 == 0) {
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
        }
        return true;
    }

}