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
package com.lothrazar.cyclicmagic.block.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockCropMagicBean extends BlockCrops implements IHasConfig {

  public static final int MAX_AGE = 7;
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);
  private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D) };
  private List<ItemStack> myDrops = new ArrayList<ItemStack>();
  private ItemMagicBean seed;
  private boolean allowBonemeal;
  private boolean dropSeedOnHarvest;
  private ArrayList<String> myDropStrings;
  private Random rand = new Random();

  public BlockCropMagicBean() {
    Item[] drops = new Item[] {
        //  junk
        Items.STICK,
        Items.WHEAT_SEEDS,
        //plants 
        Item.getItemFromBlock(Blocks.YELLOW_FLOWER),
        Item.getItemFromBlock(Blocks.TALLGRASS), //TYPES?
        Item.getItemFromBlock(Blocks.DEADBUSH)//TYPES?
    };
    for (Item i : drops) {
      myDrops.add(new ItemStack(i));
    }
    EnumDyeColor[] dyeItems = new EnumDyeColor[] {
        EnumDyeColor.GRAY, EnumDyeColor.LIGHT_BLUE, EnumDyeColor.CYAN, EnumDyeColor.RED, EnumDyeColor.ORANGE, EnumDyeColor.YELLOW,
        EnumDyeColor.LIME, EnumDyeColor.MAGENTA, EnumDyeColor.PURPLE, EnumDyeColor.PINK, EnumDyeColor.SILVER
        //no ink sac
        //no lapis
        //no cooked cactus
        // no bonemeal
    };
    for (EnumDyeColor dye : dyeItems) {
      myDrops.add(new ItemStack(Items.DYE, 1, dye.getMetadata()));
    }
    for (EnumDyeColor dye : EnumDyeColor.values()) {//all 16 cols
      myDrops.add(new ItemStack(Blocks.STAINED_GLASS_PANE, 1, dye.getMetadata()));
    }
    for (BlockFlower.EnumFlowerType b : BlockFlower.EnumFlowerType.values()) {
      myDrops.add(new ItemStack(Blocks.RED_FLOWER, 1, b.getMeta()));
    }
    for (BlockDoublePlant.EnumPlantType b : BlockDoublePlant.EnumPlantType.values()) {
      myDrops.add(new ItemStack(Blocks.DOUBLE_PLANT, 1, b.getMeta()));
    }
  }

  @Override
  protected PropertyInteger getAgeProperty() {
    return AGE;
  }

  @Nullable
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return this.isMaxAge(state) ? this.getSeed() : this.getSeed();
  }

  @Override
  protected Item getSeed() {
    return seed;
  }

  public void setSeed(ItemMagicBean item) {
    seed = item;
  }

  @Override
  protected Item getCrop() {
    return null;//the null tells harvestcraft hey: dont remove my drops
  }

  private ItemStack getCropStack() {
    String res = this.myDropStrings.get(rand.nextInt(myDropStrings.size()));
    try {
      String[] ares = res.split(Pattern.quote("*"));
      Item item = Item.getByNameOrId(ares[0]);
      if (item == null) {
        ModCyclic.logger.error("Magic Bean config: loot item not found " + res);
        this.myDropStrings.remove(res);
        return getCropStack();
      }
      String meta = (ares.length > 1) ? ares[1] : "0";
      int imeta = Integer.parseInt(meta);
      ItemStack stack = new ItemStack(item, 1, imeta);
      return stack;
    }
    catch (Exception e) {
      ModCyclic.logger.error("Magic Bean config: loot item not found " + res);
      ModCyclic.logger.error(e.getMessage());
      return new ItemStack(Blocks.DIRT);
    }
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB[state.getValue(this.getAgeProperty()).intValue()];
  }

  @Override
  public int quantityDropped(Random random) {
    return super.quantityDropped(random) + 1;
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    NonNullList<ItemStack> ret = NonNullList.create();
    this.getDrops(ret, world, pos, state, fortune);
    return ret;
  }

  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    // Used by regular 'block break' and also by other harvesting features
    boolean isGrown = this.isMaxAge(state);
    if (isGrown) {
      int count = quantityDropped(state, fortune, rand);
      for (int i = 0; i < count; i++) {
        drops.add(getCropStack().copy()); //copy to make sure we return a new instance
      }
      if (dropSeedOnHarvest) {
        //ok full grown ones get the seed too
        drops.add(new ItemStack(getSeed()).copy());// default config means only a seed if NOT fully
      }
    }
    else {
      drops.add(new ItemStack(getSeed()).copy());
      ///else not fully grown. always drop a seed
    }
  }

  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }

  @Override
  protected int getBonemealAgeIncrease(World worldIn) {
    return allowBonemeal ? 1 : 0;
  }

  @Override
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return getBonemealAgeIncrease(worldIn) > 0;
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks + ".magicbean";
    allowBonemeal = config.getBoolean("MagicBeanBonemeal", category, true, "Allow bonemeal on magic bean");
    dropSeedOnHarvest = config.getBoolean("MagicBeanGrownDropSeed", category, false, "Allow dropping the seed item if fully grown.  (if its not grown it will still drop when broken)");
    ArrayList<String> deft = new ArrayList<String>();
    for (ItemStack drop : myDrops) {
      if (drop == null || drop.getItem() == null) {
        continue;
      }
      String resource = drop.getItem().getRegistryName().getResourceDomain() + ":" + drop.getItem().getRegistryName().getResourcePath();
      if (drop.getMetadata() > 0) {
        resource += "*" + drop.getMetadata();
      }
      deft.add(resource);
    }
    myDropStrings = new ArrayList<String>(Arrays.asList(config.getStringList("MagicBeanDropList", category, deft.toArray(new String[0]), "Drop list")));
    if (myDropStrings.size() == 0) {
      //do not let it be empty! avoid infloop
      myDropStrings.add("minecraft:dirt");
    }
  }
}
