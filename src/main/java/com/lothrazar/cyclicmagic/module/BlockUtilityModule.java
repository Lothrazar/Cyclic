package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockShears;
import com.lothrazar.cyclicmagic.block.BlockSoundSuppress;
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
import com.lothrazar.cyclicmagic.component.fluidstorage.BlockBucketStorage;
import com.lothrazar.cyclicmagic.component.fluidstorage.ItemBlockBucket;
import com.lothrazar.cyclicmagic.component.fluidstorage.TileEntityBucketStorage;
import com.lothrazar.cyclicmagic.component.fluidtransfer.BlockFluidCable;
import com.lothrazar.cyclicmagic.component.fluidtransfer.BlockFluidPump;
import com.lothrazar.cyclicmagic.component.fluidtransfer.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.component.fluidtransfer.TileEntityFluidPump;
import com.lothrazar.cyclicmagic.component.itemtransfer.BlockItemCable;
import com.lothrazar.cyclicmagic.component.itemtransfer.BlockItemPump;
import com.lothrazar.cyclicmagic.component.itemtransfer.TileEntityItemCable;
import com.lothrazar.cyclicmagic.component.itemtransfer.TileEntityItemPump;
import com.lothrazar.cyclicmagic.component.scaffold.BlockScaffolding;
import com.lothrazar.cyclicmagic.component.scaffold.BlockScaffoldingReplace;
import com.lothrazar.cyclicmagic.component.scaffold.BlockScaffoldingResponsive;
import com.lothrazar.cyclicmagic.component.scaffold.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.component.workbench.BlockWorkbench;
import com.lothrazar.cyclicmagic.component.workbench.TileEntityWorkbench;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.item.ItemBlock;
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
  private boolean soundproofing;
  private boolean workbench;
  private boolean enablePumpAndPipes;
  private boolean enablItemPipes;
  public void onPreInit() {
    if (workbench) {
      BlockWorkbench workbench = new BlockWorkbench();
      BlockRegistry.registerBlock(workbench, "block_workbench", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityWorkbench.class, Const.MODID + "workbench_te");
    }
    if (soundproofing) {
      BlockSoundSuppress block_soundproofing = new BlockSoundSuppress();
      BlockRegistry.registerBlock(block_soundproofing, "block_soundproofing", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(block_soundproofing);
    }
    if (autoCrafter) {
      BlockCrafter auto_crafter = new BlockCrafter();
      BlockRegistry.registerBlock(auto_crafter, "auto_crafter", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityCrafter.class, Const.MODID + "auto_crafter_te");
    }
    if (entityDetector) {
      BlockDetector detector = new BlockDetector();
      BlockRegistry.registerBlock(detector, "entity_detector", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityDetector.class, Const.MODID + "entity_detector_te");
    }
    if (enableFan) {
      BlockFan fan = new BlockFan();
      BlockRegistry.registerBlock(fan, "fan", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityFan.class, Const.MODID + "fan_te");
    }
    if (enableShearingBlock) {
      BlockShears block_shears = new BlockShears();
      BlockRegistry.registerBlock(block_shears, "block_shears", GuideCategory.BLOCK);
    }
    if (fragileEnabled) {
      BlockScaffolding block_fragile = new BlockScaffolding(true);
      ItemBlock ib = new ItemBlockScaffolding(block_fragile);
      BlockRegistry.registerBlock(block_fragile, ib, "block_fragile", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
      BlockScaffoldingResponsive block_fragile_auto = new BlockScaffoldingResponsive();
      ib = new ItemBlockScaffolding(block_fragile_auto);
      BlockRegistry.registerBlock(block_fragile_auto, ib, "block_fragile_auto", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
      BlockScaffoldingReplace block_fragile_weak = new BlockScaffoldingReplace();
      ib = new ItemBlockScaffolding(block_fragile_weak);
      BlockRegistry.registerBlock(block_fragile_weak, ib, "block_fragile_weak", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
    }
    if (fishingBlock) {
      BlockFishing block_fishing = new BlockFishing();
      BlockRegistry.registerBlock(block_fishing, "block_fishing", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityFishing.class, Const.MODID + "block_fishing_te");
    }
    if (disenchanter) {
      BlockDisenchanter block_disenchanter = new BlockDisenchanter();
      BlockRegistry.registerBlock(block_disenchanter, "block_disenchanter", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityDisenchanter.class, Const.MODID + "block_disenchanter_te");
    }
    if (enableBucketBlocks) {
      //tank
      BlockRegistry.block_storeempty = new BlockBucketStorage();
      BlockRegistry.registerBlock(BlockRegistry.block_storeempty, new ItemBlockBucket(BlockRegistry.block_storeempty), "block_storeempty", null);
      GameRegistry.registerTileEntity(TileEntityBucketStorage.class, "bucketstorage");
      GuideRegistry.register(GuideCategory.BLOCK, BlockRegistry.block_storeempty, null, null);
    }
    if (enablePumpAndPipes) {
      //pump
      BlockFluidPump fluid_xfer = new BlockFluidPump();
      BlockRegistry.registerBlock(fluid_xfer, "fluid_pump", null);
      GameRegistry.registerTileEntity(TileEntityFluidPump.class, "fluid_pump_te");
      //pipes
      BlockFluidCable k = new BlockFluidCable();
      BlockRegistry.registerBlock(k, "fluid_pipe", null);
      GameRegistry.registerTileEntity(TileEntityFluidCable.class, "fluid_pipe_te");
    }
    if (enablItemPipes) {
      //pump
      BlockItemPump fluid_xfer = new BlockItemPump();
      BlockRegistry.registerBlock(fluid_xfer, "item_pump", null);
      GameRegistry.registerTileEntity(TileEntityItemPump.class, "item_pump_te");
      //pipes
      BlockItemCable k = new BlockItemCable();
      BlockRegistry.registerBlock(k, "item_pipe", null);
      GameRegistry.registerTileEntity(TileEntityItemCable.class, "item_pipe_te");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;
    workbench = config.getBoolean("Workbench", category, true, Const.ConfigCategory.contentDefaultText);
    soundproofing = config.getBoolean("Soundproofing", category, true, Const.ConfigCategory.contentDefaultText);
    autoCrafter = config.getBoolean("AutoCrafter", category, true, Const.ConfigCategory.contentDefaultText);
    disenchanter = config.getBoolean("UnchantPylon", category, true, Const.ConfigCategory.contentDefaultText);
    entityDetector = config.getBoolean("EntityDetector", category, true, Const.ConfigCategory.contentDefaultText);
    enableFan = config.getBoolean("Fan", category, true, Const.ConfigCategory.contentDefaultText);
    enableShearingBlock = config.getBoolean("ShearingBlock", category, true, Const.ConfigCategory.contentDefaultText);
    enableBucketBlocks = config.getBoolean("BucketBlocks", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePumpAndPipes = config.getBoolean("PumpAndPipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fishingBlock = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablItemPipes = config.getBoolean("ItemPipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    
  }
}
