package com.lothrazar.cyclicmagic.potion.effect;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketEntityDropRandom;
import com.lothrazar.cyclicmagic.potion.PotionEffectRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PotionDropItems extends PotionBase {

  private static final double DROP_CHANCE = 0.009;

  public PotionDropItems() {
    super("butter", false, 0xe5e500);
  }

  @Override
  public void tick(EntityLivingBase entity) {
    PotionEffect pot = entity.getActivePotionEffect(PotionEffectRegistry.DROPS);
    World world = entity.getEntityWorld();
    if (this.isMoving(entity) && world.rand.nextDouble() < DROP_CHANCE) {
      ModCyclic.network.sendToServer(new PacketEntityDropRandom(entity.getEntityId(), pot.getAmplifier()));
    }
  }

  /**
   * Only makes sense client side.
   * 
   * @param entity
   * @return isMoving
   */
  private boolean isMoving(EntityLivingBase entity) {
    return (Math.abs(entity.motionX) > 0) || (Math.abs(entity.motionZ) > 0);
  }
}
