package com.lothrazar.cyclic.block.enderitemshelf;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemShelf extends TileBlockEntityCyclic {

  public final ItemStackHandler inventory = new ClientAutoSyncItemHandler(this, EnderShelfItemHandler.ROWS);
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> inventory);
  public RenderTextType renderStyle = RenderTextType.STACK;

  public TileItemShelf(BlockPos pos, BlockState state) {
    super(TileRegistry.ENDER_ITEM_SHELF.get(), pos, state);
  }

  //  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileItemShelf e) {
  //    e.tick();
  //  }
  //
  //  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileItemShelf e) {
  //    e.tick();
  //  }
  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("RenderTextType")) {
      int rt = tag.getInt("RenderTextType");
      this.renderStyle = RenderTextType.values()[rt];
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("RenderTextType", this.renderStyle.ordinal());
    super.saveAdditional(tag);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }
}
