package com.lothrazar.cyclic.block.enderitemshelf;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;


public class TileItemShelf extends TileBlockEntityCyclic {

  public final ItemStackHandler inventory = new ClientAutoSyncItemHandler(this, EnderShelfItemHandler.ROWS);

  public RenderTextType renderStyle = RenderTextType.STACK;

  public TileItemShelf(BlockPos pos, BlockState state) {
    super(TileRegistry.SHELF.get(), pos, state);
  }


  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }


  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    if (tag.contains("RenderTextType")) {
      int rt = tag.getInt("RenderTextType");
      this.renderStyle = RenderTextType.values()[rt];
    }
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag,HolderLookup.Provider registries) {
    tag.put(NBTINV, inventory.serializeNBT(registries));
    tag.putInt("RenderTextType", this.renderStyle.ordinal());
    super.saveAdditional(tag,registries);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
