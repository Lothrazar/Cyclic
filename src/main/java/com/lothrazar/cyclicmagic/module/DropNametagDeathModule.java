package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropNametagDeathModule extends BaseEventModule implements IHasConfig {
  private boolean nameTagDeath;
  @Override
  public void onInit() {
    ModMain.instance.events.addEvent(this);//for SubcribeEvent hooks
  }
  @SubscribeEvent
  public void onLivingDropsEvent(LivingDropsEvent event) {
    if (nameTagDeath) {
      Entity entity = event.getEntity();
      World worldObj = entity.getEntityWorld();
      if (entity.getCustomNameTag() != null && entity.getCustomNameTag() != "") {
        // item stack NBT needs the name enchanted onto it
        if (entity.worldObj.isRemote == false) {
          ItemStack nameTag = UtilNBT.buildEnchantedNametag(entity.getCustomNameTag());
          UtilEntity.dropItemStackInWorld(worldObj, entity.getPosition(), nameTag);
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.mobs;
    nameTagDeath = config.getBoolean("Name Tag Death", category, true,
        "When an entity dies that is named with a tag, it drops the nametag");
  }
}
