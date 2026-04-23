package com.lothrazar.cyclic.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class DataTags {

  public static final TagKey<Fluid> EXPERIENCE = FluidTags.create(ResourceLocation.parse("forge:experience"));
  public static final TagKey<Fluid> HONEY = FluidTags.create(ResourceLocation.parse("forge:honey"));
  public static final TagKey<Fluid> BIOMASS = FluidTags.create(ResourceLocation.parse("forge:biomass"));
  public static final TagKey<Fluid> MAGMA = FluidTags.create(ResourceLocation.parse("forge:magma"));
  public static final TagKey<Fluid> SLIME = FluidTags.create(ResourceLocation.parse("forge:slime"));
  public static final TagKey<Block> WITH_MATTOCK = BlockTags.create(ResourceLocation.parse("minecraft:mineable/mattock"));
  public static final TagKey<Block> GLASS_DARK = BlockTags.create(ResourceLocation.parse("forge:glass/dark"));
  public static final TagKey<Block> PLANTS = BlockTags.create(ResourceLocation.parse("forge:plants"));
  public static final TagKey<Block> MUSHROOMS = BlockTags.create(ResourceLocation.parse("forge:mushrooms"));
  public static final TagKey<Block> VINES = BlockTags.create(ResourceLocation.parse("forge:vines"));
  public static final TagKey<Block> CACTUS = BlockTags.create(ResourceLocation.parse("forge:cactus"));
  public static final TagKey<Block> BREAKER_IGNORED = BlockTags.create(ResourceLocation.parse("cyclic:ignored/breaker"));
  public static final TagKey<Block> MINER_IGNORED = BlockTags.create(ResourceLocation.parse("cyclic:ignored/miner"));
  public static final TagKey<Block> CROP_BLOCKS = BlockTags.create(ResourceLocation.parse("forge:crop_blocks"));
  public static final TagKey<Item> FISHING_RODS = ItemTags.create(ResourceLocation.parse("forge:fishing_rods"));
  public static final TagKey<Item> GLASS_DARKI = ItemTags.create(ResourceLocation.parse("forge:glass/dark"));
  public static final TagKey<Item> BOOKS = ItemTags.create(ResourceLocation.parse("forge:books"));
  public static final TagKey<Item> ANVIL_IMMUNE = ItemTags.create(ResourceLocation.parse("cyclic:anvil_immune"));
  public static final TagKey<Item> DISENCHANTER_IMMUNE = ItemTags.create(ResourceLocation.parse("cyclic:disenchanter_immune"));
  public static final TagKey<Item> COPPER_ORE = ItemTags.create(ResourceLocation.parse("forge:ores/copper"));
  public static final TagKey<Item> COPPER_INGOTS = ItemTags.create(ResourceLocation.parse("forge:ingots/copper"));
  public static final TagKey<Item> WRENCH = ItemTags.create(ResourceLocation.parse("forge:tools/wrench"));
  public static final TagKey<Item> IPLANTS = ItemTags.create(ResourceLocation.parse("forge:plants"));
  public static final TagKey<Item> IMUSHROOMS = ItemTags.create(ResourceLocation.parse("forge:mushrooms"));
  public static final TagKey<Item> IVINES = ItemTags.create(ResourceLocation.parse("forge:vines"));
  public static final TagKey<Item> ICACTUS = ItemTags.create(ResourceLocation.parse("forge:cactus"));
  public static final TagKey<Block> EXCAVATE_IGNORED = BlockTags.create(ResourceLocation.parse("cyclic:ignored/excavate"));

  public static void setup() {
    // do not delete:! this makes the mod get classloaded so the wrapper tags correctly get added to the registry early, before recipe testing
  }
}
