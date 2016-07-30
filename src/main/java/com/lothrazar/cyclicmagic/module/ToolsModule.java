package com.lothrazar.cyclicmagic.module;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.ItemSleepingBag;
import com.lothrazar.cyclicmagic.item.ItemToolHarvest;
import com.lothrazar.cyclicmagic.item.ItemToolPearlReuse;
import com.lothrazar.cyclicmagic.item.ItemToolPush;
import com.lothrazar.cyclicmagic.item.ItemToolSpawnInspect;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class ToolsModule extends BaseModule {
  private boolean enableSleepingMat;
  private boolean enableToolPush;
  private boolean enableHarvestLeaves;
  private boolean enableToolHarvest;
  private boolean enableHarvestWeeds;
  private boolean enablePearlReuse;
  private boolean enableSpawnInspect;
  private boolean enableCyclicWand;
  @Override
  public void register() {
    if(enableSpawnInspect){
      ItemRegistry.tool_spawn_inspect = new ItemToolSpawnInspect();
      ItemRegistry.addItem(ItemRegistry.tool_spawn_inspect, "tool_spawn_inspect");
    }
    if(enablePearlReuse){
      ItemRegistry.ender_pearl_reuse = new ItemToolPearlReuse();
      ItemRegistry.addItem(ItemRegistry.ender_pearl_reuse, "ender_pearl_reuse");
    }
    if(enableHarvestWeeds){
      ItemRegistry.tool_harvest_weeds = new ItemToolHarvest(ItemToolHarvest.HarvestType.WEEDS);
      ItemRegistry.addItem(ItemRegistry.tool_harvest_weeds, "tool_harvest_weeds");
    }
    if(enableToolHarvest){
      ItemRegistry.tool_harvest_crops = new ItemToolHarvest(ItemToolHarvest.HarvestType.CROPS);
      ItemRegistry.addItem(ItemRegistry.tool_harvest_crops, "tool_harvest_crops");
    }
    if(enableHarvestLeaves){
      ItemRegistry.tool_harvest_leaves = new ItemToolHarvest(ItemToolHarvest.HarvestType.LEAVES);
      ItemRegistry.addItem(ItemRegistry.tool_harvest_leaves, "tool_harvest_leaves");
    }
    if(enableToolPush){
      ItemRegistry.tool_push = new ItemToolPush();
      ItemRegistry.addItem(ItemRegistry.tool_push, "tool_push");
    }
    if(enableSleepingMat){
      ItemRegistry.sleeping_mat = new ItemSleepingBag();
      ItemRegistry.addItem(ItemRegistry.sleeping_mat, "sleeping_mat");
    }
    if(enableCyclicWand){
      ItemRegistry.cyclic_wand_build = new ItemCyclicWand();
      ItemRegistry.addItem(ItemRegistry.cyclic_wand_build, "cyclic_wand_build");

      SpellRegistry.register();
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableSpawnInspect = config.getBoolean("SpawnInspect", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuse = config.getBoolean("enablePearlReuse", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestWeeds = config.getBoolean("enableHarvestWeeds", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolHarvest = config.getBoolean("enableToolHarvest", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestLeaves = config.getBoolean("HarvestLeaves", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolPush     = config.getBoolean("ToolPush", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSleepingMat  = config.getBoolean("SleepingMat", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCyclicWand = config.getBoolean("CyclicWand", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
