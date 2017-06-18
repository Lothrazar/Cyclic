package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilReflection;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantQuickdraw extends EnchantBase {
  public EnchantQuickdraw() {
    super("quickdraw", Rarity.VERY_RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
  }
  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof ItemBow
        || stack.getItem() == Items.BOOK;
  }
  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getEntity();
      if (getCurrentLevelTool(player) < 0) { return; }
      ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
      if (heldItem != null && heldItem.getItem() instanceof ItemBow
          && player.isHandActive()) {
        //     player.updateActiveHand();//BUT its protected bahhhh
        UtilReflection.callPrivateMethod(EntityLivingBase.class, player, "updateActiveHand", "updateActiveHand", new Object[0]);
        UtilReflection.callPrivateMethod(EntityLivingBase.class, player, "updateActiveHand", "updateActiveHand", new Object[0]);
      }
    }
  }
}
