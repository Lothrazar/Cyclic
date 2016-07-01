package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import com.lothrazar.cyclicmagic.block.BlockBuilder;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.BlockSprout;
import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import com.lothrazar.cyclicmagic.item.ItemSproutSeeds;
import com.lothrazar.cyclicmagic.item.itemblock.ItemBlockBucket;
import com.lothrazar.cyclicmagic.item.itemblock.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry {
  public static ArrayList<Block> blocks = new ArrayList<Block>();
  public static BlockScaffolding block_fragile;
  static BlockUncrafting uncrafting_block;
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
  private static boolean spawnersUnbreakable;
  private static BlockBuilder builder_block;
  //lots of helpers/overrides with defaults
  private static void registerBlock(Block b, String name) {
    registerBlock(b, name, false);
  }
  private static void registerBlock(Block b, String name, boolean isHidden) {
    registerBlock(b, new ItemBlock(b), name, isHidden);
  }
  private static void registerBlock(Block b, ItemBlock ib, String name) {
    registerBlock(b, ib, name, false);
  }
  private static void registerBlock(Block b, ItemBlock ib, String name, boolean isHidden) {
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
  public static void register() {
    
    registerSprout();
    //??maybe? nah.
    //Blocks.obsidian.setHardness(Blocks.obsidian.getHarvestLevel(Blocks.obsidian.getDefaultState()) / 2);
    registerBlock(uncrafting_block, "uncrafting_block");
    uncrafting_block.addRecipe();
    registerBlock(builder_block, "builder_block");
    builder_block.addRecipe();
    //they go up 2,4,6 blocks high, approx
    //old: .6 .9 1.2
    BlockLaunch plate_launch_small = new BlockLaunch(0.8F, SoundEvents.BLOCK_SLIME_STEP);
    BlockLaunch plate_launch_med = new BlockLaunch(1.3F, SoundEvents.BLOCK_SLIME_FALL);
    BlockLaunch plate_launch_large = new BlockLaunch(1.8F, SoundEvents.BLOCK_SLIME_BREAK);
    registerBlock(plate_launch_small, "plate_launch_small");
    registerBlock(plate_launch_med, "plate_launch_med");
    registerBlock(plate_launch_large, "plate_launch_large");
    GameRegistry.addRecipe(new ItemStack(plate_launch_small, 6),
        "sss", "ggg", "iii",
        's', Blocks.SLIME_BLOCK,
        'g', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
        'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    GameRegistry.addShapelessRecipe(new ItemStack(plate_launch_med),
        new ItemStack(plate_launch_small),
        new ItemStack(Items.QUARTZ));
    GameRegistry.addShapelessRecipe(new ItemStack(plate_launch_large),
        new ItemStack(plate_launch_med),
        new ItemStack(Blocks.END_STONE));
    registerBlock(block_fragile, new ItemBlockScaffolding(block_fragile), BlockScaffolding.name);
    block_fragile.addRecipe();
    if (spawnersUnbreakable) {
      Blocks.MOB_SPAWNER.setBlockUnbreakable();
    }
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
    //bucket storage
    block_storewater = new BlockBucketStorage(Items.WATER_BUCKET);
    registerBlock(block_storewater, new ItemBlockBucket(block_storewater), "block_storewater", true);
    block_storemilk = new BlockBucketStorage(Items.MILK_BUCKET);
    registerBlock(block_storemilk, new ItemBlockBucket(block_storemilk), "block_storemilk", true);
    block_storelava = new BlockBucketStorage(Items.LAVA_BUCKET);
    registerBlock(block_storelava, new ItemBlockBucket(block_storelava), "block_storelava", true);
    block_storeempty = new BlockBucketStorage(null);
    registerBlock(block_storeempty, new ItemBlockBucket(block_storeempty), "block_storeempty", false);
    block_storeempty.addRecipe();
  }
  private static void registerSprout() {
    BlockSprout sprout = new BlockSprout();
    registerBlock(sprout ,"sprout",true);
    ItemRegistry.sprout_seed = new ItemSproutSeeds(sprout, Blocks.FARMLAND);
    ItemRegistry.sprout_seed.setUnlocalizedName("sprout_seed");
    ItemRegistry.registerItem(ItemRegistry.sprout_seed, "sprout_seed");
    ItemRegistry.itemMap.put("sprout_seed", ItemRegistry.sprout_seed);
    GameRegistry.addRecipe(new ItemStack(ItemRegistry.sprout_seed,0,8), 
        "waw",
        "bEc",
        "wdw",
        'w',Items.WHEAT_SEEDS,
        'E',Items.EMERALD,
        'a',Items.BEETROOT_SEEDS,
        'b',Items.MELON_SEEDS,
        'c',Items.PUMPKIN_SEEDS,
        'd',Items.NETHER_WART );
  }
  public static void construct() {
    uncrafting_block = new BlockUncrafting();
    builder_block = new BlockBuilder();
    block_fragile = new BlockScaffolding();
  }
  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    spawnersUnbreakable = config.getBoolean("Spawners Unbreakable", category, true, "Make mob spawners unbreakable");
    uncrafting_block.syncConfig(config);
    builder_block.syncConfig(config);
  }
}
