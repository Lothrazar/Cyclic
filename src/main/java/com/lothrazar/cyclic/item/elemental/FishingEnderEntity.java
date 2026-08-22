package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.block.fishing.TileFisher;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FishingEnderEntity extends ThrowableItemProjectile {

  public FishingEnderEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public FishingEnderEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.ENDER_FISHING.get(), livingEntityIn, worldIn, new ItemStack(ItemRegistry.ENDER_FISHING.get()));
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.ENDER_FISHING.get();
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    var level = level();
    if (type == HitResult.Type.ENTITY) {
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive() && target instanceof LivingEntity alive) {
        alive.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(PotionEffectRegistry.SWIMSPEED.get()), 60, 2, false, false, false));
        target.hurt(level.damageSources().thrown(this, this.getOwner()), 0);// zero damage for visuals and knockback 
      }
    }
    else if (type == HitResult.Type.BLOCK) {
      BlockHitResult ray = (BlockHitResult) result;
      if (ray.getBlockPos() == null) {
        return;
      }
      BlockPos pos = ray.getBlockPos();
      if (ray.getDirection() != null) {
        pos = pos.relative(ray.getDirection());
      }
      if (TileFisher.isWater(level, pos)) {
        //fish!
        if (!level.isClientSide()) {
          LootTable table;
          if (level.getRandom().nextDouble() < 0.10) { // 10% junk
            table = level.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING_JUNK);
          }
          else { // 90% fish (ignoring treasure tier)
            table = level.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING_FISH);
          }
          final int luck = 2;
          final ItemStack fishingRod = new ItemStack(Items.FISHING_ROD);
          // fishingRod.enchant disabled - needs Holder<Enchantment>
          LootParams lootContext = new LootParams.Builder((ServerLevel) level)
              .withLuck(luck)//.withRandom(level.random)
              .withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()))
              .withParameter(LootContextParams.TOOL, fishingRod)
              .create(LootContextParamSets.FISHING);
          List<ItemStack> lootDrops = table.getRandomItems(lootContext);
          if (lootDrops != null && lootDrops.size() > 0) {
            //            ItemStackUtil.damageItem(null, fishingRod);
            ItemStackUtil.drop(level, pos, lootDrops);
          }
        }
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }


}
