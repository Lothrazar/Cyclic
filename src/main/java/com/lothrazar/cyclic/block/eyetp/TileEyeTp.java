package com.lothrazar.cyclic.block.eyetp;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class TileEyeTp extends TileBlockEntityCyclic {

  public static IntValue RANGE;
  public static IntValue HUNGER;
  public static IntValue EXP;
  public static IntValue FREQUENCY;

  public TileEyeTp(BlockPos pos, BlockState state) {
    super(TileRegistry.EYE_TELEPORT.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileEyeTp e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileEyeTp e) {
    e.tick();
  }

  public void tick() {
    if (level.isClientSide) {
      return;
    }
    timer--;
    if (timer > 0) {
      return;
    }
    timer = FREQUENCY.get();
    Player player = getLookingPlayer(RANGE.get(), true);
    if (this.canTp(player)) {
      boolean success = UtilEntity.enderTeleportEvent(player, level, this.worldPosition.above());
      if (success) {
        this.payCost(player);
      }
    }
  }

  private boolean canTp(Player player) {
    if (player == null) {
      return false;
    }
    if (player.isCreative()) {
      return true;
    }
    if (EXP.get() > 0 && UtilPlayer.getExpTotal(player) < EXP.get()) {
      return false;
    }
    //ignore hunger. if its zero ya dyin anyway
    return true;
  }

  private void payCost(Player player) {
    if (HUNGER.get() > 0) {
      player.getFoodData().eat(-1 * HUNGER.get(), 0);
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
