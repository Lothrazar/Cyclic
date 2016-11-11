package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockBucketStorage;
import com.lothrazar.cyclicmagic.block.BlockFishing;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBucketStorage;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.item.itemblock.ItemBlockBucket;
import com.lothrazar.cyclicmagic.item.itemblock.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUtilityModule extends BaseModule {
  private boolean fragileEnabled;
  private boolean fishingBlock;
  private boolean enableBucketBlocks;
  public void onInit() {
    if (fragileEnabled) {
      BlockScaffolding block_fragile = new BlockScaffolding();
      BlockRegistry.registerBlock(block_fragile, new ItemBlockScaffolding(block_fragile), BlockScaffolding.name);
    }
    if (fishingBlock) {
      BlockFishing block_fishing = new BlockFishing();
      BlockRegistry.registerBlock(block_fishing, "block_fishing");
      GameRegistry.registerTileEntity(TileEntityFishing.class, Const.MODID + "block_fishing_te");
    }
    if (enableBucketBlocks) {
      BlockRegistry.block_storewater = new BlockBucketStorage(Items.WATER_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storewater, new ItemBlockBucket(BlockRegistry.block_storewater), "block_storewater", true);
      BlockRegistry.block_storemilk = new BlockBucketStorage(Items.MILK_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storemilk, new ItemBlockBucket(BlockRegistry.block_storemilk), "block_storemilk", true);
      BlockRegistry.block_storelava = new BlockBucketStorage(Items.LAVA_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storelava, new ItemBlockBucket(BlockRegistry.block_storelava), "block_storelava", true);
      BlockRegistry.block_storeempty = new BlockBucketStorage(null);
      BlockRegistry.registerBlock(BlockRegistry.block_storeempty, new ItemBlockBucket(BlockRegistry.block_storeempty), "block_storeempty", false);
      BlockRegistry.block_storeempty.addRecipe();
      GameRegistry.registerTileEntity(TileEntityBucketStorage.class, "bucketstorage");
      MinecraftForge.EVENT_BUS.register(BlockRegistry.block_storeempty);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableBucketBlocks = config.getBoolean("BucketBlocks", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fishingBlock = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityFishing.SPEED = config.getFloat("AutoFisherSpeed", Const.ConfigCategory.modpackMisc, 0.07F, 0.01F, 0.99F, "Speed of the Auto fisher, bigger is faster.  0.07 is 7% chance.");
  }
}
