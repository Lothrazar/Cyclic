package com.lothrazar.cyclicmagic.module;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.item.projectile.ItemProjectileTNT;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EnderBombModule extends BaseModule {
  private boolean enderBombsEnabled;  
  @Override
  public void onInit() {
    ItemRegistry.ender_tnt_1 = new ItemProjectileTNT(1);
    ItemRegistry.ender_tnt_2 = new ItemProjectileTNT(2);
    ItemRegistry.ender_tnt_3 = new ItemProjectileTNT(3);
    ItemRegistry.ender_tnt_4 = new ItemProjectileTNT(4);
    ItemRegistry.ender_tnt_5 = new ItemProjectileTNT(5);
    ItemRegistry.ender_tnt_6 = new ItemProjectileTNT(6);
    ItemRegistry.addItem(ItemRegistry.ender_tnt_1, "ender_tnt_1");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_2, "ender_tnt_2");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_3, "ender_tnt_3");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_4, "ender_tnt_4");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_5, "ender_tnt_5");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_6, "ender_tnt_6");
    //TODO  merge with projectile module??

    int trackingRange = 64;
    int updateFrequency = 1;
    boolean sendsVelocityUpdates = true;
    EntityRegistry.registerModEntity(EntityDynamite.class, "tntbolt", 1007, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    
  }
  @Override
  public void syncConfig(Configuration config) {
    enderBombsEnabled = config.getBoolean("EnderBombs", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return enderBombsEnabled;
  }
}
