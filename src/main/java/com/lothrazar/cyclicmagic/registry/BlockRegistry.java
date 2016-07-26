package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import com.lothrazar.cyclicmagic.block.BlockBuilder;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
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
 
  public static void registerDimensionOres() {
    //nether ores
    nether_gold_ore = new BlockDimensionOre(Items.GOLD_NUGGET, 0, 4);
    nether_gold_ore.setSpawnType(SpawnType.SILVERFISH, 1);
    registerBlock(nether_gold_ore, "nether_gold_ore");
    nether_coal_ore = new BlockDimensionOre(Items.COAL);
    nether_coal_ore.setSpawnType(SpawnType.SILVERFISH, 1);
    registerBlock(nether_coal_ore, "nether_coal_ore");
    nether_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    nether_lapis_ore.setSpawnType(SpawnType.SILVERFISH, 2);
    registerBlock(nether_lapis_ore, "nether_lapis_ore");
    nether_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    nether_emerald_ore.setSpawnType(SpawnType.SILVERFISH, 5);
    registerBlock(nether_emerald_ore, "nether_emerald_ore");
    nether_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    nether_diamond_ore.setSpawnType(SpawnType.SILVERFISH, 8);
    registerBlock(nether_diamond_ore, "nether_diamond_ore");
    //end ores
    end_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    end_redstone_ore.setSpawnType(SpawnType.ENDERMITE, 3);
    registerBlock(end_redstone_ore, "end_redstone_ore");
    end_coal_ore = new BlockDimensionOre(Items.COAL);
    end_coal_ore.setSpawnType(SpawnType.ENDERMITE, 1);
    registerBlock(end_coal_ore, "end_coal_ore");
    end_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    end_lapis_ore.setSpawnType(SpawnType.ENDERMITE, 5);
    registerBlock(end_lapis_ore, "end_lapis_ore");
    end_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    end_emerald_ore.setSpawnType(SpawnType.ENDERMITE, 8);
    registerBlock(end_emerald_ore, "end_emerald_ore");
    end_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    end_diamond_ore.setSpawnType(SpawnType.ENDERMITE, 8);
    registerBlock(end_diamond_ore, "end_diamond_ore");
  }
}
