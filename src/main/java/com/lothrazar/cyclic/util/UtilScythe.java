/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.item.scythe.ScytheType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilScythe {

  private static final INamedTag<Block> SBRUSH = BlockTags.makeWrapperTag(ScytheType.BRUSH.type().toString());
  private static final INamedTag<Block> SLEAVES = BlockTags.makeWrapperTag(ScytheType.LEAVES.type().toString());
  private static final INamedTag<Block> SFORAGE = BlockTags.makeWrapperTag(ScytheType.FORAGE.type().toString());
  //  private static final BlockTags.Wrapper HARVESTABLE = new BlockTags.Wrapper(new ResourceLocation("cyclic", "harvest"));

  //  private static class ScytheConfig {
  //
  //    NonNullList<String> blockWhitelist;
  //    NonNullList<String> oreDictWhitelist;
  //  }
  //  public static void syncConfig(Configuration config) {
  //    String category = Const.ConfigCategory.modpackMisc;
  //    weedsClassCheck = config.getBoolean("tool_harvest_weeds.class_defaults", category, true, "If true this will try an auto-detect blocks that are a weed/bush/tallgrass type by class type, which "
  //        + "means harvesting many blocks that are not explicitly in the whitelist.  This is useful for handling vanilla and most modded blocks.  If you want more precise control you can turn this false "
  //        + "which means ONLY the whitelists will be harvested, so you would have to add every block yourself. ");
  //    leavesClassCheck = config.getBoolean("tool_harvest_leaves.class_defaults", category, true, "If true this will try an auto-detect blocks that are a leaf-type by class type, which "
  //        + "means harvesting many blocks that are not explicitly in the whitelist.  This is useful for handling vanilla and most modded blocks.  If you want more precise control you can turn this false "
  //        + "which means ONLY the whitelists will be harvested, so you would have to add every block yourself. ");
  //    blacklistAll = NonNullList.from("", config.getStringList("ScytheBlacklist", category, new String[] {
  //        "terraqueous:pergola", "harvestcraft:*_sapling"
  //    }, "Crops & leaves that are blocked from harvesting (Brush Scythe and Tree Scythe). A star is for a wildcard "));
  //    leafConfig.blockWhitelist = NonNullList.from("", config.getStringList("tool_harvest_leaves.whitelist", category, new String[] {
//        /* @formatter:off */
//                "extratrees:leaves.decorative.0"
//               , "extratrees:leaves.decorative.1"
//               , "extratrees:leaves.decorative.2"
//               , "extratrees:leaves.decorative.3"
//               , "extratrees:leaves.decorative.4"
//               , "extratrees:leaves.decorative.5"
//               , "forestry:leaves.decorative.0"
//               , "forestry:leaves.decorative.1"
//               , "terraqueous:foliage3:5"
//               , "plants2:nether_leaves"
//               , "plants2:crystal_leaves"
//               , "plants2:leaves_0"
//               , "plants2:bush"
//               /* @formatter:on */
  //    }, "Blocks that the Tree Scythe will attempt to harvest as if they are leaves.  A star is for a wildcard "));
  //    leafConfig.oreDictWhitelist = NonNullList.from("", config.getStringList("tool_harvest_leaves.whitelist_oredict", category, new String[] {
  //        "treeLeaves"
  //    }, "Ore dictionary entries that the Tree Scythe will attempt to harvest as if they are leaves.   "));
  //    brushConfig.oreDictWhitelist = NonNullList.from("", config.getStringList("tool_harvest_weeds.whitelist_oredict", category, new String[] {
  //        "vine", "plant", "flowerYellow", "stickWood" }, "Ore dictionary entries that the Brush Scythe will attempt to harvest as if they are leaves.  "));
  //    brushConfig.blockWhitelist = NonNullList.from("", config.getStringList("tool_harvest_weeds.whitelist", category, new String[] {
//        /* @formatter:off */
//        "plants2:cosmetic_0"
//        ,"plants2:cosmetic_1"
//        ,"plants2:cosmetic_2"
//        ,"plants2:cosmetic_3"
//        ,"plants2:cosmetic_4"
//        ,"plants2:desert_0"
//        ,"plants2:desert_1"
//        ,"plants2:double_0"
//        ,"plants2:cataplant"
//        ,"botany:flower"
//        ,"biomesoplenty:bamboo"
//        ,"biomesoplenty:flower_0"
//        ,"biomesoplenty:flower_1"
//        ,"biomesoplenty:plant_0"
//        ,"biomesoplenty:plant_1"
//        ,"biomesoplenty:mushroom"
//        ,"biomesoplenty:doubleplant"
//        ,"biomesoplenty:flower_vine"
//        ,"biomesoplenty:ivy"
//        ,"biomesoplenty:tree_moss"
//        ,"biomesoplenty:willow_vine"
//        ,"croparia:fruit_grass"
//        ,"plants2:androsace_a"
//        ,"plants2:akebia_q_vine"
//        ,"plants2:ampelopsis_a_vine"
//        ,"plants2:adlumia_f"
//        ,"abyssalcraft:wastelandsthorn"
//        ,"abyssalcraft:luminousthistle"
//        ,"harvestcraft:garden"
//        ,"harvestcraft:windygarden"
//        ,"minecraft:double_plant"
//        ,"minecraft:red_flower"
//        ,"minecraft:yellow_flower"
//        ,"minecraft:brown_mushroom"
//        ,"minecraft:red_mushroom"
//        ,"ferdinandsflowers:block_cff_*"
//        ,"extraplanets:*_flowers"
//        ,"primal:cineris_grass"
//        ,"primal:cineris_bloom"
//        ,"primal:sinuous_weed"
//        ,"primal:dry_grass_root"
//        ,"primal:nether_root"
//        ,"primal:corypha_stalk"
//        ,"twilightforest:*_plant"
//        ,"tconstruct:*_grass_tall"
//        ,"thebetweenlands:*_flower"
//        ,"thebetweenlands:*_tallgrass"
//        ,"thebetweenlands:*_stalk"
//        ,"thebetweenlands:moss"
//        ,"thebetweenlands:cattail"
//        ,"thebetweenlands:*_cattail"
//        ,"thebetweenlands:*_plant"
//        ,"thebetweenlands:*_coral"
//        ,"thebetweenlands:*_bush"
//        ,"thebetweenlands:*_ivy"
//        ,"thebetweenlands:algae"
//        ,"thebetweenlands:hanger"
//        ,"thebetweenlands:nettle"
//        ,"thebetweenlands:*_iris"
//        ,"thebetweenlands:*_kelp"
//        ,"thebetweenlands:fallen_leaves"
//        ,"thebetweenlands:swamp_reed_*"
//        ,"thebetweenlands:*_mushroom"
//        ,"natura:*_vines"
//        ,"nex:plant_thornstalk"
//        /* @formatter:on */
  //    }, "Blocks that the Brush Scythe will attempt to harvest as if they are leaves.  A star is for a wildcard "));
  //  }
  public static boolean harvestSingle(World world, PlayerEntity player, BlockPos posCurrent, ScytheType type) {
    boolean doBreak = false;
    BlockState blockState = world.getBlockState(posCurrent);
    switch (type) {
      case LEAVES:
        doBreak = blockState.isIn(SLEAVES);
      break;
      case BRUSH:
        doBreak = blockState.isIn(SBRUSH);
      break;
      case FORAGE:
        doBreak = blockState.isIn(SFORAGE);
      break;
    }
    if (doBreak) {
      //harvest block with player context: better mod compatibility
      blockState.getBlock().harvestBlock(world, player, posCurrent, blockState, world.getTileEntity(posCurrent), player.getHeldItemMainhand());
      //sometimes this doesnt work and/or doesnt sync ot client, so force it
      world.destroyBlock(posCurrent, false);
      //break with false to disable dropsfor the above versions, dont want to dupe tallflowers
      return true;
    }
    return false;
  }
}
