package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class GloveItem extends ItemBaseToggle {

  private static final double CLIMB_SPEED = 0.288D;

  public GloveItem(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, ServerLevel worldIn, Entity entityIn,  EquipmentSlot slot) {
    //so
    if (!this.isOn(stack)) {
      return;
    }
    if (entityIn instanceof Player) {
      Player player = (Player) entityIn;
      ItemStackUtil.damageItemRandomly(player, stack);
      if (player.horizontalCollision) {
        Level world = player.level();
        EntityUtil.tryMakeEntityClimb(world, player, CLIMB_SPEED);
        // 26.1: Item#inventoryTick now only fires server-side (Level narrowed to ServerLevel below),
        // so the client+server symmetric ticking this used to rely on for the player to immediately
        // feel the upward velocity is gone - setDeltaMovement here only changes server-side state,
        // and vanilla's hurtMarked-based motion sync skips the entity's own controlling player.
        // Push the updated motion to the owning client explicitly so the climb is actually felt.
        if (player instanceof ServerPlayer serverPlayer) {
          serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
        }
        ItemStackUtil.damageItem(player, stack);
        if (worldIn.getGameTime() % Const.TICKS_PER_SEC == 0) {
          SoundUtil.playSound(player, SoundEvents.LADDER_STEP);
        }
      }
    }
  }
}
