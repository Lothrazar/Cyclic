package com.lothrazar.cyclic.material;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GlowingArmorMaterial implements ArmorMaterial {

  public static final String GLOWINGID = ModCyclic.MODID + ":glowing";
  ArmorMaterials mimicArmor = ArmorMaterials.IRON;

  @Override
  public int getDurabilityForType(Type slotIn) {
    return mimicArmor.getDurabilityForType(slotIn);
  }

  @Override
  public int getDefenseForType(Type slotIn) {
    return mimicArmor.getDefenseForType(slotIn);
  }

  @Override
  public int getEnchantmentValue() {
    return mimicArmor.getEnchantmentValue() + 1;
  }

  @Override
  public SoundEvent getEquipSound() {
    return SoundRegistry.EQUIP_EMERALD.get();
  }

  @Override
  public Ingredient getRepairIngredient() {
    return Ingredient.of(new ItemStack(ItemRegistry.GEM_AMBER.get()));
  }

  @Override
  public String getName() {
    return GLOWINGID;
  }

  @Override
  public float getToughness() {
    return mimicArmor.getToughness();
  }

  @Override
  public float getKnockbackResistance() {
    return mimicArmor.getKnockbackResistance();
  }
}
