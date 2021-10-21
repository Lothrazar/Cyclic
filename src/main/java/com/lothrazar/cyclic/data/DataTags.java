package com.lothrazar.cyclic.data;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class DataTags {

  public static final INamedTag<Fluid> EXPERIENCE = FluidTags.makeWrapperTag("forge:experience");
  public static final INamedTag<Fluid> HONEY = FluidTags.makeWrapperTag("forge:honey");
  public static final INamedTag<Fluid> BIOMASS = FluidTags.makeWrapperTag("forge:biomass");
  public static final INamedTag<Fluid> MAGMA = FluidTags.makeWrapperTag("forge:magma");
  public static final INamedTag<Fluid> SLIME = FluidTags.makeWrapperTag("forge:slime");
  public static final INamedTag<Block> PLANTS = BlockTags.makeWrapperTag("forge:plants");
  public static final INamedTag<Block> MUSHROOMS = BlockTags.makeWrapperTag("forge:mushrooms");
  public static final INamedTag<Block> VINES = BlockTags.makeWrapperTag("forge:vines");
  public static final INamedTag<Block> CACTUS = BlockTags.makeWrapperTag("forge:cactus");
  public static final INamedTag<Block> CROP_BLOCKS = BlockTags.makeWrapperTag("forge:crop_blocks");
  public static final INamedTag<Item> FISHING_RODS = ItemTags.makeWrapperTag("forge:fishing_rods");
  public static final INamedTag<Item> BOOKS = ItemTags.makeWrapperTag("forge:books");
  public static final INamedTag<Item> ANVIL_IMMUNE = ItemTags.makeWrapperTag("cyclic:anvil_immune");
  public static final INamedTag<Item> DISENCHANTER_IMMUNE = ItemTags.makeWrapperTag("cyclic:disenchanter_immune");
  public static final INamedTag<Item> WRENCH = ItemTags.createOptional(new ResourceLocation("forge", "tools/wrench"));

  public static void setup() {
    // do not delete:! this makes the mod get classloaded so the wrapper tags correctly get added to the registry early, before recipe testing
  }
}
