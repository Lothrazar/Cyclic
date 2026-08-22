package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.DataTags;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;

public class MaterialRegistry {

  public static ModConfigSpec.IntValue EMERALD_BOOTS;
  public static ModConfigSpec.IntValue EMERALD_LEG;
  public static ModConfigSpec.IntValue EMERALD_CHEST;
  public static ModConfigSpec.IntValue EMERALD_HELM;
  public static ModConfigSpec.IntValue OBS_BOOTS;
  public static ModConfigSpec.IntValue OBS_LEG;
  public static ModConfigSpec.IntValue OBS_CHEST;
  public static ModConfigSpec.IntValue OBS_HELM;
  public static ModConfigSpec.DoubleValue EMERALD_TOUGH;
  public static ModConfigSpec.DoubleValue EMERALD_DMG;
  public static ModConfigSpec.DoubleValue OBS_TOUGH;
  public static ModConfigSpec.DoubleValue OBS_DMG;

  public static void setup() { // do not delete!!!
    Object a = ArmorMats.EMERALD;
    Object b = ToolMats.EMERALD;
  }

  private static ResourceKey<EquipmentAsset> equipmentAsset(String name) {
    return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(ModCyclic.MODID, name));
  }

  private static Map<ArmorType, Integer> defense(int boots, int legs, int chest, int helm, int body) {
    return Map.of(ArmorType.BOOTS, boots, ArmorType.LEGGINGS, legs, ArmorType.CHESTPLATE, chest, ArmorType.HELMET, helm, ArmorType.BODY, body);
  }

  public static class ArmorMats {

    // durability multipliers below mirror vanilla tiers: leather=5, gold=7, iron/chain=15, diamond=33, netherite=37
    public static final ArmorMaterial EMERALD = new ArmorMaterial(
        33, // durability multiplier, was previously applied per-item as ArmorItem.Type.X.getDurability(33)
        defense(4, 7, 9, 4, 11),
        25, // enchantment value (from ArmorMaterials.GOLD)
        SoundRegistry.EQUIP_EMERALD,
        3.0F, // toughness
        (ArmorMaterials.DIAMOND.knockbackResistance() + ArmorMaterials.NETHERITE.knockbackResistance()) / 2.0F,
        DataTags.REPAIR_EMERALD,
        equipmentAsset("emerald"));

    public static final ArmorMaterial COPPER = new ArmorMaterial(
        10, // durability multiplier
        defense(2, 4, 5, 2, 4),
        15, // enchantment value (between iron=9 and gold=25)
        SoundRegistry.EQUIP_EMERALD,
        0.5F, // toughness (iron is 0)
        ArmorMaterials.IRON.knockbackResistance(),
        ItemTags.REPAIRS_COPPER_ARMOR, // vanilla tag reused: fits exactly
        equipmentAsset("copper"));

    public static final ArmorMaterial GEMOBSIDIAN = new ArmorMaterial(
        37, // durability multiplier
        defense(7, 10, 11, 7, 11),
        ArmorMaterials.GOLD.enchantmentValue() + 3,
        SoundRegistry.EQUIP_EMERALD,
        6.0F,
        ArmorMaterials.NETHERITE.knockbackResistance(),
        DataTags.REPAIR_GEM_OBSIDIAN,
        equipmentAsset("gem_obsidian"));

    public static final ArmorMaterial GLOWING = new ArmorMaterial(
        ArmorMaterials.IRON.durability(),
        ArmorMaterials.IRON.defense(),
        ArmorMaterials.IRON.enchantmentValue() + 1,
        SoundRegistry.EQUIP_EMERALD,
        ArmorMaterials.IRON.toughness(),
        ArmorMaterials.IRON.knockbackResistance(),
        DataTags.REPAIR_GLOWING,
        equipmentAsset("glowing"));
  }

  public static class ToolMats {
    public static final ToolMaterial NETHERBRICK = new ToolMaterial(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        (ToolMaterial.IRON.durability() + ToolMaterial.GOLD.durability()) / 2,
        (ToolMaterial.IRON.speed() + ToolMaterial.GOLD.speed()) / 2,
        (ToolMaterial.IRON.attackDamageBonus() + ToolMaterial.GOLD.attackDamageBonus()) / 2,
        ToolMaterial.GOLD.enchantmentValue() + 2,
        DataTags.REPAIR_NETHERBRICK);

    public static final ToolMaterial SANDSTONE = new ToolMaterial(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        ToolMaterial.STONE.durability() + 20, ToolMaterial.STONE.speed(),
        (ToolMaterial.WOOD.attackDamageBonus() + ToolMaterial.STONE.attackDamageBonus()) / 2,
        ToolMaterial.IRON.enchantmentValue() + 2,
        DataTags.REPAIR_SANDSTONE);

    public static final ToolMaterial COPPER = new ToolMaterial(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        (ToolMaterial.STONE.durability() + ToolMaterial.IRON.durability()) / 2,
        (ToolMaterial.STONE.speed() + ToolMaterial.IRON.speed()) / 2,
        (ToolMaterial.STONE.attackDamageBonus() + ToolMaterial.IRON.attackDamageBonus()) / 2,
        ToolMaterial.DIAMOND.enchantmentValue() + 2,
        ItemTags.COPPER_TOOL_MATERIALS); // vanilla tag reused: fits exactly

    public static final ToolMaterial AMETHYST = new ToolMaterial(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        ToolMaterial.IRON.durability() + 5, ToolMaterial.IRON.speed() + 0.2F,
        ToolMaterial.IRON.attackDamageBonus() + 0.1F, ToolMaterial.GOLD.enchantmentValue() * 2,
        DataTags.REPAIR_AMETHYST);

    public static final ToolMaterial EMERALD = new ToolMaterial(
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
        ToolMaterial.DIAMOND.durability() + ToolMaterial.GOLD.durability(), ToolMaterial.DIAMOND.speed() * 2,
        4.5f, // original used EMERALD_DMG config = 4.5F
        ToolMaterial.GOLD.enchantmentValue() + 1,
        DataTags.REPAIR_EMERALD);

    public static final ToolMaterial GEMOBSIDIAN = new ToolMaterial(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        ToolMaterial.DIAMOND.durability() * 4, ToolMaterial.DIAMOND.speed() * 4,
        10.5f, // original used OBS_DMG config = 10.5F
        ToolMaterial.GOLD.enchantmentValue() + 1,
        DataTags.REPAIR_GEM_OBSIDIAN);
  }
}
