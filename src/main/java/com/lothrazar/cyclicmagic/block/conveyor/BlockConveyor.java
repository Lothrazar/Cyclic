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
package com.lothrazar.cyclicmagic.block.conveyor;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFlat;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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

  protected static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
  private static final int RECIPE_OUTPUT = 8;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.03125D, 1D);
  protected final static float ANGLE = 1;
  private static final float powerCorrection = 0.02F;

  public static enum SpeedType {
    TINY, SMALL, MEDIUM, LARGE;
  }

  protected SpeedType type;
  protected float power;
  private SoundEvent sound;
  private BlockConveyorCorner corner;
  public static boolean doCorrections = true;
  public static boolean keepEntityGrounded = true;
  public static boolean sneakPlayerAvoid;

  public BlockConveyor(SpeedType t) {
    super(Material.IRON);
    type = t;
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
  }

  public BlockConveyor(BlockConveyorCorner corner) {
    this(corner.type);
    this.corner = corner;
    this.setSoundType(SoundType.METAL);
    sound = SoundEvents.BLOCK_ANVIL_BREAK;
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
    tickMovement(pos, entity, getFacingFromState(state));
  }

  protected void tickMovement(BlockPos pos, Entity entity, EnumFacing face) {
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
            'x', Items.CLAY_BALL,
            'b', "dyeRed");
      break;
      case MEDIUM:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', Items.CLAY_BALL,
            'b', "dyePurple");
      break;
      case SMALL:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', Items.CLAY_BALL,
            'b', "dyeMagenta");
      break;
      case TINY:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUTPUT),
            "sbs",
            "bxb",
            "sbs",
            's', "ingotIron",
            'x', Items.CLAY_BALL,
            'b', "dyeLightBlue");
      break;
      default:
      break;
    }
    return null;
  }

  //below is all for facing
  @Override
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    return this.getDefaultState().withProperty(PROPERTYFACING, facing);
  }

  public EnumFacing getFacingFromState(IBlockState state) {
    return state.getValue(PROPERTYFACING);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    EnumFacing facing = state.getValue(PROPERTYFACING);
    int facingbits = facing.getHorizontalIndex();
    return facingbits;
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    return state;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { PROPERTYFACING });
  }

  /**
   * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the IBlockstate
   */
  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    //  ModCyclic.logger.error("getStateForPlacement");
    // find the quadrant the player is facing
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
  }

  /**
   * Called by ItemBlocks after a block is set in the world, to allow post-place logic
   * 
   * called AFTER getStateForPlacement
   * 
   * used to override straight pieces into corners
   */
  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (corner == null) {
      return;
    }
    IBlockState north = world.getBlockState(pos.offset(EnumFacing.NORTH));
    IBlockState south = world.getBlockState(pos.offset(EnumFacing.SOUTH));
    IBlockState west = world.getBlockState(pos.offset(EnumFacing.WEST));
    IBlockState east = world.getBlockState(pos.offset(EnumFacing.EAST));
    boolean isNorth = north.getBlock() == this;
    boolean isSouth = south.getBlock() == this;
    boolean isWest = west.getBlock() == this;
    boolean isEast = east.getBlock() == this;
    if (isNorth && isWest) {
      if (west.getValue(PROPERTYFACING) == EnumFacing.EAST)
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, west.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true));
      else
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, north.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false));
    }
    else if (isNorth && isEast) {
      if (east.getValue(PROPERTYFACING) == EnumFacing.WEST)
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, east.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false));
      else
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, north.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true));
    }
    else if (isSouth && isEast) {
      if (south.getValue(PROPERTYFACING) == EnumFacing.NORTH)
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, south.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false));
      else
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, east.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true));
    }
    else if (isSouth && isWest) {
      if (west.getValue(PROPERTYFACING) == EnumFacing.WEST)
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, south.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true));
      else
        world.setBlockState(pos, corner.getDefaultState().withProperty(PROPERTYFACING, west.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false));
    }
  }
  //  @Override
  //  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
  //    return this.getStateForPlacement(worldIn, pos, blockFaceClickedOn, hitX, hitY, hitZ, meta, placer);//110 support
  //  }
}
