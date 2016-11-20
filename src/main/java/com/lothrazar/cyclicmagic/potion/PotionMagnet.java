package com.lothrazar.cyclicmagic.potion;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionMagnet extends PotionBase {
  private final static int ITEM_HRADIUS = 20;
  private final static int ITEM_VRADIUS = 4;
  public PotionMagnet(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity != null && entity.isPotionActive(this)) {
      this.tick(entity);
    }
  }
  public void tick(EntityLivingBase entityLiving) {
    UtilEntity.moveEntityItemsInRegion(entityLiving.getEntityWorld(), entityLiving.getPosition(), ITEM_HRADIUS, ITEM_VRADIUS);
  }
}
