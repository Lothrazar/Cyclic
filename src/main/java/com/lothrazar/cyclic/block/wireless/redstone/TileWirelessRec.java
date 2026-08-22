package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.UUID;

public class TileWirelessRec extends TileBlockEntityCyclic {

  HashMap<UUID, Boolean> map = new HashMap<>();

  public TileWirelessRec(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_RECEIVER.get(), pos, state);
    this.needsRedstone = 0;
  }

  @Override
  public void setField(int field, int value) {
  }

  @Override
  public int getField(int field) {
    return 0;
  }

  public void putPowerSender(UUID id) {
    if (id == null) {
      return;
    }
    map.put(id, true);
    this.toggleState();
  }

  public void removePowerSender(UUID id) {
    if (id == null) {
      return;
    }
    if (map.containsKey(id)) {
      map.remove(id);
    }
    this.toggleState();
  }

  private void toggleState() {
    boolean b = map.keySet().size() > 0;
    level.setBlock(worldPosition, this.getBlockState().setValue(BlockStateProperties.POWERED, b), 3);
  }
}
