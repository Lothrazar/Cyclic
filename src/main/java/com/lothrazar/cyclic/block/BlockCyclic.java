package com.lothrazar.cyclic.block;

import com.lothrazar.cyclic.block.facade.IBlockFacade;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.library.block.EntityBlockFlib;
import com.lothrazar.library.util.SoundUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class BlockCyclic extends EntityBlockFlib {

  public static final BooleanProperty LIT = BooleanProperty.create("lit");
  private boolean hasGui = false;
  private boolean hasFluidInteract = false;

  public BlockCyclic(Properties properties) {
    super(properties);
    BlockRegistry.BLOCKSCLIENTREGISTRY.add(this);

  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    if (this instanceof IBlockFacade) {
      //ok lets check
    }
    return super.getShape(state, level, pos, ctx);
  }

  public static boolean never(BlockState bs, BlockGetter bg, BlockPos pos) {
    return false;
  }

  protected BlockCyclic setHasGui() {
    this.hasGui = true;
    return this;
  }

  protected BlockCyclic setHasFluidInteract() {
    this.hasFluidInteract = true;
    return this;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos p, BlockState st) {
    return null;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      Direction oldDir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
      Direction newDir = direction.rotate(oldDir);
      //still rotate on axis, if its valid
      if (newDir != Direction.UP && newDir != Direction.DOWN) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, newDir);
      }
    }
    if (state.hasProperty(BlockStateProperties.FACING)) {
      Direction oldDir = state.getValue(BlockStateProperties.FACING);
      Direction newDir = direction.rotate(oldDir);
      // rotate state on axis dir
      return state.setValue(BlockStateProperties.FACING, newDir);
    }
    // default doesnt do much
    BlockState newState = state.rotate(direction);
    return newState;
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (hasFluidInteract) {
      if (!world.isClientSide) {
        BlockEntity tankHere = world.getBlockEntity(pos);
        if (tankHere != null) {
          IFluidHandler handler = tankHere.getCapability(ForgeCapabilities.FLUID_HANDLER, hit.getDirection()).orElse(null);
          if (handler != null) {
            if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
              if (player instanceof ServerPlayer) {
                SoundUtil.playSoundFromServer((ServerPlayer) player, SoundEvents.BUCKET_FILL, 1F, 1F);
              }
              //success so display new amount
              if (handler.getFluidInTank(0) != null) {
                displayClientFluidMessage(player, handler);
              }
            }
            else {
              displayClientFluidMessage(player, handler);
            }
          }
        }
      }
      if (FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent()) { // reverted to how 1.16.5 does it fix sapphys bug
        return InteractionResult.SUCCESS;
      }
    }
    if (this.hasGui) {
      if (!world.isClientSide) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider) {
          NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
        }
        else {
          throw new IllegalStateException("Our named container provider is missing!");
        }
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  private void displayClientFluidMessage(Player player, IFluidHandler handler) {
    if (ClientConfigCyclic.FLUID_BLOCK_STATUS.get()) {
      player.displayClientMessage(Component.translatable(StringParseUtil.getFluidRatioName(handler)), true);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity tileentity = worldIn.getBlockEntity(pos);
      if (tileentity != null) {
        IItemHandler items = tileentity.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        if (items != null) {
          for (int i = 0; i < items.getSlots(); ++i) {
            Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
          }
          worldIn.updateNeighbourForOutputSignal(pos, this);
        }
      }
      super.onRemove(state, worldIn, pos, newState, isMoving);
    }
  }

  /**
   * Override per block for render-ers/screens/etc
   */
  public void registerClient() {}

  public static boolean isItem(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, ForgeCapabilities.ITEM_HANDLER);
  }

  public static boolean isFluid(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, ForgeCapabilities.FLUID_HANDLER);
  }

  public static boolean isEnergy(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, ForgeCapabilities.ENERGY);
  }

  private static boolean hasCapabilityDir(Direction facing, LevelAccessor world, BlockPos facingPos, Capability<?> cap) {
    if (facing == null) {
      return false;
    }
    BlockEntity neighbor = world.getBlockEntity(facingPos);
    if (neighbor != null
        && neighbor.getCapability(cap, facing.getOpposite()).orElse(null) != null) {
      return true;
    }
    return false;
  }
}
