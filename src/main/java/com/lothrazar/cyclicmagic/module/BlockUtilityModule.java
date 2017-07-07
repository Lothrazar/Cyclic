package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.BlockScaffoldingReplace;
import com.lothrazar.cyclicmagic.block.BlockScaffoldingResponsive;
import com.lothrazar.cyclicmagic.block.BlockShears;
import com.lothrazar.cyclicmagic.block.BlockSoundSuppress;
import com.lothrazar.cyclicmagic.block.ItemBlockScaffolding;
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
import com.lothrazar.cyclicmagic.component.workbench.BlockWorkbench;
import com.lothrazar.cyclicmagic.component.workbench.TileEntityWorkbench;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
  public void onPreInit() {
    if (workbench) {
      BlockWorkbench workbench = new BlockWorkbench();
      BlockRegistry.registerBlock(workbench, "block_workbench", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityWorkbench.class, Const.MODID + "workbench_te");
      OreDictionary.registerOre("workbench", workbench);
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
      BlockRegistry.registerBlock(block_fragile, new ItemBlockScaffolding(block_fragile), "block_fragile", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(Item.getItemFromBlock(block_fragile));
      BlockScaffoldingResponsive block_fragile_auto = new BlockScaffoldingResponsive();
      BlockRegistry.registerBlock(block_fragile_auto, new ItemBlockScaffolding(block_fragile_auto), "block_fragile_auto", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(Item.getItemFromBlock(block_fragile_auto));
      BlockScaffoldingReplace block_fragile_weak = new BlockScaffoldingReplace();
      BlockRegistry.registerBlock(block_fragile_weak, new ItemBlockScaffolding(block_fragile_weak), "block_fragile_weak", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(Item.getItemFromBlock(block_fragile_weak));
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
      //TODO: refactor and support more recipes
      BlockRegistry.block_storewater = new BlockBucketStorage(Items.WATER_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storewater, new ItemBlockBucket(BlockRegistry.block_storewater), "block_storewater", null);
      BlockRegistry.block_storemilk = new BlockBucketStorage(Items.MILK_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storemilk, new ItemBlockBucket(BlockRegistry.block_storemilk), "block_storemilk", null);
      BlockRegistry.block_storelava = new BlockBucketStorage(Items.LAVA_BUCKET);
      BlockRegistry.registerBlock(BlockRegistry.block_storelava, new ItemBlockBucket(BlockRegistry.block_storelava), "block_storelava", null);
      BlockRegistry.block_storeempty = new BlockBucketStorage(null);
      BlockRegistry.registerBlock(BlockRegistry.block_storeempty, new ItemBlockBucket(BlockRegistry.block_storeempty), "block_storeempty", null);
      GameRegistry.registerTileEntity(TileEntityBucketStorage.class, "bucketstorage");
      IRecipe recipe = RecipeRegistry.addShapedRecipe(new ItemStack(BlockRegistry.block_storeempty),
          "i i",
          " o ",
          "i i",
          'o', "obsidian", 'i', "ingotIron");
      GuideRegistry.register(GuideCategory.BLOCK, BlockRegistry.block_storeempty, recipe, null);
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
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fishingBlock = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
