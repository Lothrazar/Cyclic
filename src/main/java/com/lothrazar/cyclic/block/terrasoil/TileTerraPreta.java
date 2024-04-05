package com.lothrazar.cyclic.block.terrasoil;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.GrowthUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileTerraPreta extends TileEntityBase implements ITickableTileEntity {

  public static final double ODDS_DEFAULT = 0.5;
  //see:   [cyclic.blocks.terra_preta]
  public static IntValue TIMER_FULL;
  public static IntValue HEIGHT;

  public TileTerraPreta() {
    super(TileRegistry.terra_preta);
  }

  @Override
  public void tick() {
    //sprinkler to ONLY whats directly above/below
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL.get();
    for (int h = 0; h < HEIGHT.get(); h++) {
      BlockPos current = this.getPos().up(h);
      GrowthUtil.tryGrow(world, current, ODDS_DEFAULT);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
