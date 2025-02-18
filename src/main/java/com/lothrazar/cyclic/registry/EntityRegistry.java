package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.conveyor.ConveyorItemEntity;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityCarry;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityDamage;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityStun;
import com.lothrazar.cyclic.item.elemental.DarkFireEntity;
import com.lothrazar.cyclic.item.elemental.FireEntity;
import com.lothrazar.cyclic.item.elemental.FishingEnderEntity;
import com.lothrazar.cyclic.item.elemental.LightningEntity;
import com.lothrazar.cyclic.item.elemental.SnowEntity;
import com.lothrazar.cyclic.item.ender.EntityDungeonEye;
import com.lothrazar.cyclic.item.ender.EyeOfEnderEntityNodrop;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.missile.MagicMissileEntity;
import com.lothrazar.cyclic.item.slingshot.StoneEntity;
import com.lothrazar.cyclic.item.torchthrow.EntityTorchBolt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {

  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ModCyclic.MODID);
  public static final DeferredHolder<EntityType<?>, EntityType<EntityMagicNetEmpty>> MAGIC_NET = ENTITIES.register("magic_net", () -> EntityType.Builder.<EntityMagicNetEmpty> of(EntityMagicNetEmpty::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("magic_net"));
  public static final DeferredHolder<EntityType<?>, EntityType<EntityTorchBolt>> TORCH_BOLT = ENTITIES.register("torch_bolt", () -> EntityType.Builder.<EntityTorchBolt> of(EntityTorchBolt::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("torch_bolt"));
  public static final DeferredHolder<EntityType<?>, EntityType<BoomerangEntityStun>> BOOMERANG_STUN = ENTITIES.register("boomerang_stun", () -> EntityType.Builder.<BoomerangEntityStun> of(BoomerangEntityStun::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("boomerang_stun"));
  public static final DeferredHolder<EntityType<?>, EntityType<BoomerangEntityCarry>> BOOMERANG_CARRY = ENTITIES.register("boomerang_carry", () -> EntityType.Builder.<BoomerangEntityCarry> of(BoomerangEntityCarry::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("boomerang_carry"));
  public static final DeferredHolder<EntityType<?>, EntityType<BoomerangEntityDamage>> BOOMERANG_DAMAGE = ENTITIES.register("boomerang_damage", () -> EntityType.Builder.<BoomerangEntityDamage> of(BoomerangEntityDamage::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("boomerang_damage"));
  public static final DeferredHolder<EntityType<?>, EntityType<EntityDungeonEye>> DUNGEON = ENTITIES.register("dungeon", () -> EntityType.Builder.<EntityDungeonEye> of(EntityDungeonEye::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("dungeon"));
  public static final DeferredHolder<EntityType<?>, EntityType<EyeOfEnderEntityNodrop>> EYE = ENTITIES.register("eye", () -> EntityType.Builder.<EyeOfEnderEntityNodrop> of(EyeOfEnderEntityNodrop::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("eye"));
  public static final DeferredHolder<EntityType<?>, EntityType<LightningEntity>> LIGHTNING_BOLT = ENTITIES.register("lightning_bolt", () -> EntityType.Builder.<LightningEntity> of(LightningEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("lightning_bolt"));
  public static final DeferredHolder<EntityType<?>, EntityType<SnowEntity>> SNOW_BOLT = ENTITIES.register("snow_bolt", () -> EntityType.Builder.<SnowEntity> of(SnowEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("snow_bolt"));
  public static final DeferredHolder<EntityType<?>, EntityType<FireEntity>> FIRE_BOLT = ENTITIES.register("fire_bolt", () -> EntityType.Builder.<FireEntity> of(FireEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("fire_bolt"));
  public static final DeferredHolder<EntityType<?>, EntityType<StoneEntity>> STONE_BOLT = ENTITIES.register("stone_bolt", () -> EntityType.Builder.<StoneEntity> of(StoneEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("stone_bolt"));
  public static final DeferredHolder<EntityType<?>, EntityType<MagicMissileEntity>> MAGIC_MISSILE = ENTITIES.register("magic_missile", () -> EntityType.Builder.<MagicMissileEntity> of(MagicMissileEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("magic_missile"));
  public static final DeferredHolder<EntityType<?>, EntityType<ConveyorItemEntity>> CONVEYOR_ITEM = ENTITIES.register("conveyor_item", () -> EntityType.Builder.<ConveyorItemEntity> of(ConveyorItemEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("conveyor_item"));
  public static final DeferredHolder<EntityType<?>, EntityType<DarkFireEntity>> DARKFIRE_BOLT = ENTITIES.register("darkfire_bolt", () -> EntityType.Builder.<DarkFireEntity> of(DarkFireEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("darkfire_bolt"));
  public static final DeferredHolder<EntityType<?>, EntityType<FishingEnderEntity>> ENDER_FISHING = ENTITIES.register("ender_fishing", () -> EntityType.Builder.<FishingEnderEntity> of(FishingEnderEntity::new, MobCategory.MISC)
      .setShouldReceiveVelocityUpdates(true)
      .setUpdateInterval(1)
      .setTrackingRange(128)
      .sized(.6F, .6F)
      .build("ender_fishing"));
}
