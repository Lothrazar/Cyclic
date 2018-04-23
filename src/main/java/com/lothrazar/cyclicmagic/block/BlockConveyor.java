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

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFlat;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockConveyor extends BlockBaseFlat implements IHasRecipe {

  private static final int RECIPE_OUTPUT = 8;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.03125D, 1D);
  private final static float ANGLE = 1;
  private static final float powerCorrection = 0.02F;

  public static enum SpeedType {
    TINY, SMALL, MEDIUM, LARGE;
  }

  private SpeedType type;
  private float power;
  private SoundEvent sound;
  public static boolean doCorrections = true;
  public static boolean keepEntityGrounded = true;
  public static boolean sneakPlayerAvoid;

  public BlockConveyor(SpeedType t) {
    super(Material.IRON);//, MapColor.GRASS
    this.setSoundType(SoundType.SLIME);
    type = t;
    sound = SoundEvents.BLOCK_ANVIL_BREAK;
    switch (type) {
      case LARGE:
        this.power = 0.32F;
      break;
      case MEDIUM:
        this.power = 0.16F;
      break;
      case SMALL:
        this.power = 0.08F;
      break;
      case TINY:
        this.power = 0.04F;
      break;
      default:
      break;
    }
    //fixing y rotation in blockstate json: http://www.minecraftforge.net/forum/index.php?topic=25937.0
  }

  protected void playClickOnSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, this.sound, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    EnumFacing face = getFacingFromState(state);
    if (keepEntityGrounded) {
      entity.onGround = true;//THIS is to avoid the entity ZOOMING when slightly off the ground
    }
    if (sneakPlayerAvoid && entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
      return;
    }
    //for example when you have these layering down stairs, and then they speed up when going down one block ledge
    UtilEntity.launchDirection(entity, ANGLE, power, face); //this.playClickOnSound(worldIn, pos);
    if (doCorrections) {
      if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
        //then since +Z is south, and +X is east: so
        double xDiff = (pos.getX() + 0.5) - entity.posX;
        if (Math.abs(xDiff) > 0.09) {//max is .5
          if (xDiff < 0) {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.WEST);
          }
          else {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.EAST);
          }
        }
      }
      else if (face == EnumFacing.EAST || face == EnumFacing.WEST) {
        //then since +Z is south, and +X is east: so
        double diff = (pos.getZ() + 0.5) - entity.posZ;
        //??NOPE  &&  ((int) entity.posZ) == entity.getPosition().getZ()
        if (Math.abs(diff) > 0.09) {//max is .5
          if (diff < 0) {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.NORTH);
          }
          else {
            UtilEntity.launchDirection(entity, ANGLE, powerCorrection, EnumFacing.SOUTH);
          }
        }
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    int speed = (int) (this.power * 100);
    tooltip.add(UtilChat.lang("tile.plate_push.tooltip") + speed);
  }

  @Override
  public IRecipe addRecipe() {
    switch (type) {
      case LARGE:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyeRed");
      break;
      case MEDIUM:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyePurple");
      break;
      case SMALL:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyeMagenta");
      break;
      case TINY:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', "slimeball",
            'b', "dyeLightBlue");
      break;
      default:
      break;
    }
    return null;
  }
}
