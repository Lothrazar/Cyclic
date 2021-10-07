package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.capability.FluidHandlerCapabilityStack;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockFluidTank extends BlockBase {

  public static final BooleanProperty TANK_ABOVE = BooleanProperty.create("above");
  public static final BooleanProperty TANK_BELOW = BooleanProperty.create("below");

  public BlockFluidTank(Properties properties) {
    super(properties.harvestTool(ToolType.PICKAXE).strength(1.2F).noOcclusion());
    this.setHasFluidInteract();
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!player.isCrouching() && player.getItemInHand(hand).getItem() == this.asItem()
        && (hit.getDirection() == Direction.UP || hit.getDirection() == Direction.DOWN)) {
      //pass to allow quick building up and down
      return InteractionResult.PASS;
    }
    return super.use(state, world, pos, player, hand, hit);
  }

  @Override
  public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1.0f;
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return false;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(TANK_ABOVE, TANK_BELOW);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
    boolean tileAbove = world.getBlockEntity(pos.above()) instanceof TileTank;
    boolean tileBelow = world.getBlockEntity(pos.below()) instanceof TileTank;
    return state
        .setValue(TANK_ABOVE, tileAbove)
        .setValue(TANK_BELOW, tileBelow);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileTank();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.translucent());
    ClientRegistry.bindTileEntityRenderer(TileRegistry.tank, RenderTank::new);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootContext.Builder builder) {
    //because harvestBlock manually forces a drop 
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IFluidHandlerItem storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      BlockEntity container = world.getBlockEntity(pos);
      if (storage != null && container != null) {
        IFluidHandler storageTile = container.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
        if (storageTile != null) {
          storageTile.fill(storage.getFluidInTank(0), FluidAction.EXECUTE);
        }
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error during fill from item ", e);
    }
    //set default state
    state = state.setValue(TANK_ABOVE, false).setValue(TANK_BELOW, false);
    world.setBlockAndUpdate(pos, state);
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stackTool) {
    super.playerDestroy(world, player, pos, state, ent, stackTool);
    ItemStack tankStack = new ItemStack(this);
    if (ent != null) {
      IFluidHandler fluidInTile = ent.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
      // note a DIFFERENT cap here for the item
      IFluidHandler fluidInStack = tankStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      if (fluidInStack != null) {
        //now give 
        FluidStack fs = fluidInTile.getFluidInTank(0);
        ((FluidHandlerCapabilityStack) fluidInStack).setFluid(fs);
      }
    }
    if (world.isClientSide == false) {
      world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tankStack));
    }
  }
}
