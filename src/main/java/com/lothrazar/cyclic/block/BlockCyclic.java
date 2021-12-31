package com.lothrazar.cyclic.block;

import java.util.List;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class BlockCyclic extends BaseEntityBlock {

  public static final BooleanProperty LIT = BooleanProperty.create("lit");
  private boolean hasGui = false;
  private boolean hasFluidInteract = false;

  public BlockCyclic(Properties properties) {
    super(properties);
    BlockRegistry.BLOCKSCLIENTREGISTRY.add(this);
  }

  @Override
  public RenderShape getRenderShape(BlockState st) {
    return RenderShape.MODEL;
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

  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (hasFluidInteract) {
      if (!world.isClientSide) {
        BlockEntity tankHere = world.getBlockEntity(pos);
        if (tankHere != null) {
          IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getDirection()).orElse(null);
          if (handler != null) {
            if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
              //success so display new amount
              if (handler.getFluidInTank(0) != null) {
                player.displayClientMessage(new TranslatableComponent(getFluidRatioName(handler)), true);
              }
              //and also play the fluid sound
              if (player instanceof ServerPlayer) {
                UtilSound.playSoundFromServer((ServerPlayer) player, SoundEvents.BUCKET_FILL);
              }
            }
            else {
              player.displayClientMessage(new TranslatableComponent(getFluidRatioName(handler)), true);
            }
          }
        }
      }
      if (FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent()) {
        return InteractionResult.SUCCESS;
      }
    }
    if (this.hasGui) {
      if (!world.isClientSide) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider) {
          NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
        }
        else {
          throw new IllegalStateException("Our named container provider is missing!");
        }
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  public static String getFluidRatioName(IFluidHandler handler) {
    String ratio = handler.getFluidInTank(0).getAmount() + "/" + handler.getTankCapacity(0);
    if (!handler.getFluidInTank(0).isEmpty()) {
      ratio += " " + handler.getFluidInTank(0).getDisplayName().getString();
    }
    return ratio;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity tileentity = worldIn.getBlockEntity(pos);
      if (tileentity != null) {
        IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
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

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
  }

  /**
   * Override per block for render-ers/screens/etc
   */
  public void registerClient() {}

  public static boolean isItem(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  public static boolean isFluid(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  public static boolean isEnergy(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityEnergy.ENERGY);
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
