package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.module.PotionModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventPotionModule implements IHasConfig {
  public boolean cancelPotionInventoryShift;
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    if (entity.isPotionActive(PotionModule.slowfall)) {
      PotionModule.slowfall.tick(entity);
    }
    if (entity.isPotionActive(PotionModule.magnet)) {
      PotionModule.magnet.tick(entity);
    }
    if (entity.isPotionActive(PotionModule.waterwalk)) {
      PotionModule.waterwalk.tick(entity);
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
    event.setCanceled(cancelPotionInventoryShift);
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.inventory;
    cancelPotionInventoryShift = config.getBoolean("Potion Inventory Shift", category, true,
        "When true, this blocks the potions moving the inventory over");
  }
}
