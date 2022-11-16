package net.darkexplosiveqwx.bat_elytra.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ArmoredBatElytra extends ArmorItem {
    public ArmoredBatElytra(ArmorMaterial materialIn, EquipmentSlot slot, Properties properties) {
        super(materialIn, slot, properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 10;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ModItems.BAT_WING.get();
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when
     * this item is used on a Block, see .
     */
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
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

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
    {
        if (!entity.level.isClientSide && (flightTicks + 1) % 20 == 0) {
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
        }
        return isUsable(stack);
    }
}