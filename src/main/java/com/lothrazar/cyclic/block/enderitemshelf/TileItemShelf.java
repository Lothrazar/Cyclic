package com.lothrazar.cyclic.block.enderitemshelf;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemShelf extends TileEntityBase {

  public final ItemStackHandler inventory = new ClientAutoSyncItemHandler(this, EnderShelfItemHandler.ROWS);
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> inventory);
  public RenderTextType renderStyle = RenderTextType.STACK;

  public TileItemShelf(BlockPos pos, BlockState state) {
    super(TileRegistry.ENDER_ITEM_SHELF.get(),pos,state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("RenderTextType")) {
      int rt = tag.getInt("RenderTextType");
      this.renderStyle = RenderTextType.values()[rt];
    }
    super.load(bs, tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("RenderTextType", this.renderStyle.ordinal());
    return super.save(tag);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }
}
