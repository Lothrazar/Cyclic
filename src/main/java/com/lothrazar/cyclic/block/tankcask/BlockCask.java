package com.lothrazar.cyclic.block.tankcask;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


public class BlockCask extends BlockCyclic {

  public BlockCask(Properties properties) {
    super(properties.strength(1.2F));
    this.setHasFluidInteract();
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState bs) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState st, Level level, BlockPos pos, Direction direction) {
    return calcRedstoneFromFluid(level.getBlockEntity(pos));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileCask(pos, state);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    //because harvestBlock manually forces a drop 
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IFluidHandler storage = CapabilityUtil.fluid(stack);// stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);

      IFluidHandler storageTile = CapabilityUtil.fluid(world,pos);
      if (storage != null && storageTile != null) {

          storageTile.fill(storage.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE);

      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error during fill from item ", e);
    }
    //set default state 
    world.setBlockAndUpdate(pos, state);
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stack) {
    super.playerDestroy(world, player, pos, state, ent, stack);
    ItemStack tankStack = new ItemStack(this);
    if (ent != null) {
      IFluidHandler fluidInStack = CapabilityUtil.fluid(tankStack);// tankStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
      if (fluidInStack != null && ent instanceof TileCask cask) {
        // push fluid from dying tank to itemstack 
        FluidStack fs = cask.tank.getFluid();
        fluidInStack.fill(fs, IFluidHandler.FluidAction.EXECUTE);
//        ((FluidHandlerCapabilityStack) fluidInStack).setFluid(fs);
      }
    }
    ItemStackUtil.dropItemStackMotionless(world, pos, tankStack);
  }
}
