package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class MaterialRegistry {

  public static ItemGroup itemGroup = new ItemGroup(ModCyclic.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(BlockRegistry.trash);
    }
  };

  public static class ArmorMats {

    private static final String EMERALDID = ModCyclic.MODID + ":emerald";
    private static final String CRYSTALID = ModCyclic.MODID + ":crystal";
    public static final IArmorMaterial EMERALD = new IArmorMaterial() {

      @Override
      public int getDurability(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDurability(slotIn) + ArmorMaterial.IRON.getDurability(slotIn);
      }

      @Override
      public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDamageReductionAmount(slotIn) + ArmorMaterial.IRON.getDamageReductionAmount(slotIn);
      }

      @Override
      public int getEnchantability() {
        return ArmorMaterial.GOLD.getEnchantability();
      }

      @Override
      public SoundEvent getSoundEvent() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(net.minecraft.item.Items.EMERALD));
      }

      @Override
      public String getName() {
        return EMERALDID;
      }

      @Override
      public float getToughness() {
        return ArmorMaterial.DIAMOND.getToughness() * 1.5F;
      }

      @Override
      public float getKnockbackResistance() {
        return ArmorMaterial.DIAMOND.getKnockbackResistance();
      }
    };
    public static final IArmorMaterial GEMOBSIDIAN = new IArmorMaterial() {

      @Override
      public int getDurability(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDurability(slotIn) * 4;
      }

      @Override
      public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return ArmorMaterial.DIAMOND.getDamageReductionAmount(slotIn) * 3;
      }

      @Override
      public int getEnchantability() {
        return ArmorMaterial.GOLD.getEnchantability() + 3;
      }

      @Override
      public SoundEvent getSoundEvent() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(ItemRegistry.gem_obsidian));
      }

      @Override
      public String getName() {
        return CRYSTALID;
      }

      @Override
      public float getToughness() {
        return ArmorMaterial.DIAMOND.getToughness() * 3F;
      }

      @Override
      public float getKnockbackResistance() {
        // knockback
        return ArmorMaterial.NETHERITE.getKnockbackResistance();
      }
    };
  }

  public static class ToolMats {

    public static final IItemTier GEMOBSIDIAN = new IItemTier() {

      @Override
      public int getMaxUses() {
        return ItemTier.DIAMOND.getMaxUses() * 4;
      }

      @Override
      public float getEfficiency() {
        return ItemTier.DIAMOND.getEfficiency() * 4;
      }

      @Override
      public float getAttackDamage() {
        return ItemTier.DIAMOND.getAttackDamage() * 3.5F;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.DIAMOND.getHarvestLevel() + 1;
      }

      @Override
      public int getEnchantability() {
        return ItemTier.GOLD.getEnchantability() + 1;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(ItemRegistry.gem_obsidian));
      }
    };
    public static final IItemTier EMERALD = new IItemTier() {

      @Override
      public int getMaxUses() {
        return ItemTier.DIAMOND.getMaxUses() + ItemTier.GOLD.getMaxUses();
      }

      @Override
      public float getEfficiency() {
        return ItemTier.DIAMOND.getEfficiency() * 2;
      }

      @Override
      public float getAttackDamage() {
        return ItemTier.DIAMOND.getAttackDamage() * 1.5F;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.DIAMOND.getHarvestLevel();
      }

      @Override
      public int getEnchantability() {
        return ItemTier.GOLD.getEnchantability() + 1;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(net.minecraft.item.Items.EMERALD));
      }
    };
    public static final IItemTier SANDSTONE = new IItemTier() {

      @Override
      public int getMaxUses() {
        return ItemTier.STONE.getMaxUses() - 2;
      }

      @Override
      public float getEfficiency() {
        return ItemTier.STONE.getEfficiency();
      }

      @Override
      public float getAttackDamage() {
        return (ItemTier.WOOD.getAttackDamage() + ItemTier.STONE.getAttackDamage()) / 2;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.STONE.getHarvestLevel();
      }

      @Override
      public int getEnchantability() {
        return (ItemTier.WOOD.getEnchantability() + ItemTier.STONE.getEnchantability()) / 2;
      }

      @Override
      public Ingredient getRepairMaterial() {
        //        Ingredient.fromTag(Tag.)
        return Ingredient.fromStacks(new ItemStack(net.minecraft.block.Blocks.SANDSTONE));
      }
    };
    public static final IItemTier NETHERBRICK = new IItemTier() {

      @Override
      public int getMaxUses() {
        return (ItemTier.IRON.getMaxUses() + ItemTier.STONE.getMaxUses()) / 2;
      }

      @Override
      public float getEfficiency() {
        return (ItemTier.IRON.getEfficiency() + ItemTier.STONE.getEfficiency()) / 2;
      }

      @Override
      public float getAttackDamage() {
        return (ItemTier.IRON.getAttackDamage() + ItemTier.STONE.getAttackDamage()) / 2;
      }

      @Override
      public int getHarvestLevel() {
        return ItemTier.IRON.getHarvestLevel();
      }

      @Override
      public int getEnchantability() {
        return (ItemTier.IRON.getEnchantability() + ItemTier.STONE.getEnchantability()) / 2;
      }

      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromStacks(new ItemStack(net.minecraft.block.Blocks.NETHER_BRICKS));
      }
    };
  }
}
