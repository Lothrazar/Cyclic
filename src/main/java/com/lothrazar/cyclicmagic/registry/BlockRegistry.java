package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {
  public static ArrayList<Block> blocks = new ArrayList<Block>();
  public static BlockBucketStorage block_storelava;//TODO: FIX THIS ENTIRE THING.. maybe even fliud registry eh
  public static BlockBucketStorage block_storewater;
  public static BlockBucketStorage block_storemilk;
  public static BlockBucketStorage block_storeempty;
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
      b.setCreativeTab(ModCyclic.TAB);
    }
    if (b instanceof IHasRecipe) {
      ((IHasRecipe) b).addRecipe();
    }
    if (b instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) b);
    }
    blocks.add(b);
  }
}
