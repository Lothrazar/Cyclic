package com.lothrazar.cyclicmagic.potion;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.EntityLivingBase;

public class PotionSwimSpeed extends PotionBase {
  public PotionSwimSpeed(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @Override
  public void tick(EntityLivingBase entity) {
    if (entity.isInWater()) {
      UtilEntity.speedupEntityIfMoving(entity);
    }
  }
}
