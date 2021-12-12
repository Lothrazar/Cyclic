package com.lothrazar.cyclic.block.cable.fluid;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.ShapeCache;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockCableFluid extends CableBase {

  public BlockCableFluid(Properties properties) {
    super(properties.hardnessAndResistance(0.5F));
  }

  @Override
  public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    if (Screen.hasShiftDown()) {
      tooltip.add(new TranslationTextComponent("block.cyclic.fluid_pipe.tooltip0").mergeStyle(TextFormatting.GRAY));
    }
    else {
      tooltip.add(new TranslationTextComponent("item.cyclic.shift").mergeStyle(TextFormatting.DARK_GRAY));
    }
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.fluid_pipe, ScreenCableFluid::new);
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!world.isRemote) {
      TileEntity ent = world.getTileEntity(pos);
      IFluidHandler handlerHere = ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).resolve().orElse(null);
      //show current 
      if (handlerHere != null) {
        FluidStack fluid = handlerHere.getFluidInTank(0);
        int st = fluid.getAmount();
        if (st > 0) {
          player.sendStatusMessage(new TranslationTextComponent(st + " " + fluid.getDisplayName()), true);
        }
      }
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return ShapeCache.getOrCreate(state, CableBase::createShape);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCableFluid();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState stateIn, LivingEntity placer, ItemStack stack) {
    for (Direction d : Direction.values()) {
      TileEntity facingTile = worldIn.getTileEntity(pos.offset(d));
      IFluidHandler cap = facingTile == null ? null : facingTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, d.getOpposite()).resolve().orElse(null);
      if (cap != null) {
        stateIn = stateIn.with(FACING_TO_PROPERTY_MAP.get(d), EnumConnectType.INVENTORY);
        if (worldIn.setBlockState(pos, stateIn)) {
          updateConnection(worldIn, pos, d, EnumConnectType.INVENTORY);
        }
      }
    }
    super.onBlockPlacedBy(worldIn, pos, stateIn, placer, stack);
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileCableFluid tileentity = (TileCableFluid) worldIn.getTileEntity(pos);
      if (tileentity != null && tileentity.filter != null) {
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.filter.getStackInSlot(0));
      }
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    EnumProperty<EnumConnectType> property = FACING_TO_PROPERTY_MAP.get(facing);
    EnumConnectType oldProp = stateIn.get(property);
    if (oldProp.isBlocked() || oldProp.isExtraction()) {
      updateConnection(world, currentPos, facing, oldProp);
      return stateIn;
    }
    if (isFluid(stateIn, facing, facingState, world, currentPos, facingPos)) {
      BlockState with = stateIn.with(property, EnumConnectType.INVENTORY);
      if (world instanceof World && world.getBlockState(currentPos).getBlock() == this) {
        //hack to force {any} -> inventory IF its here
        if (((World) world).setBlockState(currentPos, with)) {
          updateConnection(world, currentPos, facing, EnumConnectType.INVENTORY);
        }
      }
      return with;
    }
    else {
      updateConnection(world, currentPos, facing, EnumConnectType.NONE);
      return stateIn.with(property, EnumConnectType.NONE);
    }
  }
}
