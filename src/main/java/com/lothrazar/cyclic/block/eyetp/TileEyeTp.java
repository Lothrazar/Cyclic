package com.lothrazar.cyclic.block.eyetp;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileEyeTp extends TileEntityBase implements ITickableTileEntity {

  public static IntValue RANGE;
  public static IntValue HUNGER;
  public static IntValue EXP;
  public static IntValue FREQUENCY;

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
    if (world == null || world.isRemote) {
      return;
    }
    if (timer-- > 0) {
      return;
    }
    timer = FREQUENCY.get();
    PlayerEntity player = getLookingPlayer(RANGE.get(), true);
    if (this.canTp(player)) {
      boolean success = UtilEntity.enderTeleportEvent(player, world, this.pos.up());
      if (success) {
        this.payCost(player);
      }
    }
  }

  private boolean canTp(PlayerEntity player) {
    if (player == null) {
      return false;
    }
    if (player.isCreative()) {
      return true;
    }
    return EXP.get() <= 0 || !(UtilPlayer.getExpTotal(player) < EXP.get());
    //ignore hunger. if its zero ya dyin anyway
  }

  private void payCost(PlayerEntity player) {
    if (HUNGER.get() > 0) {
      player.getFoodStats().addStats(-1 * HUNGER.get(), 0);
    }
    if (EXP.get() > 0) {
      player.giveExperiencePoints(-1 * EXP.get());
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
