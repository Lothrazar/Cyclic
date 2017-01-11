package com.lothrazar.cyclicmagic.potion;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionSwimSpeed extends PotionBase {
  public PotionSwimSpeed(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity != null && entity.isPotionActive(this)) {
      this.tick(entity);
    }
  }
  public void tick(EntityLivingBase entity) {
    if (entity.isInWater()) {
      UtilEntity.speedupEntityIfMoving(entity);
    }
  }
}
