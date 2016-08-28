package com.lothrazar.cyclicmagic.potion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEnder extends PotionBase {
  public PotionEnder(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    Entity ent = event.getEntity();
    if (ent instanceof EntityLivingBase == false) { return; }
    EntityLivingBase living = (EntityLivingBase) event.getEntity();
    if (living.isPotionActive(this)) {
      event.setAttackDamage(0);
    }
  }
  @Override
  public void tick(EntityLivingBase entity) {

    
  }
}
