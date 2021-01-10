package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;

public class DataTags {

  public static final INamedTag<Block> PLANTS = BlockTags.makeWrapperTag("forge:plants");
  public static final INamedTag<Block> MUSHROOMS = BlockTags.makeWrapperTag("forge:mushrooms");
  public static final INamedTag<Block> VINES = BlockTags.makeWrapperTag("forge:vines");
  public static final INamedTag<Block> CROPBLOCKS = BlockTags.makeWrapperTag("forge:crop_blocks");
  public static final INamedTag<Item> RODS = ItemTags.makeWrapperTag("forge:fishing_rods");
  public static final INamedTag<Item> IMMUNE = ItemTags.makeWrapperTag(ModCyclic.MODID + ":anvil_immune");
}
