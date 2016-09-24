package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorStandSwapModule extends BaseEventModule implements IHasConfig {
  //added for https://www.twitch.tv/darkphan
  private boolean enabled;
  private final static EntityEquipmentSlot[] armorStandEquipment = {
      EntityEquipmentSlot.OFFHAND, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
  @SubscribeEvent
  public void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
    if (!enabled) { return; }
    if (event.getWorld().isRemote) { return; } //server side only
    if (event.getTarget() == null || event.getTarget() instanceof EntityArmorStand == false) { return; }
    EntityArmorStand entityStand = (EntityArmorStand) event.getTarget();
    EntityPlayer player = event.getEntityPlayer();
    if (player.isSneaking() == false) { return; } //bc when not sneaking, we do the normal single item version
    event.setCanceled(true);//which means we need to now cancel that normal version and do our own
    for (EntityEquipmentSlot slot : armorStandEquipment) {
      ItemStack itemPlayer = player.getItemStackFromSlot(slot);
      ItemStack itemArmorstand = entityStand.getItemStackFromSlot(slot);
      player.setItemStackToSlot(slot, itemArmorstand);
      entityStand.setItemStackToSlot(slot, itemPlayer);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    enabled = config.getBoolean("ArmorStandSwap", category, true,
        "Swap armor with a stand whenever you interact while sneaking");
  }
}
