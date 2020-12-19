package com.lothrazar.cyclic.block.eyetp;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileEyeTp extends TileEntityBase implements ITickableTileEntity {

  public static IntValue RANGE;

  public TileEyeTp() {
    super(TileRegistry.eye_teleport);
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
      return;// the %10 means only check every half second
    }
    PlayerEntity player = getLookingPlayer(RANGE.get(), true);
    if (player != null) {
      UtilEntity.enderTeleportEvent(player, world, this.pos.up());
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
