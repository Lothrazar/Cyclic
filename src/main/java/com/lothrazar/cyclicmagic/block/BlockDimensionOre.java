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
package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import com.lothrazar.cyclicmagic.block.base.IHasOreDict;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDimensionOre extends BlockOre implements IHasOreDict {
  private Item dropped;
  private int droppedMeta;
  private int randomMax;
  private int spawnChance = 0;
  private SpawnType spawn = null;
  ItemStack smeltOut;
  String ore;
  public static enum SpawnType {
    ENDERMITE, SILVERFISH
  }
  public BlockDimensionOre(Item drop) {
    this(drop, 0);
  }
  public BlockDimensionOre(Item drop, int dmg) {
    this(drop, 0, 1);
  }
  public BlockDimensionOre(Item drop, int dmg, int max) {
    super();
    dropped = drop;
    droppedMeta = dmg;
    randomMax = max;
    this.setSoundType(SoundType.STONE);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setHarvestLevel(Const.ToolStrings.axe, 0);
    this.setHarvestLevel(Const.ToolStrings.shovel, 0);
  }
  public BlockDimensionOre setPickaxeHarvestLevel(int h) {
    this.setHarvestLevel(Const.ToolStrings.pickaxe, h);
    return this;
  }
  public void setSpawnType(SpawnType t, int chance) {
    this.spawn = t;
    this.spawnChance = chance;
  }
  public void registerSmeltingOutput(Item out) {
    smeltOut = new ItemStack(out);
  }
  public void registerSmeltingOutput(ItemStack out) {
    smeltOut = out;
  }
  public ItemStack getSmeltingOutput() {
    return smeltOut;
  }
  public void registerOre(String out) {
    ore = out;
  }
  @Override
  public String getOre() {
    return ore;
  }
  public void trySpawnTriggeredEntity(World world, BlockPos pos) {
    if (this.spawn != null && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
      int rand = world.rand.nextInt(100);
      if (rand < this.spawnChance) {
        Entity e = null;
        switch (this.spawn) {
          case ENDERMITE:
            e = new EntityEndermite(world);
          break;
          case SILVERFISH:
            e = new EntitySilverfish(world);
          break;
        }
        if (e != null) {
          e.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
          world.spawnEntity(e);
        }
      }
    }
  }
  @Override
  public int damageDropped(IBlockState state) {
    return droppedMeta;
  }
  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {
    return super.quantityDroppedWithBonus(fortune, random);
  }
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return dropped;
  }
  @Override
  public int quantityDropped(Random random) {
    if (randomMax == 1) {
      return 1;
    }
    return 1 + random.nextInt(randomMax);
  }
  @Override
  public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
    Random rand = world instanceof World ? ((World) world).rand : new Random();
    return MathHelper.getInt(rand, 2, 5);
  }
  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    return true;
  }
  @Override
  public String toString() {
    return this.getLocalizedName();
  }
  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
