package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
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
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

  public static class ArmorMats {

    private static final String EMERALDID = ModCyclic.MODID + ":emerald";
    private static final String CRYSTALID = ModCyclic.MODID + ":crystal";
    private static final String GLOWINGID = ModCyclic.MODID + ":glowing";
    public static final ArmorMaterial EMERALD = new ArmorMaterial() {

      @Override
      public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return (ArmorMaterials.DIAMOND.getDurabilityForSlot(slotIn) + ArmorMaterials.NETHERITE.getDurabilityForSlot(slotIn)) / 2;
      }

      @Override
      public int getDefenseForSlot(EquipmentSlot slot) {
        switch (slot) {
          case CHEST:
            return ArmorMaterials.DIAMOND.getDefenseForSlot(slot) - 2;
          case FEET:
            return ArmorMaterials.DIAMOND.getDefenseForSlot(slot) + 2;
          case HEAD:
            return ArmorMaterials.DIAMOND.getDefenseForSlot(slot) - 1;
          case LEGS:
            return ArmorMaterials.DIAMOND.getDefenseForSlot(slot) + 1;
          case MAINHAND:
          case OFFHAND:
          default:
            break;
        }
        return 0;
      }

      @Override
      public int getEnchantmentValue() {
        return ArmorMaterials.GOLD.getEnchantmentValue() * 4;
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
        return (ArmorMaterials.DIAMOND.getToughness() + ArmorMaterials.NETHERITE.getToughness()) / 2;
      }

      @Override
      public float getKnockbackResistance() {
        return (ArmorMaterials.DIAMOND.getKnockbackResistance() + ArmorMaterials.NETHERITE.getKnockbackResistance()) / 2;
      }
    };
    public static final ArmorMaterial GEMOBSIDIAN = new ArmorMaterial() {

      @Override
      public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return ArmorMaterials.NETHERITE.getEnchantmentValue() * 2;
      }

      @Override
      public int getDefenseForSlot(EquipmentSlot slot) {
        switch (slot) {
          case CHEST:
            return ArmorMaterials.NETHERITE.getDefenseForSlot(slot) - 1;
          case FEET:
            return ArmorMaterials.NETHERITE.getDefenseForSlot(slot) + 2;
          case HEAD:
            return ArmorMaterials.NETHERITE.getDefenseForSlot(slot) - 2;
          case LEGS:
            return ArmorMaterials.NETHERITE.getDefenseForSlot(slot) + 3;
          case MAINHAND:
          case OFFHAND:
          default:
            break;
        }
        return 0;
      }

      @Override
      public int getEnchantmentValue() {
        return ArmorMaterials.NETHERITE.getEnchantmentValue() + 2;
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
        return ArmorMaterials.NETHERITE.getToughness() * 1.5F;
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
            Tiers.DIAMOND.getUses() * 4, Tiers.DIAMOND.getSpeed() * 4, Tiers.DIAMOND.getAttackDamageBonus() + 1, (Tiers.DIAMOND.getEnchantmentValue() + Tiers.GOLD.getEnchantmentValue()) / 2,
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
        new ForgeTier(Tiers.NETHERITE.getLevel(),
            (Tiers.DIAMOND.getUses() + Tiers.NETHERITE.getUses()) / 2, (Tiers.DIAMOND.getSpeed() + Tiers.IRON.getSpeed()) / 2, (Tiers.DIAMOND.getAttackDamageBonus() + Tiers.IRON.getAttackDamageBonus()) / 2, Tiers.DIAMOND.getEnchantmentValue() + 2,
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
