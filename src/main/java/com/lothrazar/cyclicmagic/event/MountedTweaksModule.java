package com.lothrazar.cyclicmagic.event;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.module.BaseEventModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MountedTweaksModule extends BaseEventModule implements IHasConfig {
  private boolean showHungerMounted;
  private boolean disableHurtMount;
  private boolean mountedPearl;
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
      if (p != null && sourceOfDamage instanceof EntityPlayer
          && (p.getUniqueID() == sourceOfDamage.getUniqueID() || p == sourceOfDamage)) {
        //with arrows/sword/etc
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
    } //else config is false, so leave it alone
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.player;
    showHungerMounted = config.getBoolean("Show Hunger Mounted", category, true, "Force the players hunger bar to show even when mounted");
    //TODO:disableHurtMount
    mountedPearl = config.getBoolean("Pearls On Horseback", category, true,
        "Enderpearls work on a horse, bringing it with you");
  }
  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    if (mountedPearl) { 
      Entity ent = event.getEntity();
      if (ent instanceof EntityLivingBase == false) { return; }
      EntityLivingBase living = (EntityLivingBase) event.getEntity();
      if (living.getRidingEntity() != null && living instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) living;
        Entity horse = player.getRidingEntity();
        horse.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        player.startRiding(horse, true);
      }
    }
  }
}
