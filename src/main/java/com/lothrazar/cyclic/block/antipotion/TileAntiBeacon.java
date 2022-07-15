package com.lothrazar.cyclic.block.antipotion;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileAntiBeacon extends TileBlockEntityCyclic {

  public static IntValue RADIUS;
  public static IntValue TICKS;
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return true; // stack.isFood();
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(0);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileAntiBeacon(BlockPos pos, BlockState state) {
    super(TileRegistry.ANTI_BEACON.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAntiBeacon tile) {
    boolean powered = tile.isPowered();
    if (powered) {
      return;
    }
    //ok go
    tile.timer--;
    if (tile.timer <= 0) {
      BlockAntiBeacon.absorbPotions(level, blockPos);
      tile.timer = TICKS.get();
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileAntiBeacon e) {}
  //  public void tick() {
  //    boolean lit = this.getBlockState().getValue(BlockCyclic.LIT);
  //    //if we are going from unpowered to powered, meaning state isnt set but power is
  //    if (powered && !lit) {
  //      Direction currentFacing = this.getCurrentFacing();
  //      BlockPos target = worldPosition.relative(currentFacing);
  //      BlockState state = level.getBlockState(target);
  //      //
  //      if (!state.isAir() &&
  //          state.getDestroySpeed(level, target) >= 0) {
  //        //        boolean succ = 
  //        BlockUtil.rotateBlockValidState(level, target, currentFacing.getOpposite());
  //      }
  //    }
  //    //now powered and lit match
  //    setLitProperty(powered);
  //  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }
}
