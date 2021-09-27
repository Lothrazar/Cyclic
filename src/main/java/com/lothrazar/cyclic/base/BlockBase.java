package com.lothrazar.cyclic.base;

import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockBase extends Block {

  public static final BooleanProperty LIT = BooleanProperty.create("lit");
  private boolean hasGui = false;
  private boolean hasFluidInteract = false;

  public BlockBase(Properties properties) {
    super(properties);
    BlockRegistry.blocksClientRegistry.add(this);
  }

  protected BlockBase setHasGui() {
    this.hasGui = true;
    return this;
  }

  protected BlockBase setHasFluidInteract() {
    this.hasFluidInteract = true;
    return this;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
      Direction oldDir = state.get(BlockStateProperties.HORIZONTAL_FACING);
      Direction newDir = direction.rotate(oldDir);
      //still rotate on axis, if its valid
      if (newDir != Direction.UP && newDir != Direction.DOWN) {
        return state.with(BlockStateProperties.HORIZONTAL_FACING, newDir);
      }
    }
    if (state.hasProperty(BlockStateProperties.FACING)) {
      Direction oldDir = state.get(BlockStateProperties.FACING);
      Direction newDir = direction.rotate(oldDir);
      // rotate state on axis dir
      return state.with(BlockStateProperties.FACING, newDir);
    }
    // default doesnt do much
    BlockState newState = state.rotate(direction);
    return newState;
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (hasFluidInteract) {
      if (!world.isRemote) {
        TileEntity tankHere = world.getTileEntity(pos);
        if (tankHere != null) {
          IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace()).orElse(null);
          if (handler != null) {
            if (FluidUtil.interactWithFluidHandler(player, hand, handler)) {
              //success so display new amount
              if (handler.getFluidInTank(0) != null) {
                player.sendStatusMessage(new TranslationTextComponent(getFluidRatioName(handler)), true);
              }
              //and also play the fluid sound
              if (player instanceof ServerPlayerEntity) {
                UtilSound.playSoundFromServer((ServerPlayerEntity) player, SoundEvents.ITEM_BUCKET_FILL);
              }
            }
            else {
              player.sendStatusMessage(new TranslationTextComponent(getFluidRatioName(handler)), true);
            }
          }
        }
      }
      if (FluidUtil.getFluidHandler(player.getHeldItem(hand)).isPresent()) {
        return ActionResultType.SUCCESS;
      }
    }
    if (this.hasGui) {
      if (!world.isRemote) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
          NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
        }
        else {
          throw new IllegalStateException("Our named container provider is missing!");
        }
      }
      return ActionResultType.SUCCESS;
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
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
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity != null) {
        IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (items != null) {
          for (int i = 0; i < items.getSlots(); ++i) {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
          }
          worldIn.updateComparatorOutputLevel(pos, this);
        }
      }
      super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    TranslationTextComponent t = new TranslationTextComponent(getTranslationKey() + ".tooltip");
    t.mergeStyle(TextFormatting.GRAY);
    tooltip.add(t);
  }

  /**
   * Override per block for render-ers/screens/etc
   */
  public void registerClient() {}

  public static boolean isItem(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
  }

  public static boolean isFluid(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  public static boolean isEnergy(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return hasCapabilityDir(facing, world, facingPos, CapabilityEnergy.ENERGY);
  }

  private static boolean hasCapabilityDir(Direction facing, IWorld world, BlockPos facingPos, Capability<?> cap) {
    if (facing == null) {
      return false;
    }
    TileEntity neighbor = world.getTileEntity(facingPos);
    if (neighbor != null
        && neighbor.getCapability(cap, facing.getOpposite()).orElse(null) != null) {
      return true;
    }
    return false;
  }
}
