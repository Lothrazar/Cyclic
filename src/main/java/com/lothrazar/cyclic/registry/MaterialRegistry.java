package com.lothrazar.cyclic.registry;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class MaterialRegistry {

  public static final CreativeModeTab BLOCK_GROUP = new CreativeModeTab(ModCyclic.MODID) {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(BlockRegistry.TRASH.get());
    }
  };
  public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(ModCyclic.MODID + "items") {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ItemRegistry.GEM_AMBER.get());
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
        return 0;
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
        return 0;
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
        return Ingredient.of(new ItemStack(ItemRegistry.GEM_OBSIDIAN.get()));
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
    };
  }

  public static class ToolMats {

    public static final Tier GEMOBSIDIAN = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.NETHERITE.getLevel(),
            Tiers.DIAMOND.getUses() * 4, Tiers.DIAMOND.getSpeed() * 4, OBS_DMG.get().floatValue(), Tiers.GOLD.getEnchantmentValue() + 1,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_obsidian_tool")),
            () -> Ingredient.of(ItemRegistry.GEM_OBSIDIAN.get())),
        new ResourceLocation(ModCyclic.MODID, "gem_obsidian"),
        List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
    //
    public static final Tier AMETHYST = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.IRON.getLevel(),
            Tiers.IRON.getUses() + 5, Tiers.IRON.getSpeed() + 0.2F, Tiers.IRON.getAttackDamageBonus() + 0.1F, Tiers.GOLD.getEnchantmentValue() * 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_amethyst_tool")),
            () -> Ingredient.of(Items.AMETHYST_SHARD)),
        new ResourceLocation(ModCyclic.MODID, "amethyst"),
        List.of(Tiers.STONE), List.of(Tiers.IRON));
    public static final Tier COPPER = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.IRON.getLevel(),
            (Tiers.STONE.getUses() + Tiers.IRON.getUses()) / 2, (Tiers.STONE.getSpeed() + Tiers.IRON.getSpeed()) / 2, (Tiers.STONE.getAttackDamageBonus() + Tiers.IRON.getAttackDamageBonus()) / 2, Tiers.DIAMOND.getEnchantmentValue() + 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_copper_tool")),
            () -> Ingredient.of(Items.COPPER_INGOT)),
        new ResourceLocation(ModCyclic.MODID, "copper"),
        List.of(Tiers.WOOD), List.of(Tiers.IRON));
    //
    //
    public static final Tier EMERALD = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.DIAMOND.getLevel(),
            Tiers.DIAMOND.getUses() + Tiers.GOLD.getUses(), Tiers.DIAMOND.getSpeed() * 2, EMERALD_DMG.get().floatValue(), Tiers.GOLD.getEnchantmentValue() + 1,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_emerald_tool")),
            () -> Ingredient.of(Items.EMERALD)),
        new ResourceLocation(ModCyclic.MODID, "emerald"),
        List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
    //
    public static final Tier SANDSTONE = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.STONE.getLevel(),
            Tiers.STONE.getUses() + 20, Tiers.STONE.getSpeed(), (Tiers.WOOD.getAttackDamageBonus() + Tiers.STONE.getAttackDamageBonus()) / 2, Tiers.IRON.getEnchantmentValue() + 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_sandstone_tool")),
            () -> Ingredient.of(Items.SANDSTONE)),
        new ResourceLocation(ModCyclic.MODID, "sandstone"),
        List.of(Tiers.WOOD), List.of(Tiers.STONE));
    //
    public static final Tier NETHERBRICK = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.IRON.getLevel(),
            (Tiers.IRON.getUses() + Tiers.GOLD.getUses()) / 2, (Tiers.IRON.getSpeed() + Tiers.GOLD.getSpeed()) / 2, (Tiers.IRON.getAttackDamageBonus() + Tiers.GOLD.getAttackDamageBonus()) / 2, Tiers.GOLD.getEnchantmentValue() + 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_nether_bricks_tool")),
            () -> Ingredient.of(Items.NETHER_BRICKS)),
        new ResourceLocation(ModCyclic.MODID, "nether_bricks"),
        List.of(Tiers.STONE), List.of(Tiers.DIAMOND));
  }
}
