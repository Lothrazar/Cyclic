package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.items.IItemHandler;

public class TileEnderShelf extends TileBlockEntityCyclic {

  public final EnderShelfItemHandler inventory = new EnderShelfItemHandler(this);
  public RenderTextType renderStyle = RenderTextType.TEXT;

  public static enum RenderTextType {
    TEXT, STACK, NONE;
  }

  public TileEnderShelf(BlockPos pos, BlockState state) {
    super(TileRegistry.ENDER_SHELF.get(), pos, state);
    this.needsRedstone = 0;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    int rt = input.getIntOr("RenderTextType", 0);
    this.renderStyle = RenderTextType.values()[rt];
    inventory.resetNameCache();
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    output.putInt("RenderTextType", this.renderStyle.ordinal());
    super.saveAdditional(output);
  }

  public void toggleShowText() {
    int ord = renderStyle.ordinal() + 1;
    if (ord == RenderTextType.values().length) {
      ord = 0;
    }
    this.renderStyle = RenderTextType.values()[ord];
  }
}
