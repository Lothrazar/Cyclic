package com.lothrazar.cyclic.block.enderitemshelf;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;


public class TileItemShelf extends TileBlockEntityCyclic {

  public final ItemStackHandler inventory = new ClientAutoSyncItemHandler(this, EnderShelfItemHandler.ROWS);

  public RenderTextType renderStyle = RenderTextType.STACK;

  public TileItemShelf(BlockPos pos, BlockState state) {
    super(TileRegistry.SHELF.get(), pos, state);
    this.needsRedstone = 0;
  }


  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }


  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    int rt = input.getIntOr("RenderTextType", 0);
    this.renderStyle = RenderTextType.values()[rt];
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
