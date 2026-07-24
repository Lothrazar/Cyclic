package com.lothrazar.cyclic.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class DataTags {

  public static final TagKey<Fluid> EXPERIENCE = FluidTags.create(Identifier.parse("c:experience"));
  public static final TagKey<Fluid> HONEY = FluidTags.create(Identifier.parse("c:honey"));
  public static final TagKey<Fluid> BIOMASS = FluidTags.create(Identifier.parse("c:biomass"));
  public static final TagKey<Fluid> MAGMA = FluidTags.create(Identifier.parse("c:magma"));
  public static final TagKey<Fluid> SLIME = FluidTags.create(Identifier.parse("c:slime"));
  public static final TagKey<Block> WITH_MATTOCK = BlockTags.create(Identifier.parse("minecraft:mineable/mattock"));
  public static final TagKey<Block> GLASS_DARK = BlockTags.create(Identifier.parse("c:glass/dark"));
  public static final TagKey<Block> PLANTS = BlockTags.create(Identifier.parse("c:plants"));
  public static final TagKey<Block> MUSHROOMS = BlockTags.create(Identifier.parse("c:mushrooms"));
  public static final TagKey<Block> VINES = BlockTags.create(Identifier.parse("c:vines"));
  public static final TagKey<Block> CACTUS = BlockTags.create(Identifier.parse("c:cactus"));
  public static final TagKey<Block> BREAKER_IGNORED = BlockTags.create(Identifier.parse("cyclic:ignored/breaker"));
  public static final TagKey<Block> MINER_IGNORED = BlockTags.create(Identifier.parse("cyclic:ignored/miner"));
  public static final TagKey<Block> CROP_BLOCKS = BlockTags.create(Identifier.parse("c:crop_blocks"));
  public static final TagKey<Item> FISHING_RODS = ItemTags.create(Identifier.parse("c:fishing_rods"));
  public static final TagKey<Item> GLASS_DARKI = ItemTags.create(Identifier.parse("c:glass/dark"));
  public static final TagKey<Item> BOOKS = ItemTags.create(Identifier.parse("c:books"));
  public static final TagKey<Item> ANVIL_IMMUNE = ItemTags.create(Identifier.parse("cyclic:anvil_immune"));
  public static final TagKey<Item> DISENCHANTER_IMMUNE = ItemTags.create(Identifier.parse("cyclic:disenchanter_immune"));
  public static final TagKey<Item> COPPER_ORE = ItemTags.create(Identifier.parse("c:ores/copper"));
  public static final TagKey<Item> COPPER_INGOTS = ItemTags.create(Identifier.parse("c:ingots/copper"));
  public static final TagKey<Item> WRENCH = ItemTags.create(Identifier.parse("c:tools/wrench"));
  public static final TagKey<Item> IPLANTS = ItemTags.create(Identifier.parse("c:plants"));
  public static final TagKey<Item> IMUSHROOMS = ItemTags.create(Identifier.parse("c:mushrooms"));
  public static final TagKey<Item> IVINES = ItemTags.create(Identifier.parse("c:vines"));
  public static final TagKey<Item> ICACTUS = ItemTags.create(Identifier.parse("c:cactus"));
  public static final TagKey<Block> EXCAVATE_IGNORED = BlockTags.create(Identifier.parse("cyclic:ignored/excavate"));
  public static final TagKey<EntityType<?>> MAGICNET_BLOCKED = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("cyclic:magicnet_blocked"));
  // repair item tags for custom ArmorMaterial/ToolMaterial (no vanilla equivalent exists for these materials)
  public static final TagKey<Item> REPAIR_EMERALD = ItemTags.create(Identifier.parse("cyclic:repair/emerald"));
  public static final TagKey<Item> REPAIR_GEM_OBSIDIAN = ItemTags.create(Identifier.parse("cyclic:repair/gem_obsidian"));
  public static final TagKey<Item> REPAIR_GLOWING = ItemTags.create(Identifier.parse("cyclic:repair/glowing"));
  public static final TagKey<Item> REPAIR_NETHERBRICK = ItemTags.create(Identifier.parse("cyclic:repair/netherbrick"));
  public static final TagKey<Item> REPAIR_SANDSTONE = ItemTags.create(Identifier.parse("cyclic:repair/sandstone"));
  public static final TagKey<Item> REPAIR_AMETHYST = ItemTags.create(Identifier.parse("cyclic:repair/amethyst"));

  public static void setup() {
    // do not delete:! this makes the mod get classloaded so the wrapper tags correctly get added to the registry early, before recipe testing
  }
}
