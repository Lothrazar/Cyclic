package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.entity.projectile.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ProjectileRegistry {
  public static void register(FMLInitializationEvent event) {
    int entityID = 999;
    int trackingRange = 64;
    int updateFrequency = 1;
    boolean sendsVelocityUpdates = true;
    // range, freq, velocity
    EntityRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityWaterBolt.class, "waterbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityFishingBolt.class, "fishingbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityHomeBolt.class, "bedbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityDynamite.class, "tntbolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    EntityRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", entityID++, ModMain.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
  }
}
