package com.lothrazar.cyclicmagic.potion;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketEntityDropRandom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class PotionDropItems extends PotionBase {

  public PotionDropItems() {
    super("butter", false, 0xFFFF66);
  }

  @Override
  public void tick(EntityLivingBase entity) {
    World world = entity.getEntityWorld();

    if (this.isMoving(entity) && world.rand.nextDouble() < 0.06) {
      ModCyclic.network.sendToServer(new PacketEntityDropRandom(entity.getEntityId()));

    }
  }

  private boolean isMoving(EntityLivingBase entity) {

    // return entity.prevPosX != entity.posX || entity.prevPosZ != entity.posZ;
    return (Math.abs(entity.motionX) > 0) || (Math.abs(entity.motionZ) > 0);
  }
}
