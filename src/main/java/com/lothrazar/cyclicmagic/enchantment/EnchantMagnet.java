package com.lothrazar.cyclicmagic.enchantment;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantMagnet extends Enchantment {
  private static final int ITEM_HRADIUS = 5;
  private static final int HRADIUS_PER_LEVEL = 4;
  private static final int ITEM_VRADIUS = 4;
  public EnchantMagnet() {
    super(Rarity.COMMON, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    this.setName("magnet");
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    //Ticking
    ItemStack main = entity.getHeldItemMainhand();
    ItemStack off = entity.getHeldItemOffhand();
    int mainLevel = -1, offLevel = -1;
    if (main != null && EnchantmentHelper.getEnchantments(main).containsKey(this)) {
      mainLevel = EnchantmentHelper.getEnchantments(main).get(this);
    }
    if (off != null && EnchantmentHelper.getEnchantments(off).containsKey(this)) {
      offLevel = EnchantmentHelper.getEnchantments(off).get(this);
    }
    int level = Math.max(mainLevel, offLevel);
    if (level > 0) {
      UtilEntity.moveEntityItemsInRegion(entity.getEntityWorld(), entity.getPosition(), ITEM_HRADIUS + HRADIUS_PER_LEVEL * level, ITEM_VRADIUS);
    }
  }
}
