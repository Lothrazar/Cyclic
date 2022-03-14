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

  public static final TagKey<Fluid> EXPERIENCE = FluidTags.create(new ResourceLocation("forge:experience"));
  public static final TagKey<Fluid> HONEY = FluidTags.create(new ResourceLocation("forge:honey"));
  public static final TagKey<Fluid> BIOMASS = FluidTags.create(new ResourceLocation("forge:biomass"));
  public static final TagKey<Fluid> MAGMA = FluidTags.create(new ResourceLocation("forge:magma"));
  public static final TagKey<Fluid> SLIME = FluidTags.create(new ResourceLocation("forge:slime"));
  public static final TagKey<Block> WITH_MATTOCK = BlockTags.create(new ResourceLocation("minecraft:mineable/mattock"));
  public static final TagKey<Block> GLASS_DARK = BlockTags.create(new ResourceLocation("forge:glass/dark"));
  public static final TagKey<Block> PLANTS = BlockTags.create(new ResourceLocation("forge:plants"));
  public static final TagKey<Block> MUSHROOMS = BlockTags.create(new ResourceLocation("forge:mushrooms"));
  public static final TagKey<Block> VINES = BlockTags.create(new ResourceLocation("forge:vines"));
  public static final TagKey<Block> CACTUS = BlockTags.create(new ResourceLocation("forge:cactus"));
  public static final TagKey<Block> CROP_BLOCKS = BlockTags.create(new ResourceLocation("forge:crop_blocks"));
  public static final TagKey<Item> FISHING_RODS = ItemTags.create(new ResourceLocation("forge:fishing_rods"));
  public static final TagKey<Item> GLASS_DARKI = ItemTags.create(new ResourceLocation("forge:glass/dark"));
  public static final TagKey<Item> BOOKS = ItemTags.create(new ResourceLocation("forge:books"));
  public static final TagKey<Item> ANVIL_IMMUNE = ItemTags.create(new ResourceLocation("cyclic:anvil_immune"));
  public static final TagKey<Item> DISENCHANTER_IMMUNE = ItemTags.create(new ResourceLocation("cyclic:disenchanter_immune"));
  public static final TagKey<Item> COPPER_ORE = ItemTags.create(new ResourceLocation("forge:ores/copper"));
  public static final TagKey<Item> COPPER_INGOTS = ItemTags.create(new ResourceLocation("forge:ingots/copper"));
  public static final TagKey<Item> WRENCH = ItemTags.create(new ResourceLocation("forge:tools/wrench"));
  public static final TagKey<Item> IPLANTS = ItemTags.create(new ResourceLocation("forge:plants"));
  public static final TagKey<Item> IMUSHROOMS = ItemTags.create(new ResourceLocation("forge:mushrooms"));
  public static final TagKey<Item> IVINES = ItemTags.create(new ResourceLocation("forge:vines"));
  public static final TagKey<Item> ICACTUS = ItemTags.create(new ResourceLocation("forge:cactus"));

  public static void setup() {
    // do not delete:! this makes the mod get classloaded so the wrapper tags correctly get added to the registry early, before recipe testing
  }
}
