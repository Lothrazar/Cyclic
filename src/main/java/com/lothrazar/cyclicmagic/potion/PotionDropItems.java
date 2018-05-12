package com.lothrazar.cyclicmagic.potion;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketEntityDropRandom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionDropItems extends PotionBase {

  public PotionDropItems() {
    super("butter", false, 0xFFFF66);
  }

  @Override
  public void tick(EntityLivingBase entity) {
    World world = entity.getEntityWorld();
    BlockPos here = entity.getPosition();
    if (entity instanceof EntityPlayer && world.isRemote) {


      ModCyclic.logger.log("butter on player ! clientside!"
          + "?entity.isMoving(entity)  " + isMoving(entity));
    }
    if (this.isMoving(entity) && world.rand.nextDouble() < 0.1) {
      ModCyclic.network.sendToServer(new PacketEntityDropRandom(entity.getEntityId()));
      //NET PACKET

    }
  }

  private boolean isMoving(EntityLivingBase entity) {

    // return entity.prevPosX != entity.posX || entity.prevPosZ != entity.posZ;
    return (Math.abs(entity.motionX) > 0) || (Math.abs(entity.motionZ) > 0);
  }
}
