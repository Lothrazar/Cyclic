package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AirAntiGravity extends ItemBaseToggle {

  private static final int TICKS_FALLDIST_SYNC = 22; //tick every so often
  private static final double DOWNWARD_SPEED_SNEAKING = -0.32;

  public AirAntiGravity(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (!(entity instanceof PlayerEntity)) {
      return;
    }
    PlayerEntity player = (PlayerEntity) entity;
    BlockPos belowMe = player.getPosition().down();
    //sneak on air, or a nonsolid block like a flower
    boolean isAirBorne = (world.isAirBlock(belowMe) || !world.isTopSolid(belowMe, player));
    //
    if (isAirBorne && player.getMotion().y < 0) { //player.isSneaking() &&
      double y = (player.isCrouching()) ? DOWNWARD_SPEED_SNEAKING : 0;
      player.setMotion(player.getMotion().x, y, player.getMotion().z);
      player.isAirBorne = false;
      //if we set onGround->true all the time, it blocks fwd movement anywya
      player.setOnGround(true);
      // (player.motionX == 0 && player.motionZ == 0); //allow jump only if not walking
      if (player.getEntityWorld().rand.nextDouble() < 0.1) {
        //        super.damageCharm(player, stack);
        UtilItemStack.damageItem(player, stack);
      }
      if (world.isRemote && player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
        PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
      }
    }
  }
}
