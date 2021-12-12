package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEnderShelf extends TileEntityBase {

  public final EnderShelfItemHandler inventory = new EnderShelfItemHandler(this);
  private final LazyOptional<EnderShelfItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  public RenderTextType renderStyle = RenderTextType.TEXT;

  public static enum RenderTextType {
    TEXT, STACK, NONE;
  }

  public TileEnderShelf() {
    super(TileRegistry.ender_shelf);
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
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    if (tag.contains("RenderTextType")) {
      int rt = tag.getInt("RenderTextType");
      this.renderStyle = RenderTextType.values()[rt];
    }
    inventory.resetNameCache();
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("RenderTextType", this.renderStyle.ordinal());
    return super.write(tag);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }
}
