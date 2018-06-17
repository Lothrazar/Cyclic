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
import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFlat;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
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
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 0.1875D, 1D);
  protected final static float ANGLE = 1;
  private static final float powerCorrection = 0.02F;

  public static enum SpeedType {
    TINY, SMALL, MEDIUM, LARGE;
  }

  protected SpeedType type;
  protected float power;
  private SoundEvent sound;
  public static boolean doCorrections = true;
  protected boolean keepEntityGrounded = true;
  private BlockConveyor corner;
  private BlockConveyor angled;
  protected BlockConveyor dropFlat;
  public static boolean sneakPlayerAvoid;

  public BlockConveyor(SpeedType t) {
    super(Material.ROCK);
    dropFlat = this;
    type = t;
    switch (type) {
      case LARGE:
        this.power = 0.32F;
      break;
      case MEDIUM:
        this.power = 0.20F;
      break;
      case SMALL:
        this.power = 0.16F;
      break;
      case TINY:
        this.power = 0.08F;
      break;
      default:
      break;
    }
  }

  public BlockConveyor(@Nonnull BlockConveyor corner, @Nonnull BlockConveyor angled) {
    this(corner.type);
    this.setCorner(corner);
    this.setAngled(angled);
    this.setSoundType(SoundType.METAL);
    sound = SoundEvents.BLOCK_ANVIL_BREAK;
    //fixing y rotation in blockstate json: http://www.minecraftforge.net/forum/index.php?topic=25937.0
  }

  public boolean isAngle() {
    return false;
  }

  public boolean isCorner() {
    return false;
  }

  @Override
  public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
    IBlockState state = world.getBlockState(pos);
    EnumFacing iAmFacing = state.getValue(PROPERTYFACING);
    if (this.isAngle()) {
      if (iAmFacing == EnumFacing.NORTH) {
        if (state.getValue(BlockConveyorAngle.FLIPPED)) {//default is false 
          world.setBlockState(pos, state.withProperty(BlockConveyorAngle.FLIPPED, false).withProperty(PROPERTYFACING, EnumFacing.EAST));
        }
        else {
          world.setBlockState(pos, dropFlat.getDefaultState().withProperty(PROPERTYFACING, EnumFacing.EAST));
        }
        return true;
      }
    }
    else if (this.isCorner()) {
      if (iAmFacing == EnumFacing.NORTH) {
        if (state.getValue(BlockConveyorCorner.FLIPPED)) {//default is false 
          world.setBlockState(pos, state.withProperty(BlockConveyorCorner.FLIPPED, false).withProperty(PROPERTYFACING, EnumFacing.EAST));
        }
        else {
          world.setBlockState(pos, getAngled().getDefaultState().withProperty(PROPERTYFACING, EnumFacing.EAST));
        }
        return true;
      }
    }
    else { // is flat
      if (iAmFacing == EnumFacing.NORTH) {
        world.setBlockState(pos, getCorner().getDefaultState().withProperty(PROPERTYFACING, EnumFacing.EAST));
        return true;
      }
    }
    return super.rotateBlock(world, pos, axis);
  }

  protected void playClickOnSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, this.sound, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    //  this.getCollisionBoundingBox(blockState, worldIn, pos)
    return AABB;
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
      return;
    }
    EnumFacing face = getFacingFromState(state);
    tickMovement(pos, entity, face);
    if (entity instanceof EntityLivingBase == false) {
      entity.motionY += 0.5;
      hackOverBump(worldIn, pos, entity, face);
    }
    //hack to get over the bump
  }

  protected void hackOverBump(World worldIn, BlockPos pos, Entity entity, EnumFacing face) {
    if (entity instanceof EntityLivingBase == false &&
        worldIn.getBlockState(pos.offset(face)).getBlock() instanceof BlockConveyorAngle ||
        worldIn.getBlockState(pos.offset(face).up()).getBlock() instanceof BlockConveyorAngle) {
      //      entity.setGlowing(true);
      entity.onGround = false;
      entity.motionY += 0.1;
    }
  }

  protected void tickMovement(BlockPos pos, Entity entity, EnumFacing face) {
    // if (keepEntityGrounded) {
    entity.onGround = true;//THIS is to avoid the entity ZOOMING when slightly off the ground
    // }
    //for example when you have these layering down stairs, and then they speed up when going down one block ledge
    UtilEntity.launchDirection(entity, power, face); //this.playClickOnSound(worldIn, pos);
    if (doCorrections) {
      if (face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
        //then since +Z is south, and +X is east: so
        double xDiff = (pos.getX() + 0.5) - entity.posX;
        if (Math.abs(xDiff) > 0.09) {//max is .5
          if (xDiff < 0) {
            UtilEntity.launchDirection(entity, powerCorrection, EnumFacing.WEST);
          }
          else {
            UtilEntity.launchDirection(entity, powerCorrection, EnumFacing.EAST);
          }
        }
      }
      else if (face == EnumFacing.EAST || face == EnumFacing.WEST) {
        //then since +Z is south, and +X is east: so
        double diff = (pos.getZ() + 0.5) - entity.posZ;
        //??NOPE  &&  ((int) entity.posZ) == entity.getPosition().getZ()
        if (Math.abs(diff) > 0.09) {//max is .5
          if (diff < 0) {
            UtilEntity.launchDirection(entity, powerCorrection, EnumFacing.NORTH);
          }
          else {
            UtilEntity.launchDirection(entity, powerCorrection, EnumFacing.SOUTH);
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
    // find the quadrant the player is facing
    EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
    return this.getDefaultState().withProperty(PROPERTYFACING, enumfacing);
  }

  @Override
  public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
    return false;
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
    if (placer.isSneaking()) {
      //skip and place as normal 
      return;
    }
    IBlockState north = world.getBlockState(pos.offset(EnumFacing.NORTH));
    IBlockState south = world.getBlockState(pos.offset(EnumFacing.SOUTH));
    IBlockState west = world.getBlockState(pos.offset(EnumFacing.WEST));
    IBlockState east = world.getBlockState(pos.offset(EnumFacing.EAST));
    boolean isNorth = north.getBlock() instanceof BlockConveyor;
    boolean isSouth = south.getBlock() instanceof BlockConveyor;
    boolean isWest = west.getBlock() instanceof BlockConveyor;
    boolean isEast = east.getBlock() instanceof BlockConveyor;
    IBlockState northUp = world.getBlockState(pos.offset(EnumFacing.NORTH).up());
    IBlockState southUp = world.getBlockState(pos.offset(EnumFacing.SOUTH).up());
    IBlockState westUp = world.getBlockState(pos.offset(EnumFacing.WEST).up());
    IBlockState eastUp = world.getBlockState(pos.offset(EnumFacing.EAST).up());
    //
    boolean isNorthUp = northUp.getBlock() instanceof BlockConveyor;
    boolean isSouthUp = southUp.getBlock() instanceof BlockConveyor;
    boolean isWestUp = westUp.getBlock() instanceof BlockConveyor;
    boolean isEastUp = eastUp.getBlock() instanceof BlockConveyor;
    BlockPos posTarget = new BlockPos(pos);
    IBlockState targetState = null;
    // auto place angle ramps
    boolean flip = false;
    if (isEastUp) {
      flip = eastUp.getValue(PROPERTYFACING) == EnumFacing.WEST;
      if (eastUp.getBlock() instanceof BlockConveyorAngle)
        flip = flip || eastUp.getValue(BlockConveyorAngle.FLIPPED);
      targetState = getAngled().getDefaultState().withProperty(PROPERTYFACING, EnumFacing.EAST)
          .withProperty(BlockConveyorAngle.FLIPPED, flip);
    }
    else if (isWestUp) {
      flip = westUp.getValue(PROPERTYFACING) == EnumFacing.EAST;
      if (westUp.getBlock() instanceof BlockConveyorAngle)
        flip = flip || westUp.getValue(BlockConveyorAngle.FLIPPED);
      targetState = getAngled().getDefaultState().withProperty(PROPERTYFACING, EnumFacing.WEST)
          .withProperty(BlockConveyorAngle.FLIPPED, flip);
    }
    else if (isSouthUp) {
      flip = southUp.getValue(PROPERTYFACING) == EnumFacing.NORTH;
      if (southUp.getBlock() instanceof BlockConveyorAngle)
        flip = flip || southUp.getValue(BlockConveyorAngle.FLIPPED);
      targetState = getAngled().getDefaultState().withProperty(PROPERTYFACING, EnumFacing.SOUTH)
          .withProperty(BlockConveyorAngle.FLIPPED, flip);
    }
    else if (isNorthUp) {
      if (northUp.getBlock() instanceof BlockConveyorAngle)
        flip = flip || northUp.getValue(BlockConveyorAngle.FLIPPED);
      flip = northUp.getValue(PROPERTYFACING) == EnumFacing.SOUTH;
      targetState = getAngled().getDefaultState().withProperty(PROPERTYFACING, EnumFacing.NORTH)
          .withProperty(BlockConveyorAngle.FLIPPED, flip);
    }
    else if (isNorth && isWest) {
      if (west.getValue(PROPERTYFACING) == EnumFacing.EAST) {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, west.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true);
      }
      else {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, north.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false);
      }
    } //auto place corners
    else if (isNorth && isEast) {
      if (east.getValue(PROPERTYFACING) == EnumFacing.WEST) {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, east.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false);
      }
      else {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, north.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true);
      }
    }
    else if (isSouth && isEast) {
      if (south.getValue(PROPERTYFACING) == EnumFacing.NORTH) {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, south.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false);
      }
      else {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, east.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true);
      }
    }
    else if (isSouth && isWest) {
      if (west.getValue(PROPERTYFACING) == EnumFacing.WEST) {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, south.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, true);
      }
      else {
        targetState = getCorner().getDefaultState().withProperty(PROPERTYFACING, west.getValue(PROPERTYFACING))
            .withProperty(BlockConveyorCorner.FLIPPED, false);
      }
    }
    //straight up
    else if (isSouth) {
      targetState = this.getDefaultState().withProperty(PROPERTYFACING, getFacingDir(south));
    }
    else if (isNorth) {
      targetState = this.getDefaultState().withProperty(PROPERTYFACING, getFacingDir(north));
    }
    else if (isWest) {
      targetState = this.getDefaultState().withProperty(PROPERTYFACING, getFacingDir(west));
    }
    else if (isEast) {
      targetState = this.getDefaultState().withProperty(PROPERTYFACING, getFacingDir(east));
    }
    //fire away
    if (targetState != null) {
      world.setBlockState(posTarget, targetState);
    }
  }

  private EnumFacing getFacingDir(IBlockState st) {
    EnumFacing f = st.getValue(PROPERTYFACING);
    if (st.getBlock() instanceof BlockConveyorAngle && st.getValue(BlockConveyorAngle.FLIPPED)) {
      f = f.getOpposite();
    }
    return f;
  }

  public BlockConveyor getCorner() {
    return corner;
  }

  public void setCorner(BlockConveyor corner) {
    this.corner = corner;
  }

  public BlockConveyor getAngled() {
    return angled;
  }

  public void setAngled(BlockConveyor angled) {
    this.angled = angled;
  }
}
