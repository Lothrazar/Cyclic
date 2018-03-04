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
package com.lothrazar.cyclicmagic.module;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.component.ore.BlockDimensionOre;
import com.lothrazar.cyclicmagic.component.ore.WorldGenNewOre;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockOreRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEmeraldHeight;
import com.lothrazar.cyclicmagic.world.gen.WorldGenGoldRiver;
import com.lothrazar.cyclicmagic.world.gen.WorldGenOreSingleton;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldModule extends BaseEventModule implements IHasConfig {
  final static int weightOre = 0;

  public static boolean netherOreEnabled;
  public static boolean endOreEnabled;
  public static boolean oreSpawns = true;
  public static boolean pigmenEnrage = true;
  private static boolean emeraldHeight = true;
  private static boolean goldRiver;
  private static boolean oreSingletons;
  public WorldModule() {
    BlockOreRegistry.register();
  }
  public static List<BlockDimensionOre> ores = new ArrayList<BlockDimensionOre>();

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.worldGen;
    config.setCategoryComment(category, "Control any blocks that get generated in new chunks & new worlds");
    Property prop;
    prop = config.get(category, "Nether Ore", true, "Generate ore in the nether.");
    prop.setRequiresMcRestart(true);
    netherOreEnabled = prop.getBoolean();
    prop = config.get(category, "End Ore", true, "Generate ore in the end.");
    prop.setRequiresMcRestart(true);
    endOreEnabled = prop.getBoolean();
    prop = config.get(category, "Infested Ores", true, "These dimension ores (nether and end) have a chance to spawn endermites or silverfish");
    oreSpawns = prop.getBoolean();
    prop = config.get(category, "PigmenEnrage", false, "If true, mining Nether ore has a 20% chance of enraging a nearby PigZombie within 16 blocks");
    pigmenEnrage = prop.getBoolean();
    prop = config.get(category, "Emerald Ore Boost", true, "Vanilla emerald ore now can spawn at any height, not only below the ground [still only in the Extreme Hills biomes as normal]");
    prop.setRequiresMcRestart(true);
    emeraldHeight = prop.getBoolean();
    prop = config.get(category, "Gold Rivers", true, "Vanilla gold ore can spawn in and river biomes at any height");
    prop.setRequiresMcRestart(true);
    goldRiver = prop.getBoolean();
    prop = config.get(category, "Ore Singletons", true, "Vanilla ores of all kinds can rarely spawn at all world heights, but only in veins of size one.  Great for amplified terrain.");
    prop.setRequiresMcRestart(true);
    oreSingletons = prop.getBoolean();

    //NEW ORES start here

    for (BlockDimensionOre ore : WorldModule.ores) {
      category = ore.config.getConfigCategory();
      ore.config.setBlockCount(
          config.getInt(ore.config.getBlockCountConfig(), category, ore.config.getBlockCountDefault(), 0, 32, "Approximate ore vein size.  Zero means no spawns."));
      ore.config.setSpawnChance(
          config.getInt(ore.config.getSpawnChanceConfig(), category, ore.config.getSpawnChanceDefault(), 0, 100, "Chance of a vein to spawn.  Zero means no spawns."));
      ore.config.setRegistered(
          config.getBoolean(ore.config.getBlockId(), category, ore.config.isRegisteredDefault(), "Ore exists"));
      ore.config.setHarvestLevel(
          config.getInt(ore.config.getBlockId() + "_harvest_level", category,
              ore.config.getHarvestLevelDefault(), 0, 3, "Tool Harvest Level"));
    }
  }
  @Override
  public void onPreInit() {

    super.onPreInit();

    for (BlockDimensionOre ore : WorldModule.ores) {
      if (ore.config.isRegistered()) {
        BlockRegistry.registerBlock(ore, ore.config.getBlockId(), null);
        ore.setPickaxeHarvestLevel(ore.config.getHarvestLevel());
      }
    }
  }
  @Override
  public void onInit() {
    //syncConfig comes AFTER pre init then init. which is why the configs require a restart 

    GameRegistry.registerWorldGenerator(new WorldGenNewOre(), weightOre);

    if (emeraldHeight) {
      GameRegistry.registerWorldGenerator(new WorldGenEmeraldHeight(), weightOre);
    }
    if (goldRiver) {
      GameRegistry.registerWorldGenerator(new WorldGenGoldRiver(), weightOre);
    }
    if (oreSingletons) {
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.IRON_ORE, 68), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.GOLD_ORE, 34), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.LAPIS_ORE, 34), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.REDSTONE_ORE, 16), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.DIAMOND_ORE, 16), weightOre);
    }
  }
  /**
   * TODO: move this to BlockDimensionOre
   * 
   * @param event
   */
  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() != null && event.getState().getBlock() instanceof BlockDimensionOre) {
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      if (oreSpawns) {
        //then try spawning mob- silverfish etc
        BlockDimensionOre block = (BlockDimensionOre) event.getState().getBlock();
        block.trySpawnTriggeredEntity(world, pos);
      }
      if (pigmenEnrage &&
          event.getWorld().provider.getDimension() == Const.Dimension.nether &&
          world.rand.nextDouble() < 0.2) {
        //then look for one 
        AxisAlignedBB range = UtilEntity.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(), 3, 16);
        List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, range);
        for (EntityPigZombie pz : found) {
          if (pz.isAngry() == false) {
            pz.attackEntityFrom(DamageSource.causePlayerDamage(event.getHarvester()), 0);
            // one enraged, is enough
            break;
          }
        }
      }
    }
  }

}
