package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.module.BaseModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMountedPearl extends BaseModule  implements IHasConfig {
  private boolean mountedPearl;
  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    if (!mountedPearl) { return; }
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
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.player;
    mountedPearl = config.getBoolean("Pearls On Horseback", category, true,
        "Enderpearls work on a horse, bringing it with you");
  }
}
