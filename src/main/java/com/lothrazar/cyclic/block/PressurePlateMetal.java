package com.lothrazar.cyclic.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.AABB;

public class PressurePlateMetal extends PressurePlateBlock {

  boolean playersOnly = false;

  public PressurePlateMetal(Properties properties) {
    super(PressurePlateBlock.Sensitivity.EVERYTHING, properties, BlockSetType.STONE);
    playersOnly = true;
  }

  @Override
  protected int getSignalStrength(Level world, BlockPos pos) {
    if (playersOnly) {
      List<? extends Entity> list;
      //custom stuff
      AABB aabb = TOUCH_AABB.move(pos);
      list = world.getEntitiesOfClass(Player.class, aabb);
      if (!list.isEmpty()) {
        for (Entity entity : list) {
          if (!entity.isIgnoringBlockTriggers()) {
            return 15;
          }
        }
      }
      return 0;
    }
    return super.getSignalStrength(world, pos);
  }
}
