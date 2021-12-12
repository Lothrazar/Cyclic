package com.lothrazar.cyclic.block.tankcask;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.capability.FluidHandlerCapabilityStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BlockCask extends BlockBase {

  public BlockCask(Properties properties) {
    super(properties.harvestTool(ToolType.PICKAXE).hardnessAndResistance(1.2F));
    this.setHasFluidInteract();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileCask();
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
      //
      ModCyclic.LOGGER.error("Error during fill from item ", e);
    }
    //set default state
    //    state = state.with(TANK_ABOVE, false).with(TANK_BELOW, false);
    world.setBlockState(pos, state);
  }

  @Override
  public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity ent, ItemStack stack) {
    super.harvestBlock(world, player, pos, state, ent, stack);
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
    if (!world.isRemote) {
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tankStack));
    }
  }
}
