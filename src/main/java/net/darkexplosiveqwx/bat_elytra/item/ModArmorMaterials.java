package net.darkexplosiveqwx.bat_elytra.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    NONE("none", 37, new int[]{0, 0, 0, 0}, 15, SoundEvents.ARMOR_EQUIP_ELYTRA, 0F, 0F, () -> {
        return Ingredient.of(ItemStack.EMPTY);
    });

    private static final int[] baseDurability = { 128, 144, 160, 112 };
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Ingredient repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier, int[] armorValueArr, int enchantability,
                      SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValueArr;
        this.enchantability = enchantability;
        this.equipSound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient.get();
    }

    public int getDurabilityForSlot(EquipmentSlot equipmentSlot_1) {
        return baseDurability[equipmentSlot_1.getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(EquipmentSlot equipmentSlot_1) {
        return this.armorValues[equipmentSlot_1.getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        // We needed to make it a Lazy type so we can actually get the Ingredient from
        // the Supplier.
        return this.repairIngredient;
    }

    @Override
    public String getName() {
        return null;
    }


    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}