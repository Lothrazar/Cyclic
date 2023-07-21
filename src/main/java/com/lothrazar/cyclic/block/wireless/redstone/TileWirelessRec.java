package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.state.properties.BlockStateProperties;

public class TileWirelessRec extends TileEntityBase {

  //not saved, desynced. used on server and updated by the transmitters
  HashMap<UUID, Boolean> map = new HashMap<>();

  public TileWirelessRec() {
    super(TileRegistry.wireless_receiver);
  }

  @Override
  public void setField(int field, int value) {}

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
    world.setBlockState(pos, this.getBlockState().with(BlockStateProperties.POWERED, b), 3);
  }
}
