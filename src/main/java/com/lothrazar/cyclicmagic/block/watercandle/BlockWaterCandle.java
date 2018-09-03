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
package com.lothrazar.cyclicmagic.block.watercandle;

import java.util.List;
import java.util.Random;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.BlockBase;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class BlockWaterCandle extends BlockBase implements IHasRecipe, IContent {

  private static final int TICK_RATE = 50;
  private static final int RADIUS = 5;
  private static final double CHANCE = 0.333;
  EnumCreatureType type = EnumCreatureType.MONSTER;

  public BlockWaterCandle() {
    super(Material.GROUND);
    this.setSoundType(SoundType.GROUND);
    //    this.setHarvestLevel(Const.ToolStrings.shovel, 2);
    this.setTickRandomly(true);
    this.setTranslucent();
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    return false;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return null;
  }

  @Override
  public int quantityDropped(Random rand) {
    return 0;
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    try {
      //      if (rand.nextDouble() < CHANCE)
        trySpawn(world, pos, rand);
    }
    catch (Exception exception) {
      ModCyclic.logger.error("Error spawning monster ", exception);
    }
  }

  @Override
  public void randomTick(World world, BlockPos pos, IBlockState state, Random rand) {
    try {
      //      if (rand.nextDouble() < CHANCE) 
        trySpawn(world, pos, rand);
    }
    catch (Exception exception) {
      ModCyclic.logger.error("Error spawning monster ", exception);
    }
  }

  private void trySpawn(World world, BlockPos pos, Random rand) throws Exception {
    ModCyclic.logger.error("!!tick ");
    List<SpawnListEntry> spawnOptions = world.getBiome(pos).getSpawnableList(type);
    if (spawnOptions == null) {
      return;
    }
    //end is inclusive 
    int found = MathHelper.getInt(rand, 0, spawnOptions.size() - 1);
    SpawnListEntry entry = spawnOptions.get(found);
    if (entry == null || entry.entityClass == null) {
      return;
    }
    EntityEntry entityEntry = EntityRegistry.getEntry(entry.entityClass);
    EntityLiving monster = null;
    Entity ent = entityEntry.newInstance(world);
    if (ent instanceof EntityLiving)
      monster = (EntityLiving) ent;
    if (monster == null) {
      return;
    }
    //if radius is 3, then go be
    float x = pos.getX() + MathHelper.getInt(rand, -1 * RADIUS, RADIUS);
    float y = pos.getY();
    float z = pos.getZ() + MathHelper.getInt(rand, -1 * RADIUS, RADIUS);
    monster.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
    //null means not from a spawner 
    if (monster.getCanSpawnHere() &&
        ForgeEventFactory.canEntitySpawn(monster, world, x, y, z, null) == Event.Result.DENY) {
      return;
    }
    //we can spawn here
    if (world.spawnEntity(monster)) {
      monster.onInitialSpawn(world.getDifficultyForLocation(pos), null);//i hope null is ok? 
      world.scheduleUpdate(pos, this, TICK_RATE);
    }
  }

  @Override
  public int tickRate(World worldIn) {
    return TICK_RATE;
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, "water_candle", GuideCategory.BLOCK);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("water_candle", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 4),
        "pcp",
        "cxc",
        "pcp",
        'c', "dirt",
        'x', Items.DIAMOND,
        'p', new ItemStack(Items.CLAY_BALL));
  }
}
