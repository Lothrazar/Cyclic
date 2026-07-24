package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AmethystPickaxeItem extends Item {

  public AmethystPickaxeItem(ToolMaterial t, float db, float attackspeed, Properties prop) {
    super(prop.pickaxe(t, db, attackspeed));
  }

  @Override
  public boolean mineBlock(ItemStack s, Level w, BlockState state, BlockPos pos, LivingEntity entity) {
    if (w instanceof ServerLevel) {
      SoundUtil.playSoundFromServer((ServerLevel) w, pos, SoundEvents.AMETHYST_CLUSTER_BREAK);
    }
    return super.mineBlock(s, w, state, pos, entity);
  }
}
