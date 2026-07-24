package com.lothrazar.cyclic.block.enderctrl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.endershelf.TileEnderShelf.RenderTextType;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.state.BlockState;

public class TileEnderCtrl extends TileBlockEntityCyclic {

  private static final String NBT_SHELVES = "shelves";
  EnderControllerItemHandler controllerInv = new EnderControllerItemHandler(this);
  private List<BlockPos> connectedShelves = new ArrayList<>();
  RenderTextType renderStyle = RenderTextType.TEXT;

  public TileEnderCtrl(BlockPos pos, BlockState state) {
    super(TileRegistry.ENDER_CONTROLLER.get(), pos, state);
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return controllerInv;
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  /**
   * Converts to sorted list
   *
   * @param shelvesIn
   */
  public void setAndSort(Set<BlockPos> shelvesIn) {
    this.connectedShelves = shelvesIn.stream().sorted(Comparator.comparing(o -> o.distSqr(this.worldPosition))).collect(Collectors.toList());
  }

  public List<BlockPos> getShelves() {
    if (connectedShelves == null) {
      connectedShelves = new ArrayList<>();
    }
    return connectedShelves;
  }

  @Override
  public void loadAdditional(ValueInput input) {
    int rt = input.getIntOr("RenderTextType", 0);
    this.renderStyle = RenderTextType.values()[rt];
    for (BlockPos pos : input.listOrEmpty(NBT_SHELVES, BlockPos.CODEC)) {
      this.connectedShelves.add(pos);
    }
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    output.putInt("RenderTextType", this.renderStyle.ordinal());
    var shelvesList = output.list(NBT_SHELVES, BlockPos.CODEC);
    for (BlockPos pos : this.connectedShelves) {
      shelvesList.add(pos);
    }
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
