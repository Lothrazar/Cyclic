package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AmethystPickaxeItem extends PickaxeItem {

  public AmethystPickaxeItem(Tier t, int db, float attackspeed, Properties prop) {
    super(t, db, attackspeed, prop);
  }

  @Override
  public boolean mineBlock(ItemStack s, Level w, BlockState state, BlockPos pos, LivingEntity entity) {
    if (w instanceof ServerLevel) {
      SoundUtil.playSoundFromServer((ServerLevel) w, pos, SoundEvents.AMETHYST_CLUSTER_BREAK);
    }
    return super.mineBlock(s, w, state, pos, entity);
  }
}
