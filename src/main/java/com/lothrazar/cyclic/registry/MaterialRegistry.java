package com.lothrazar.cyclic.registry;

import java.util.List;
import java.util.EnumMap;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.Util;
import net.minecraft.world.item.ArmorMaterials;

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

  public static void setup() {
    Object a = ArmorMats.EMERALD;
    Object b = ToolMats.EMERALD;
  }

  public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, ModCyclic.MODID);

  public static class ArmorMats {
    public static final Holder<ArmorMaterial> EMERALD = ARMOR_MATERIALS.register("emerald", () -> new ArmorMaterial(
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 4);
            map.put(ArmorItem.Type.LEGGINGS, 7);
            map.put(ArmorItem.Type.CHESTPLATE, 9);
            map.put(ArmorItem.Type.HELMET, 4);
            map.put(ArmorItem.Type.BODY, 11);
        }),
        25, // enchantment value (from ArmorMaterials.GOLD)
        SoundRegistry.EQUIP_EMERALD,
        () -> Ingredient.of(Items.EMERALD),
        List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "emerald"))),
        3.0F, // toughness
        (ArmorMaterials.DIAMOND.value().knockbackResistance() + ArmorMaterials.NETHERITE.value().knockbackResistance()) / 2.0F
    ));

    public static final Holder<ArmorMaterial> GEMOBSIDIAN = ARMOR_MATERIALS.register("gem_obsidian", () -> new ArmorMaterial(
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 7);
            map.put(ArmorItem.Type.LEGGINGS, 10); // Diamond is 6, config says boots 7 helm 7 chest 11, let's assume legs 10
            map.put(ArmorItem.Type.CHESTPLATE, 11);
            map.put(ArmorItem.Type.HELMET, 7);
            map.put(ArmorItem.Type.BODY, 11);
        }),
        25, // enchantment value (from ArmorMaterials.GOLD)
        SoundEvents.ARMOR_EQUIP_DIAMOND,
        () -> Ingredient.of(ItemRegistry.GEM_OBSIDIAN.get()),
        List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "gem_obsidian"))),
        6.0F, // toughness
        0.2F  // knockback resistance (Netherite is 0.1)
    ));

    public static final Holder<ArmorMaterial> GLOWING = ARMOR_MATERIALS.register("glowing", () -> new ArmorMaterial(
        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, 3);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.CHESTPLATE, 8);
            map.put(ArmorItem.Type.HELMET, 3);
            map.put(ArmorItem.Type.BODY, 11);
        }),
        15, // enchantment value
        SoundEvents.ARMOR_EQUIP_DIAMOND,
        () -> Ingredient.of(ItemRegistry.GEM_AMBER.get()), // Or glowing material
        List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "glowing"))),
        2.0F, // toughness
        0.0F  // knockback resistance
    ));
  }

  public static class ToolMats {
    public static final Tier NETHERBRICK = new SimpleTier(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        (Tiers.IRON.getUses() + Tiers.GOLD.getUses()) / 2,
        (Tiers.IRON.getSpeed() + Tiers.GOLD.getSpeed()) / 2,
        (Tiers.IRON.getAttackDamageBonus() + Tiers.GOLD.getAttackDamageBonus()) / 2,
        Tiers.GOLD.getEnchantmentValue() + 2,
        () -> Ingredient.of(Items.NETHER_BRICKS)
    );

    public static final Tier SANDSTONE = new SimpleTier(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        Tiers.STONE.getUses() + 20, Tiers.STONE.getSpeed(),
        (Tiers.WOOD.getAttackDamageBonus() + Tiers.STONE.getAttackDamageBonus()) / 2,
        Tiers.IRON.getEnchantmentValue() + 2,
        () -> Ingredient.of(Items.SANDSTONE)
    );

    public static final Tier COPPER = new SimpleTier(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        (Tiers.STONE.getUses() + Tiers.IRON.getUses()) / 2, 
        (Tiers.STONE.getSpeed() + Tiers.IRON.getSpeed()) / 2,
        (Tiers.STONE.getAttackDamageBonus() + Tiers.IRON.getAttackDamageBonus()) / 2,
        Tiers.DIAMOND.getEnchantmentValue() + 2,
        () -> Ingredient.of(Items.COPPER_INGOT)
    );

    public static final Tier AMETHYST = new SimpleTier(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        Tiers.IRON.getUses() + 5, Tiers.IRON.getSpeed() + 0.2F, 
        Tiers.IRON.getAttackDamageBonus() + 0.1F, Tiers.GOLD.getEnchantmentValue() * 2,
        () -> Ingredient.of(Items.AMETHYST_SHARD)
    );

    public static final Tier EMERALD = new SimpleTier(
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
        Tiers.DIAMOND.getUses() + Tiers.GOLD.getUses(), Tiers.DIAMOND.getSpeed() * 2, 
        4.5f, // original used EMERALD_DMG config = 4.5F
        Tiers.GOLD.getEnchantmentValue() + 1,
        () -> Ingredient.of(Items.EMERALD)
    );

    public static final Tier GEMOBSIDIAN = new SimpleTier(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        Tiers.DIAMOND.getUses() * 4, Tiers.DIAMOND.getSpeed() * 4, 
        10.5f, // original used OBS_DMG config = 10.5F
        Tiers.GOLD.getEnchantmentValue() + 1,
        () -> Ingredient.of(ItemRegistry.GEM_OBSIDIAN.get())
    );
  }
}
