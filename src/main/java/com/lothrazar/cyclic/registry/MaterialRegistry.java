package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class MaterialRegistry {

  public static final CreativeModeTab BLOCK_GROUP = new CreativeModeTab(ModCyclic.MODID) {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(BlockRegistry.trash);
    }
  };
  public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(ModCyclic.MODID + "items") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ItemRegistry.gem_amber);
    }
  };
  public static IntValue EMERALD_BOOTS;
  public static IntValue EMERALD_LEG;
  public static IntValue EMERALD_CHEST;
  public static IntValue EMERALD_HELM;
  public static IntValue OBS_BOOTS;
  public static IntValue OBS_LEG;
  public static IntValue OBS_CHEST;
  public static IntValue OBS_HELM;
  public static DoubleValue EMERALD_TOUGH;
  public static DoubleValue EMERALD_DMG;
  public static DoubleValue OBS_TOUGH;
  public static DoubleValue OBS_DMG;

  public static class ArmorMats {

    private static final String EMERALDID = ModCyclic.MODID + ":emerald";
    private static final String CRYSTALID = ModCyclic.MODID + ":crystal";
    private static final String GLOWINGID = ModCyclic.MODID + ":glowing";
    public static final ArmorMaterial EMERALD = new ArmorMaterial() {

      @Override
      public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return ArmorMaterials.DIAMOND.getDurabilityForSlot(slotIn) + ArmorMaterials.IRON.getDurabilityForSlot(slotIn);
      }

      @Override
      public int getDefenseForSlot(EquipmentSlot slot) {
        switch (slot) {
          case CHEST:
            return EMERALD_CHEST.get();
          case FEET:
            return EMERALD_BOOTS.get();
          case HEAD:
            return EMERALD_HELM.get();
          case LEGS:
            return EMERALD_LEG.get();
          case MAINHAND:
          case OFFHAND:
          default:
          break;
        }
        return 0; //ArmorMaterial.DIAMOND.getDamageReductionAmount(slot) + ArmorMaterial.IRON.getDamageReductionAmount(slot);
      }

      @Override
      public int getEnchantmentValue() {
        return ArmorMaterials.GOLD.getEnchantmentValue();
      }

      @Override
      public SoundEvent getEquipSound() {
        return SoundRegistry.ITEM_ARMOR_EQUIP_EMERALD;
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
        return EMERALD_TOUGH.get().floatValue();
      }

      @Override
      public float getKnockbackResistance() {
        return ArmorMaterials.DIAMOND.getKnockbackResistance();
      }
    };
    public static final ArmorMaterial GEMOBSIDIAN = new ArmorMaterial() {

      @Override
      public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return ArmorMaterials.DIAMOND.getDurabilityForSlot(slotIn) * 4;
      }

      @Override
      public int getDefenseForSlot(EquipmentSlot slot) {
        switch (slot) {
          case CHEST:
            return OBS_CHEST.get();
          case FEET:
            return OBS_BOOTS.get();
          case HEAD:
            return OBS_HELM.get();
          case LEGS:
            return OBS_LEG.get();
          case MAINHAND:
          case OFFHAND:
          default:
          break;
        }
        return 0; // ArmorMaterial.DIAMOND.getDamageReductionAmount(slotIn) * 3;
      }

      @Override
      public int getEnchantmentValue() {
        return ArmorMaterials.GOLD.getEnchantmentValue() + 3;
      }

      @Override
      public SoundEvent getEquipSound() {
        return SoundRegistry.ITEM_ARMOR_EQUIP_EMERALD;
      }

      @Override
      public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(ItemRegistry.gem_obsidian));
      }

      @Override
      public String getName() {
        return CRYSTALID;
      }

      @Override
      public float getToughness() {
        return OBS_TOUGH.get().floatValue();
      }

      @Override
      public float getKnockbackResistance() {
        return ArmorMaterials.NETHERITE.getKnockbackResistance();
      }
    };
    public static final ArmorMaterial GLOWING = new ArmorMaterial() {

      ArmorMaterials mimicArmor = ArmorMaterials.IRON;

      @Override
      public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return mimicArmor.getDurabilityForSlot(slotIn);
      }

      @Override
      public int getDefenseForSlot(EquipmentSlot slotIn) {
        return mimicArmor.getDefenseForSlot(slotIn);
      }

      @Override
      public int getEnchantmentValue() {
        return mimicArmor.getEnchantmentValue() + 1;
      }

      @Override
      public SoundEvent getEquipSound() {
        return SoundRegistry.ITEM_ARMOR_EQUIP_EMERALD;
      }

      @Override
      public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(ItemRegistry.gem_amber));
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
    };
  }

  public static class ToolMats {

    public static final Tier GEMOBSIDIAN = new Tier() {

      @Override
      public int getUses() {
        return Tiers.DIAMOND.getUses() * 4;
      }

      @Override
      public float getSpeed() {
        return Tiers.DIAMOND.getSpeed() * 4;
      }

      @Override
      public float getAttackDamageBonus() {
        return OBS_DMG.get().floatValue();
      }

      @Override
      public int getLevel() {
        return Tiers.DIAMOND.getLevel() + 1;
      }

      @Override
      public int getEnchantmentValue() {
        return Tiers.GOLD.getEnchantmentValue() + 1;
      }

      @Override
      public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(ItemRegistry.gem_obsidian));
      }
    };
    public static final Tier EMERALD = new Tier() {

      @Override
      public int getUses() {
        return Tiers.DIAMOND.getUses() + Tiers.GOLD.getUses();
      }

      @Override
      public float getSpeed() {
        return Tiers.DIAMOND.getSpeed() * 2;
      }

      @Override
      public float getAttackDamageBonus() {
        return EMERALD_DMG.get().floatValue();
      }

      @Override
      public int getLevel() {
        return Tiers.DIAMOND.getLevel();
      }

      @Override
      public int getEnchantmentValue() {
        return Tiers.GOLD.getEnchantmentValue() + 1;
      }

      @Override
      public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(net.minecraft.world.item.Items.EMERALD));
      }
    };
    public static final Tier SANDSTONE = new Tier() {

      @Override
      public int getUses() {
        return Tiers.STONE.getUses() - 2;
      }

      @Override
      public float getSpeed() {
        return Tiers.STONE.getSpeed();
      }

      @Override
      public float getAttackDamageBonus() {
        return (Tiers.WOOD.getAttackDamageBonus() + Tiers.STONE.getAttackDamageBonus()) / 2;
      }

      @Override
      public int getLevel() {
        return Tiers.STONE.getLevel();
      }

      @Override
      public int getEnchantmentValue() {
        return (Tiers.WOOD.getEnchantmentValue() + Tiers.STONE.getEnchantmentValue()) / 2;
      }

      @Override
      public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(net.minecraft.world.level.block.Blocks.SANDSTONE));
      }
    };
    public static final Tier NETHERBRICK = new Tier() {

      @Override
      public int getUses() {
        return (Tiers.IRON.getUses() + Tiers.STONE.getUses()) / 2;
      }

      @Override
      public float getSpeed() {
        return (Tiers.IRON.getSpeed() + Tiers.STONE.getSpeed()) / 2;
      }

      @Override
      public float getAttackDamageBonus() {
        return (Tiers.IRON.getAttackDamageBonus() + Tiers.STONE.getAttackDamageBonus()) / 2;
      }

      @Override
      public int getLevel() {
        return Tiers.IRON.getLevel();
      }

      @Override
      public int getEnchantmentValue() {
        return (Tiers.IRON.getEnchantmentValue() + Tiers.STONE.getEnchantmentValue()) / 2;
      }

      @Override
      public Ingredient getRepairIngredient() {
        return Ingredient.of(new ItemStack(net.minecraft.world.level.block.Blocks.NETHER_BRICKS));
      }
    };
  }
}
