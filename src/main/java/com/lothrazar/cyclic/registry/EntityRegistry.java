package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.conveyor.ConveyorItemEntity;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntity;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityCarry;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityDamage;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityStun;
import com.lothrazar.cyclic.item.elemental.FireEntity;
import com.lothrazar.cyclic.item.elemental.LightningEntity;
import com.lothrazar.cyclic.item.elemental.SnowEntity;
import com.lothrazar.cyclic.item.endereye.EyeOfEnderEntityNodrop;
import com.lothrazar.cyclic.item.findspawner.EntityDungeonEye;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.slingshot.LaserEntity;
import com.lothrazar.cyclic.item.slingshot.MagicMissileEntity;
import com.lothrazar.cyclic.item.slingshot.StoneEntity;
import com.lothrazar.cyclic.item.torchthrow.EntityTorchBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {

  @ObjectHolder(ModCyclic.MODID + ":dungeon")
  public static EntityType<EntityDungeonEye> DUNGEON;
  @ObjectHolder(ModCyclic.MODID + ":magic_net")
  public static EntityType<EntityMagicNetEmpty> NETBALL;
  @ObjectHolder(ModCyclic.MODID + ":torch_bolt")
  public static EntityType<EntityTorchBolt> TORCHBOLT;
  @ObjectHolder(ModCyclic.MODID + ":lightning_bolt")
  public static EntityType<LightningEntity> LIGHTNINGBOLT;
  @ObjectHolder(ModCyclic.MODID + ":snow_bolt")
  public static EntityType<SnowEntity> SNOWBOLT;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_stun")
  public static EntityType<BoomerangEntity> BOOMERANG_STUN;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_carry")
  public static EntityType<BoomerangEntity> BOOMERANG_CARRY;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_damage")
  public static EntityType<BoomerangEntity> BOOMERANG_DAMAGE;
  @ObjectHolder(ModCyclic.MODID + ":eye")
  public static EntityType<EyeOfEnderEntityNodrop> EYE;
  @ObjectHolder(ModCyclic.MODID + ":fire_bolt")
  public static EntityType<FireEntity> FIRE_BOLT;
  @ObjectHolder(ModCyclic.MODID + ":stone_bolt")
  public static EntityType<StoneEntity> STONE_BOLT;
  @ObjectHolder(ModCyclic.MODID + ":conveyor_item")
  public static EntityType<ConveyorItemEntity> CONVEYOR_ITEM;
  @ObjectHolder(ModCyclic.MODID + ":laser_bolt")
  public static EntityType<LaserEntity> LASER_BOLT;
  @ObjectHolder(ModCyclic.MODID + ":magic_missile")
  public static EntityType<MagicMissileEntity> MAGIC_MISSILE;

  @SubscribeEvent
  public static void registerEntity(RegistryEvent.Register<EntityType<?>> e) {
    IForgeRegistry<EntityType<?>> r = e.getRegistry();
    r.register(
        EntityType.Builder.<EntityMagicNetEmpty> of(EntityMagicNetEmpty::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("magic_net")
            .setRegistryName("magic_net"));
    r.register(
        EntityType.Builder.<EntityTorchBolt> of(EntityTorchBolt::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("torch_bolt")
            .setRegistryName("torch_bolt"));
    r.register(
        EntityType.Builder.<BoomerangEntityStun> of(BoomerangEntityStun::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("boomerang_stun")
            .setRegistryName("boomerang_stun"));
    r.register(
        EntityType.Builder.<BoomerangEntityCarry> of(BoomerangEntityCarry::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("boomerang_carry")
            .setRegistryName("boomerang_carry"));
    r.register(
        EntityType.Builder.<BoomerangEntityDamage> of(BoomerangEntityDamage::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("boomerang_damage")
            .setRegistryName("boomerang_damage"));
    r.register(
        EntityType.Builder.<EntityDungeonEye> of(EntityDungeonEye::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("dungeon")
            .setRegistryName("dungeon"));
    r.register(
        EntityType.Builder.<EyeOfEnderEntityNodrop> of(EyeOfEnderEntityNodrop::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("eye")
            .setRegistryName("eye"));
    r.register(
        EntityType.Builder.<LightningEntity> of(LightningEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("lightning_bolt")
            .setRegistryName("lightning_bolt"));
    r.register(
        EntityType.Builder.<SnowEntity> of(SnowEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("snow_bolt")
            .setRegistryName("snow_bolt"));
    r.register(
        EntityType.Builder.<FireEntity> of(FireEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("fire_bolt")
            .setRegistryName("fire_bolt"));
    r.register(
        EntityType.Builder.<StoneEntity> of(StoneEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("stone_bolt")
            .setRegistryName("stone_bolt"));
    r.register(
        EntityType.Builder.<LaserEntity> of(LaserEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("laser_bolt")
            .setRegistryName("laser_bolt"));
    r.register(
        EntityType.Builder.<MagicMissileEntity> of(MagicMissileEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .sized(.6f, .6f)
            .build("magic_missile")
            .setRegistryName("magic_missile"));
    r.register(
        EntityType.Builder.<ConveyorItemEntity> of(ConveyorItemEntity::new, MobCategory.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(1)
            .sized(0.45F, 0.45F)
            .build("conveyor_item")
            .setRegistryName("conveyor_item"));
  }
}
