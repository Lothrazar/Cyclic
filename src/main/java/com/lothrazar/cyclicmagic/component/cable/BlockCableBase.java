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
package com.lothrazar.cyclicmagic.component.cable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.component.cable.item.TileEntityItemCable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * 
 * @author insomniaKitten
 *
 */
@SuppressWarnings("deprecation")
public abstract class BlockCableBase extends BlockBaseHasTile {
  /**
   * Virtual properties used for the multipart cable model and determining the presence of adjacent inventories
   */
  public static final Map<EnumFacing, PropertyEnum<EnumConnectType>> PROPERTIES = Maps.newEnumMap(
      new ImmutableMap.Builder<EnumFacing, PropertyEnum<EnumConnectType>>()
          .put(EnumFacing.DOWN, PropertyEnum.create("down", EnumConnectType.class))
          .put(EnumFacing.UP, PropertyEnum.create("up", EnumConnectType.class))
          .put(EnumFacing.NORTH, PropertyEnum.create("north", EnumConnectType.class))
          .put(EnumFacing.SOUTH, PropertyEnum.create("south", EnumConnectType.class))
          .put(EnumFacing.WEST, PropertyEnum.create("west", EnumConnectType.class))
          .put(EnumFacing.EAST, PropertyEnum.create("east", EnumConnectType.class))
          .build());
  public static final AxisAlignedBB AABB_NONE = new AxisAlignedBB(0.375D, 0.375D, 0.375D, 0.625D, 0.625D, 0.625D);
  public static final Map<EnumFacing, AxisAlignedBB> AABB_SIDES = Maps.newEnumMap(
      new ImmutableMap.Builder<EnumFacing, AxisAlignedBB>()
          .put(EnumFacing.DOWN, new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D))
          .put(EnumFacing.UP, new AxisAlignedBB(0.375D, 0.625D, 0.375D, 0.625D, 1.0D, 0.625D))
          .put(EnumFacing.NORTH, new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.375D))
          .put(EnumFacing.SOUTH, new AxisAlignedBB(0.375D, 0.375D, 0.625D, 0.625D, 0.625D, 1.0D))
          .put(EnumFacing.WEST, new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.375D, 0.625D, 0.625D))
          .put(EnumFacing.EAST, new AxisAlignedBB(0.625D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D))
          .build());
  public enum EnumConnectType implements IStringSerializable {
    NONE, CABLE, INVENTORY;
    @Override
    public String getName() {
      return this.name().toLowerCase();
    }
  }
  public BlockCableBase() {
    super(Material.CLOTH);
    setDefaultState(getDefaultState());
    setSoundType(SoundType.CLOTH);
    setHardness(0.5F);
    setResistance(2.5F);
    setLightOpacity(0);
  }
  public abstract TileEntity createTileEntity(World world, IBlockState state);
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileEntityCableBase te = (TileEntityCableBase) world.getTileEntity(pos);
    if (te != null && world.isRemote == false) {
      String msg = te.getLabelTextOrEmpty();
      UtilChat.sendStatusMessage(player, msg);
    }
    // otherwise return true if it is a fluid handler to prevent in world placement    
    return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityItemCable) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityItemCable) tileentity);
    }
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState();
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos origin) {
    BlockPos pos = new BlockPos(origin);
    for (EnumFacing side : EnumFacing.VALUES) {
      pos = origin.offset(side);
      PropertyEnum<EnumConnectType> property = PROPERTIES.get(side);
      state = state.withProperty(property, EnumConnectType.NONE);
      TileEntity tileTarget = world.getTileEntity(pos);
      TileEntityCableBase tileCable = null;
      if (tileTarget != null && tileTarget instanceof TileEntityCableBase) {
        tileCable = (TileEntityCableBase) tileTarget;
      }
      if (this.powerTransport) {
        if (tileCable != null && tileCable.isEnergyPipe()) {
          state = state.withProperty(property, EnumConnectType.CABLE);
        }
        if (tileTarget != null &&
            tileTarget.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
          state = state.withProperty(property, EnumConnectType.CABLE);
        }
      }
      if (this.itemTransport) {
        if (tileCable != null && tileCable.isItemPipe()) {
          state = state.withProperty(property, EnumConnectType.INVENTORY);
        }
        if (tileTarget != null &&
            tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite())) {
          state = state.withProperty(property, EnumConnectType.INVENTORY);
        }
      }
      if (this.fluidTransport) {
        if (tileCable != null && tileCable.isFluidPipe()) {
          state = state.withProperty(property, EnumConnectType.CABLE);
        }
        // getFluidHandler uses fluid capability and other things
        // CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        //        if (world instanceof World && FluidUtil.getFluidHandler((World) world, pos, side.getOpposite()) != null) {
        if (tileTarget != null &&
            tileTarget.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())) {
          state = state.withProperty(property, EnumConnectType.INVENTORY);
        }
      }
    }
    return super.getActualState(state, world, origin);
  }
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return world.getBlockState(pos.offset(side)).getBlock() != this
        && super.shouldSideBeRendered(state, world, pos, side);
  }
  @Override
  public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NONE);
    if (!isActualState) state = state.getActualState(world, pos);
    for (EnumFacing side : EnumFacing.VALUES) {
      if (state.getValue(PROPERTIES.get(side)) != EnumConnectType.NONE) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SIDES.get(side));
      }
    }
  }
  @Override
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
    AxisAlignedBB box = AABB_NONE.offset(pos);
    state = state.getActualState(world, pos);
    for (EnumFacing side : EnumFacing.VALUES) {
      if (state.getValue(PROPERTIES.get(side)) != EnumConnectType.NONE) {
        box = box.union(AABB_SIDES.get(side).offset(pos));
      }
    }
    return box;
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  @Override
  public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
    List<AxisAlignedBB> boxes = Lists.newArrayList(AABB_NONE);
    state = state.getActualState(world, pos);
    for (EnumFacing side : EnumFacing.VALUES) {
      if (state.getValue(PROPERTIES.get(side)) != EnumConnectType.NONE) {
        boxes.add(AABB_SIDES.get(side));
      }
    }
    List<RayTraceResult> results = new ArrayList<>();
    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    Vec3d a = start.subtract(x, y, z);
    Vec3d b = end.subtract(x, y, z);
    for (AxisAlignedBB box : boxes) {
      RayTraceResult result = box.calculateIntercept(a, b);
      if (result != null) {
        Vec3d vec = result.hitVec.addVector(x, y, z);
        results.add(new RayTraceResult(vec, result.sideHit, pos));
      }
    }
    RayTraceResult ret = null;
    double sqrDis = 0.0D;
    for (RayTraceResult result : results) {
      double newSqrDis = result.hitVec.squareDistanceTo(end);
      if (newSqrDis > sqrDis) {
        ret = result;
        sqrDis = newSqrDis;
      }
    }
    return ret;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }
  @Override
  protected BlockStateContainer createBlockState() {
    BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
    for (PropertyEnum<EnumConnectType> property : PROPERTIES.values()) {
      builder.add(property);
    }
    return builder.build();
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }
  private boolean itemTransport = false;
  private boolean fluidTransport = false;
  private boolean powerTransport = false;
  public void setItemTransport() {
    this.itemTransport = true;
  }
  public void setFluidTransport() {
    this.fluidTransport = true;
  }
  public void setPowerTransport() {
    this.powerTransport = true;
  }
}
