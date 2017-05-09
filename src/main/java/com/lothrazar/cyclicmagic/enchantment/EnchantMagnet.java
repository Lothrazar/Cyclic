package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantMagnet extends EnchantBase {
  private static final int ITEM_HRADIUS = 5;
  private static final int HRADIUS_PER_LEVEL = 4;
  private static final int ITEM_VRADIUS = 4;
  public EnchantMagnet() {
    super("magnet",Rarity.COMMON, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });

    GuideRegistry.register(this,new ArrayList<String>( Arrays.asList(HRADIUS_PER_LEVEL+"")));
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    //Ticking
    int level = getCurrentLevelTool(entity);
    if (level > 0) {
      UtilEntity.moveEntityItemsInRegion(entity.getEntityWorld(), entity.getPosition(), ITEM_HRADIUS + HRADIUS_PER_LEVEL * level, ITEM_VRADIUS);
    }
  }
}
