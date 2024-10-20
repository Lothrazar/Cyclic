package com.lothrazar.cyclic.block.tank;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.capability.FluidHandlerCapabilityStack;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockFluidTank extends BlockBase {

  public static final BooleanProperty TANK_ABOVE = BooleanProperty.create("above");
  public static final BooleanProperty TANK_BELOW = BooleanProperty.create("below");

  public BlockFluidTank(Properties properties) {
    super(properties.harvestTool(ToolType.PICKAXE).hardnessAndResistance(1.2F).notSolid());
    this.setHasFluidInteract();
  }

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return true;
  }

  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    return calcRedstoneFromFluid(worldIn.getTileEntity(pos));
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!player.isCrouching() && player.getHeldItem(hand).getItem() == this.asItem()
        && (hit.getFace() == Direction.UP || hit.getFace() == Direction.DOWN)) {
      //pass to allow quick building up and down
      return ActionResultType.PASS;
    }
    return super.onBlockActivated(state, world, pos, player, hand, hit);
  }

  @Override
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1.0f;
  }

  @Override
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    return false;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(TANK_ABOVE, TANK_BELOW);
  }

  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
    boolean tileAbove = world.getTileEntity(pos.up()) instanceof TileTank;
    boolean tileBelow = world.getTileEntity(pos.down()) instanceof TileTank;
    return state
        .with(TANK_ABOVE, tileAbove)
        .with(TANK_BELOW, tileBelow);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileTank();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
    ClientRegistry.bindTileEntityRenderer(TileRegistry.tank, RenderTank::new);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, net.minecraft.loot.LootContext.Builder builder) {
    //because harvestBlock manually forces a drop 
    return new ArrayList<>();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IFluidHandlerItem storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      TileEntity container = world.getTileEntity(pos);
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
    state = state.with(TANK_ABOVE, false).with(TANK_BELOW, false);
    world.setBlockState(pos, state);
  }

  @Override
  public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity ent, ItemStack stackTool) {
    super.harvestBlock(world, player, pos, state, ent, stackTool);
    ItemStack tankStack = new ItemStack(this);
    if (ent instanceof TileTank) {
      TileTank tile = (TileTank) ent;
      IFluidHandler fluidInStack = tankStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      if (fluidInStack != null) {
        //now give 
        FluidStack fs = tile.tank.getFluidInTank(0);
        ((FluidHandlerCapabilityStack) fluidInStack).setFluid(fs);
      }
    }
    if (world.isRemote == false) {
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tankStack));
    }
  }
}
