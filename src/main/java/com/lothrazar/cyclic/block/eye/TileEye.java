package com.lothrazar.cyclic.block.eye;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileEye extends TileEntityBase implements ITickableTileEntity {

  public static IntValue RANGE;

  public TileEye() {
    super(TileRegistry.eye_redstone);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world.isRemote || world.getGameTime() % 10 != 0) {
      return;
    }
    //
    boolean playerFound = getLookingPlayer(RANGE.get(), false) != null;
    this.setLitProperty(playerFound);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
