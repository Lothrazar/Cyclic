package com.lothrazar.cyclic.block.expcollect;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.library.cap.item.FluidHandlerCapabilityStack;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class BlockExpPylon extends BlockCyclic {

  public BlockExpPylon(Properties properties) {
    super(properties.strength(1.8F).sound(SoundType.GLASS).noOcclusion());
    this.setHasGui();
    this.setHasFluidInteract();
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.EXPERIENCE_PYLON.get(), ScreenExpPylon::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileExpPylon(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.EXPERIENCE_PYLON.get(), world.isClientSide ? TileExpPylon::clientTick : TileExpPylon::serverTick);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
    //because harvestBlock manually forces a drop 
    return new ArrayList<>();
  }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    try {
      IFluidHandlerItem storage = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
      BlockEntity container = world.getBlockEntity(pos);
      if (storage != null && container != null) {
        IFluidHandler storageTile = container.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
        if (storageTile != null) {
          storageTile.fill(storage.getFluidInTank(0), FluidAction.EXECUTE);
        }
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error during fill from item ", e);
    }
    //set default state
    //    state = state.setValue(TANK_ABOVE, false).setValue(TANK_BELOW, false);
    world.setBlockAndUpdate(pos, state);
  }

  @Override
  public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, BlockEntity ent, ItemStack stackTool) {
    super.playerDestroy(world, player, pos, state, ent, stackTool);
    ItemStack tankStack = new ItemStack(this);
    if (ent != null) {
      IFluidHandler fluidInStack = tankStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
      if (fluidInStack != null && ent instanceof TileExpPylon) {
        // push fluid from dying tank to itemstack
        TileExpPylon ttank = (TileExpPylon) ent;
        FluidStack fs = ttank.tank.getFluid();
        ((FluidHandlerCapabilityStack) fluidInStack).setFluid(fs);
      }
    }
    ItemStackUtil.dropItemStackMotionless(world, pos, tankStack);
  }
}
