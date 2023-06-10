package com.lothrazar.cyclic.material;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class EmeraldArmorMaterial implements ArmorMaterial {

  public static final String EMERALDID = ModCyclic.MODID + ":emerald";

  @Override
  public int getDurabilityForType(Type slotIn) {
    return (ArmorMaterials.DIAMOND.getDurabilityForType(slotIn) +
        ArmorMaterials.NETHERITE.getDurabilityForType(slotIn)) / 2;
  }

  @Override
  public int getDefenseForType(Type slot) {
    switch (slot) {
      case CHESTPLATE:
        return MaterialRegistry.EMERALD_CHEST.get();
      case BOOTS:
        return MaterialRegistry.EMERALD_BOOTS.get();
      case HELMET:
        return MaterialRegistry.EMERALD_HELM.get();
      case LEGGINGS:
        return MaterialRegistry.EMERALD_LEG.get();
      default:
      break;
    }
    return 0;
  }

  @Override
  public int getEnchantmentValue() {
    return ArmorMaterials.GOLD.getEnchantmentValue();
  }

  @Override
  public SoundEvent getEquipSound() {
    return SoundRegistry.EQUIP_EMERALD.get();
  }

  @Override
  public Ingredient getRepairIngredient() {
    return Ingredient.of(new ItemStack(Items.EMERALD));
  }

  @Override
  public String getName() {
    return EMERALDID;
  }

  @Override
  public float getToughness() {
    return MaterialRegistry.EMERALD_TOUGH.get().floatValue();
  }

  @Override
  public float getKnockbackResistance() {
    return (ArmorMaterials.DIAMOND.getKnockbackResistance() + ArmorMaterials.NETHERITE.getKnockbackResistance()) / 2;
  }
}
