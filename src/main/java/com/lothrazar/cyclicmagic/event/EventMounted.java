package com.lothrazar.cyclicmagic.event;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventMounted implements IHasConfig {
  private boolean disableHurtMount = false;
  private boolean showHungerMounted = true;
  @SubscribeEvent
  public void onLivingHurtEvent(LivingHurtEvent event) {
    if (disableHurtMount == false) { return;//this is always off. it seems like in vanilla minecraft this just never happens
    // at least in 1.9.4, i cannot hurt the horse im riding with a sword or bow shot
    //so no point in having feature.
    }
    DamageSource source = event.getSource();
    if (source.getSourceOfDamage() == null) { return; }
    Entity sourceOfDamage = source.getEntity();
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    List<Entity> getPassengers = entity.getPassengers();
    for (Entity p : getPassengers) {
      //			System.out.println("pasanger hurt its rider?");
      if (p != null && sourceOfDamage instanceof EntityPlayer
          && (p.getUniqueID() == sourceOfDamage.getUniqueID()
              || p == sourceOfDamage)) {
        //with arrows/sword/etc
        //				System.out.println("Cannot hurt your own horse");
        event.setCanceled(true);
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderOverlay(RenderGameOverlayEvent event) {
    // https://github.com/LothrazarMinecraftMods/OverpoweredInventory/blob/8a7459161837b930c5417f774676504bce970e66/src/main/java/com/lothrazar/powerinventory/EventHandler.java
    if (showHungerMounted) {
      GuiIngameForge.renderFood = true;
    }
    //else config is false, so leave it alone
    //doesnt really work tho...
    //TODO: if space bar is down, then hide jump bar and show this
    //			GuiIngameForge.renderExperiance = true;
  }
  @Override
  public void syncConfig(Configuration config) {
    showHungerMounted = config.getBoolean("Show Hunger Mounted", Const.ConfigCategory.player, true, "Force the players hunger bar to show even when mounted");
  }
}
