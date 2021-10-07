package com.lothrazar.cyclic.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class DataTags {

  public static final Named<Fluid> EXPERIENCE = FluidTags.bind("forge:experience");
  public static final Named<Block> PLANTS = BlockTags.bind("forge:plants");
  public static final Named<Block> MUSHROOMS = BlockTags.bind("forge:mushrooms");
  public static final Named<Block> VINES = BlockTags.bind("forge:vines");
  public static final Named<Block> CACTUS = BlockTags.bind("forge:cactus");
  public static final Named<Block> CROP_BLOCKS = BlockTags.bind("forge:crop_blocks");
  public static final Named<Item> FISHING_RODS = ItemTags.bind("forge:fishing_rods");
  public static final Named<Item> BOOKS = ItemTags.bind("forge:books");
  public static final Named<Item> ANVIL_IMMUNE = ItemTags.bind("cyclic:anvil_immune");
  public static final Named<Item> DISENCHANTER_IMMUNE = ItemTags.bind("cyclic:disenchanter_immune");
  public static final Named<Item> WRENCH = ItemTags.createOptional(new ResourceLocation("forge", "tools/wrench"));
}
