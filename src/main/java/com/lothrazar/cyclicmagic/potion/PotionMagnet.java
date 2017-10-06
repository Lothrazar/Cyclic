package com.lothrazar.cyclicmagic.potion;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.EntityLivingBase;
 
public class PotionMagnet extends PotionBase {
  private final static int ITEM_HRADIUS = 20;
  private final static int ITEM_VRADIUS = 4;
  public PotionMagnet(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @Override
  public void tick(EntityLivingBase entityLiving) {
    UtilEntity.moveEntityItemsInRegion(entityLiving.getEntityWorld(), entityLiving.getPosition(), ITEM_HRADIUS, ITEM_VRADIUS);
  }
}
