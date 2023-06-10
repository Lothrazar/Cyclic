package com.lothrazar.cyclic.material;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MaterialRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GemArmorMaterial implements ArmorMaterial {

  private static final String CRYSTALID = ModCyclic.MODID + ":crystal";

  @Override
  public int getDurabilityForType(Type slotIn) {
    return ArmorMaterials.DIAMOND.getDurabilityForType(slotIn) * 4;
  }

  @Override
  public int getDefenseForType(Type slot) {
    switch (slot) {
      case CHESTPLATE:
        return MaterialRegistry.OBS_CHEST.get();
      case BOOTS:
        return MaterialRegistry.OBS_BOOTS.get();
      case HELMET:
        return MaterialRegistry.OBS_HELM.get();
      case LEGGINGS:
        return MaterialRegistry.OBS_LEG.get();
      default:
      break;
    }
    return 0;
  }

  @Override
  public int getEnchantmentValue() {
    return ArmorMaterials.GOLD.getEnchantmentValue() + 3;
  }

  @Override
  public SoundEvent getEquipSound() {
    return SoundRegistry.EQUIP_EMERALD.get();
  }

  @Override
  public Ingredient getRepairIngredient() {
    return Ingredient.of(new ItemStack(ItemRegistry.GEM_OBSIDIAN.get()));
  }

  @Override
  public String getName() {
    return CRYSTALID;
  }

  @Override
  public float getToughness() {
    return MaterialRegistry.OBS_TOUGH.get().floatValue();
  }

  @Override
  public float getKnockbackResistance() {
    return ArmorMaterials.NETHERITE.getKnockbackResistance();
  }
}
