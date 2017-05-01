package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockShears;
import com.lothrazar.cyclicmagic.block.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.BlockScaffoldingReplace;
import com.lothrazar.cyclicmagic.block.BlockScaffoldingResponsive;
import com.lothrazar.cyclicmagic.component.bucketstorage.BlockBucketStorage;
import com.lothrazar.cyclicmagic.component.bucketstorage.ItemBlockBucket;
import com.lothrazar.cyclicmagic.component.bucketstorage.TileEntityBucketStorage;
import com.lothrazar.cyclicmagic.component.crafter.BlockCrafter;
import com.lothrazar.cyclicmagic.component.crafter.TileEntityCrafter;
import com.lothrazar.cyclicmagic.component.disenchanter.BlockDisenchanter;
import com.lothrazar.cyclicmagic.component.disenchanter.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.component.entitydetector.BlockDetector;
import com.lothrazar.cyclicmagic.component.entitydetector.TileEntityDetector;
import com.lothrazar.cyclicmagic.component.fan.BlockFan;
import com.lothrazar.cyclicmagic.component.fan.TileEntityFan;
import com.lothrazar.cyclicmagic.component.fisher.BlockFishing;
import com.lothrazar.cyclicmagic.component.fisher.TileEntityFishing;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUtilityModule extends BaseModule implements IHasConfig {
  private boolean fragileEnabled;
  private boolean fishingBlock;
  private boolean enableBucketBlocks;
  private boolean enableShearingBlock;
  private boolean enableFan;
  private boolean entityDetector;
  private boolean disenchanter;
  private boolean autoCrafter;
  public void onInit() {
    if (autoCrafter) {
      BlockCrafter auto_crafter = new BlockCrafter();
      BlockRegistry.registerBlock(auto_crafter, "auto_crafter");
      GameRegistry.registerTileEntity(TileEntityCrafter.class, Const.MODID + "auto_crafter_te");
    }
    if (entityDetector) {
      BlockDetector detector = new BlockDetector();
      BlockRegistry.registerBlock(detector, "entity_detector");
      GameRegistry.registerTileEntity(TileEntityDetector.class, Const.MODID + "entity_detector_te");
    }
    if (enableFan) {
      BlockFan fan = new BlockFan();
      BlockRegistry.registerBlock(fan, "fan");
      GameRegistry.registerTileEntity(TileEntityFan.class, Const.MODID + "fan_te");
    }
    if (enableShearingBlock) {
      BlockShears block_shears = new BlockShears();
      BlockRegistry.registerBlock(block_shears, "block_shears");
    }
    if (fragileEnabled) {
      BlockScaffolding block_fragile = new BlockScaffolding(true);
      BlockRegistry.registerBlock(block_fragile, new ItemBlockScaffolding(block_fragile), "block_fragile");
      ModCyclic.instance.events.register(Item.getItemFromBlock(block_fragile));
      BlockScaffoldingResponsive block_fragile_auto = new BlockScaffoldingResponsive();
      BlockRegistry.registerBlock(block_fragile_auto, new ItemBlockScaffolding(block_fragile_auto), "block_fragile_auto");
      ModCyclic.instance.events.register(Item.getItemFromBlock(block_fragile_auto));
      BlockScaffoldingReplace block_fragile_weak = new BlockScaffoldingReplace();
      BlockRegistry.registerBlock(block_fragile_weak, new ItemBlockScaffolding(block_fragile_weak), "block_fragile_weak");
      ModCyclic.instance.events.register(Item.getItemFromBlock(block_fragile_weak));
    }
    if (fishingBlock) {
      BlockFishing block_fishing = new BlockFishing();
      BlockRegistry.registerBlock(block_fishing, "block_fishing");
      GameRegistry.registerTileEntity(TileEntityFishing.class, Const.MODID + "block_fishing_te");
    }
    if (disenchanter) {
      BlockDisenchanter block_disenchanter = new BlockDisenchanter();
      BlockRegistry.registerBlock(block_disenchanter, "block_disenchanter");
      GameRegistry.registerTileEntity(TileEntityDisenchanter.class, Const.MODID + "block_disenchanter_te");
    }
    if (enableBucketBlocks) {
      //TODO: refactor and support more recipes
      BlockRegistry.block_storewater = new BlockBucketStorage(Items.WATER_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storewater, new ItemBlockBucket(BlockRegistry.block_storewater), "block_storewater", true);
      BlockRegistry.block_storemilk = new BlockBucketStorage(Items.MILK_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storemilk, new ItemBlockBucket(BlockRegistry.block_storemilk), "block_storemilk", true);
      BlockRegistry.block_storelava = new BlockBucketStorage(Items.LAVA_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storelava, new ItemBlockBucket(BlockRegistry.block_storelava), "block_storelava", true);
      BlockRegistry.block_storeempty = new BlockBucketStorage(null);
      BlockRegistry.registerBlock(BlockRegistry.block_storeempty, new ItemBlockBucket(BlockRegistry.block_storeempty), "block_storeempty", false);
      GameRegistry.registerTileEntity(TileEntityBucketStorage.class, "bucketstorage");
      GameRegistry.addRecipe(new ItemStack(BlockRegistry.block_storeempty),
          "i i",
          " o ",
          "i i",
          'o', Blocks.OBSIDIAN, 'i', Items.IRON_INGOT);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;
    autoCrafter = config.getBoolean("AutoCrafter", category, true, Const.ConfigCategory.contentDefaultText);
    disenchanter = config.getBoolean("UnchantPylon", category, true, Const.ConfigCategory.contentDefaultText);
    entityDetector = config.getBoolean("EntityDetector", category, true, Const.ConfigCategory.contentDefaultText);
    enableFan = config.getBoolean("Fan", category, true, Const.ConfigCategory.contentDefaultText);
    enableShearingBlock = config.getBoolean("ShearingBlock", category, true, Const.ConfigCategory.contentDefaultText);
    enableBucketBlocks = config.getBoolean("BucketBlocks", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fishingBlock = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    config.getFloat("AutoFisherSpeed", Const.ConfigCategory.modpackMisc, 0.07F, 0.01F, 0.99F, "[This config is deprecated.  Speed now depends on how much water is nearby].  Speed of the Auto fisher, bigger is faster.  0.07 is 7% chance.");
  }
}
