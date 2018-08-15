package com.lothrazar.cyclicmagic.block.cablepump;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.IFacingBlacklist;
import com.lothrazar.cyclicmagic.block.cable.BlockCableBase;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.core.block.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class BlockPump extends BlockBaseFacingOmni {

  public enum EnumConnectType implements IStringSerializable {
    NONE, CABLE, BLOCKED;

    @Override
    public String getName() {
      return this.name().toLowerCase();
    }
  }

  private static final double SIZE = 0.875D;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(1 - SIZE, 1 - SIZE, 1 - SIZE, SIZE, SIZE, SIZE);
  /**
   * Virtual properties used for the multipart cable model and determining the presence of adjacent inventories
   */
  protected static final Map<EnumFacing, PropertyEnum<EnumConnectType>> PROPERTIES = Maps.newEnumMap(
      new ImmutableMap.Builder<EnumFacing, PropertyEnum<EnumConnectType>>()
          .put(EnumFacing.DOWN, PropertyEnum.create("down", EnumConnectType.class))
          .put(EnumFacing.UP, PropertyEnum.create("up", EnumConnectType.class))
          .put(EnumFacing.NORTH, PropertyEnum.create("north", EnumConnectType.class))
          .put(EnumFacing.SOUTH, PropertyEnum.create("south", EnumConnectType.class))
          .put(EnumFacing.WEST, PropertyEnum.create("west", EnumConnectType.class))
          .put(EnumFacing.EAST, PropertyEnum.create("east", EnumConnectType.class))
          .build());
  private boolean itemTransport = false;
  private boolean fluidTransport = false;
  private boolean powerTransport = false;

  public BlockPump() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(3F);
    this.setHarvestLevel("pickaxe", 1);
    this.setTranslucent();
    setSoundType(SoundType.CLOTH);
    this.placeType = PlacementType.SIDE_BLOCK;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  public void setItemTransport() {
    this.itemTransport = true;
  }

  public void setFluidTransport() {
    this.fluidTransport = true;
  }

  public void setPowerTransport() {
    this.powerTransport = true;
  }

  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if (side == EnumFacing.UP || side == EnumFacing.DOWN)
      return false;//allows, for example, you to open chest that is directly below
    return true;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);
    for (PropertyEnum<EnumConnectType> property : PROPERTIES.values()) {
      builder.add(property);
    }
    builder.add(BlockDirectional.FACING);
    return builder.build();
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileEntityBasePump te = (TileEntityBasePump) world.getTileEntity(pos);
    if (te != null) {
      if (BlockCableBase.isWrenchItem(player.getHeldItem(hand))) {
        EnumFacing sideToToggle = null;
        if (hitX < BlockCableBase.hitLimit) {
          sideToToggle = EnumFacing.WEST;
        }
        else if (hitX > 1 - BlockCableBase.hitLimit) {
          sideToToggle = EnumFacing.EAST;
        }
        else if (hitY < BlockCableBase.hitLimit) {
          sideToToggle = EnumFacing.DOWN;
        }
        else if (hitY > 1 - BlockCableBase.hitLimit) {
          sideToToggle = EnumFacing.UP;
        }
        else if (hitZ < BlockCableBase.hitLimit) {
          sideToToggle = EnumFacing.NORTH;
        }
        else if (hitZ > 1 - BlockCableBase.hitLimit) {
          sideToToggle = EnumFacing.SOUTH;
        }
        else {
          sideToToggle = side;
        }
        if (sideToToggle != null) {
          te.toggleBlacklist(sideToToggle);
          boolean theNew = te.getBlacklist(sideToToggle);
          world.setBlockState(pos, state.withProperty(PROPERTIES.get(sideToToggle), (theNew) ? EnumConnectType.BLOCKED : EnumConnectType.NONE));
          UtilChat.sendStatusMessage(player, UtilChat.lang("cable.block.toggled." + theNew) + sideToToggle.name().toLowerCase());
        }
        return true;
      }
      //      else if (world.isRemote == false && hand == EnumHand.MAIN_HAND) {
      //         UtilChat.sendStatusMessage(player, te.getLabelTextOrEmpty());
      //      }
    }
    return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos origin) {
    BlockPos pos = new BlockPos(origin);
    IFacingBlacklist pumpHere = (IFacingBlacklist) world.getTileEntity(pos);
    for (EnumFacing side : EnumFacing.VALUES) {
      PropertyEnum<EnumConnectType> property = PROPERTIES.get(side);
      if (pumpHere != null && pumpHere.getBlacklist(side)) {
        //im blocked off, so i cant connect to you
        state = state.withProperty(property, EnumConnectType.BLOCKED);
        continue;
      }
      pos = origin.offset(side);
      state = state.withProperty(property, EnumConnectType.NONE);
      TileEntity tileTarget = world.getTileEntity(pos);
      TileEntityCableBase tileCable = null;
      if (tileTarget != null && tileTarget instanceof TileEntityCableBase) {
        tileCable = (TileEntityCableBase) tileTarget;
      }
      if (tileCable != null && tileCable.getBlacklist(side.getOpposite())) {
        //you are blocked on your facing side, so i wont push out to you
        continue;
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
          state = state.withProperty(property, EnumConnectType.CABLE);
        }
        if (tileTarget != null &&
            tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite())) {
          state = state.withProperty(property, EnumConnectType.CABLE);
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
          state = state.withProperty(property, EnumConnectType.CABLE);
        }
        //        if (side == EnumFacing.NORTH && this.fluidTransport)
        //          state = state.withProperty(property, EnumConnectType.BLOCKED);
      }
    }
    return super.getActualState(state, world, origin);
  }
}
