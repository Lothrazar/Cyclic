package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityProjectileRegistry {
  static int trackingRange = 64;
  static int updateFrequency = 1;
  static boolean sendsVelocityUpdates = true;
  public static void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id) {
    EntityRegistry.registerModEntity(entityClass, entityName, id, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
  }
}
