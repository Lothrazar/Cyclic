package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import com.lothrazar.cyclicmagic.block.BlockBuilder;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.BlockHarvester;
import com.lothrazar.cyclicmagic.block.BlockMagnet;
import com.lothrazar.cyclicmagic.block.BlockMiner;
import com.lothrazar.cyclicmagic.block.BlockPlacer;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {
  public static ArrayList<Block> blocks = new ArrayList<Block>();
  public static BlockScaffolding block_fragile;
  public static BlockUncrafting uncrafting_block;
  public static BlockBucketStorage block_storelava;
  public static BlockBucketStorage block_storewater;
  public static BlockBucketStorage block_storemilk;
  public static BlockBucketStorage block_storeempty;
  public static BlockDimensionOre nether_gold_ore;
  public static BlockDimensionOre nether_coal_ore;
  public static BlockDimensionOre nether_lapis_ore;
  public static BlockDimensionOre nether_emerald_ore;
  public static BlockDimensionOre end_redstone_ore;
  public static BlockDimensionOre end_coal_ore;
  public static BlockDimensionOre end_lapis_ore;
  public static BlockDimensionOre end_emerald_ore;
  public static BlockDimensionOre nether_diamond_ore;
  public static BlockDimensionOre end_diamond_ore;
  public static BlockBuilder builder_block;
  public static BlockHarvester harvester_block;
  public static BlockMagnet magnet_block;
  public static BlockMiner miner_block;
  public static BlockMiner block_miner_tunnel;
  public static BlockPlacer placer_block;
  //lots of helpers/overrides with defaults
  public static void registerBlock(Block b, String name) {
    registerBlock(b, name, false);
  }
  public static void registerBlock(Block b, String name, boolean isHidden) {
    registerBlock(b, new ItemBlock(b), name, isHidden);
  }
  public static void registerBlock(Block b, ItemBlock ib, String name) {
    registerBlock(b, ib, name, false);
  }
  public static void registerBlock(Block b, ItemBlock ib, String name, boolean isHidden) {
    b.setRegistryName(name);
    b.setUnlocalizedName(name);
    GameRegistry.register(b);
    ib.setRegistryName(b.getRegistryName());
    GameRegistry.register(ib);
    if (isHidden == false) {
      b.setCreativeTab(ModMain.TAB);
    }
    blocks.add(b);
  }
}
