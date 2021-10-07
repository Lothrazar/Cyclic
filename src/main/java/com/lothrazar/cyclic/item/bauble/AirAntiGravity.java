package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class AirAntiGravity extends ItemBaseToggle {

  private static final int TICKS_FALLDIST_SYNC = 22; //tick every so often
  private static final double DOWNWARD_SPEED_SNEAKING = -0.32;

  public AirAntiGravity(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (entity instanceof Player == false) {
      return;
    }
    Player player = (Player) entity;
    BlockPos belowMe = player.blockPosition().below();
    //sneak on air, or a nonsolid block like a flower
    boolean isAirBorne = (world.isEmptyBlock(belowMe) || world.loadedAndEntityCanStandOn(belowMe, player) == false);
    //
    if (isAirBorne && player.getDeltaMovement().y < 0) { //player.isSneaking() &&
      double y = (player.isCrouching()) ? DOWNWARD_SPEED_SNEAKING : 0;
      player.setDeltaMovement(player.getDeltaMovement().x, y, player.getDeltaMovement().z);
      player.hasImpulse = false;
      //if we set onGround->true all the time, it blocks fwd movement anywya
      player.setOnGround(true);
      // (player.motionX == 0 && player.motionZ == 0); //allow jump only if not walking
      if (player.getCommandSenderWorld().random.nextDouble() < 0.1) {
        //        super.damageCharm(player, stack);
        UtilItemStack.damageItem(player, stack);
      }
      if (world.isClientSide && player.tickCount % TICKS_FALLDIST_SYNC == 0) {
        PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
      }
    }
  }
}
