package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.base.ItemEntityInteractable;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.item.AntimatterEvaporatorWandItem;
import com.lothrazar.cyclic.item.bauble.CharmBase;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.carrot.ItemHorseEnder;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.heart.HeartItem;
import com.lothrazar.cyclic.item.storagebag.StorageBagItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {

  @SubscribeEvent
  public void onCriticalHitEvent(CriticalHitEvent event) {
    //LivingJumpEvent  
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity ply = (PlayerEntity) event.getEntityLiving();
      //      ply.getAttribute(Attributes.)
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_CRIT.get());
      if (!find.isEmpty()) {
        // This is by default 1.5F for ciritcal hits and 1F for normal hits . 
        event.setDamageModifier(3F);
      }
    }
  }

  @SubscribeEvent
  public void onLivingKnockBackEvent(LivingKnockBackEvent event) {
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity ply = (PlayerEntity) event.getEntityLiving();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_KNOCKBACK_RESIST.get());
      if (!find.isEmpty()) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public void onProjectileImpactEvent(ProjectileImpactEvent.Arrow event) {
    if (event.getArrow() == null || event.getRayTraceResult() == null) {
      return;
    }
    World world = event.getArrow().world;
    Type hit = event.getRayTraceResult().getType();
    Entity shooter = event.getArrow().func_234616_v_(); // getShooter
    if (shooter instanceof PlayerEntity) {
      PlayerEntity ply = (PlayerEntity) shooter;
      //      ply.isSprinting()
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.QUIVER_DMG.get());
      if (!find.isEmpty()) {
        //        ModCyclic.LOGGER.info("before " + event.getArrow().getDamage());
        AbstractArrowEntity arrow = event.getArrow();
        double boost = arrow.getDamage() / 2;
        arrow.setDamage(arrow.getDamage() + boost);
        UtilItemStack.damageItem(ply, find);
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.QUIVER_LIT.get());
      if (!find.isEmpty() && world.rand.nextDouble() < 0.25) {
        if (hit == RayTraceResult.Type.ENTITY && ((EntityRayTraceResult) event.getRayTraceResult()).getEntity() instanceof LivingEntity) {
          LivingEntity target = (LivingEntity) ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();
          target.setGlowing(true);
          //          ModCyclic.LOGGER.info(event.getEntity() + " eeeee" + event.getArrow().getDamage());
          BlockPos p = target.getPosition();
          // lightning? 
          LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
          lightningboltentity.moveForced(p.getX(), p.getY(), p.getZ());
          world.addEntity(lightningboltentity);
          UtilItemStack.damageItem(ply, find);
        }
      }
    }
  }

  @SubscribeEvent
  public void onPotionAddedEvent(PotionAddedEvent event) {
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity ply = (PlayerEntity) event.getEntityLiving();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_ANTIPOTION.get());
      if (!find.isEmpty()) {
        event.getPotionEffect().duration = 0;
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_STEALTHPOTION.get());
      if (!find.isEmpty()) {
        if (event.getOldPotionEffect() != null) {
          event.getOldPotionEffect().showParticles = false;
        }
        event.getPotionEffect().showParticles = false;
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_BOOSTPOTION.get());
      if (!find.isEmpty()) {
        int boost = event.getPotionEffect().duration / 2;
        event.getPotionEffect().duration += boost;
      }
    }
  }

  @SubscribeEvent
  public void onEntityDamage(LivingDamageEvent event) {
    DamageSource src = event.getSource();
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      if (src.isExplosion()) {
        //explosion thingy
        this.damageFinder(event, player, ItemRegistry.CHARM_CREEPER.get(), 0);
      }
      //check all cases
      //SB switch 
      if (src == DamageSource.FALL || src == DamageSource.CACTUS || src == DamageSource.SWEET_BERRY_BUSH) {
        this.damageFinder(event, player, ItemRegistry.CHARM_LONGFALL.get(), 0);
      }
      else if (src == DamageSource.FLY_INTO_WALL || src == DamageSource.IN_WALL) {
        //stone lung
        this.damageFinder(event, player, ItemRegistry.CHARM_STONE.get(), 0);
      }
      else if (src == DamageSource.MAGIC || src == DamageSource.DRAGON_BREATH) {
        this.damageFinder(event, player, ItemRegistry.CHARM_MAGICDEF.get(), 0.5F);
      }
      else if (src == DamageSource.STARVE) {
        if (this.damageFinder(event, player, ItemRegistry.CHARM_STARVATION.get(), 0)) {
          player.getFoodStats().addStats(0, 0.2F);
        }
      }
      else if (src == DamageSource.DROWN) {
        if (this.damageFinder(event, player, ItemRegistry.CHARM_WATER.get(), 0)) {
          //and a holdover bonus
          EffectInstance eff = new EffectInstance(Effects.WATER_BREATHING, 20 * 10, 1);
          eff.showParticles = false;
          eff.showIcon = false;
          player.addPotionEffect(eff);
        }
      }
      else if (src == DamageSource.LAVA || src == DamageSource.IN_FIRE || src == DamageSource.ON_FIRE) {
        this.damageFinder(event, player, ItemRegistry.charm_fire, 0);
      }
    }
    else if (src.getTrueSource() instanceof PlayerEntity) {
      //player DEALING damage
      PlayerEntity ply = (PlayerEntity) src.getTrueSource();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_VENOM.get());
      if (!find.isEmpty() && ply.world.rand.nextDouble() < 0.25F) {
        int seconds = 2 + ply.world.rand.nextInt(4);
        event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.POISON, 20 * seconds, 0));
      }
      if (ply.getActiveHand() != null && ply.getHeldItem(ply.getActiveHand()).isEmpty()) {
        //            ModCyclic.LOGGER.info("EMPTY hand damage");
      }
    }
  }

  private boolean damageFinder(LivingDamageEvent event, PlayerEntity player, Item item, float factor) {
    ItemStack find = CharmUtil.getIfEnabled(player, item);
    if (!find.isEmpty()) {
      float amt = event.getAmount() * factor;
      event.setAmount(amt);
      if (amt <= 0) {
        event.setCanceled(true);
      }
      UtilItemStack.damageItem(player, find);
      return true;
    }
    return false;
  }

  @SubscribeEvent
  public void onPlayerCloneDeath(PlayerEvent.Clone event) {
    ModifiableAttributeInstance original = event.getOriginal().getAttribute(Attributes.MAX_HEALTH);
    if (original != null) {
      AttributeModifier healthModifier = original.getModifier(HeartItem.ID);
      if (healthModifier != null) {
        event.getPlayer().getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(healthModifier);
      }
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity liv = event.getEntityLiving();
    tryItemHorseEnder(liv);
    if (liv instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) liv;
      CharmBase.charmSpeed(player);
      CharmBase.charmLuck(player);
      CharmBase.charmAttackSpeed(player);
      CharmBase.charmExpSpeed(player);
    }
  }

  @SubscribeEvent
  public void onXpPickup(PlayerXpEvent.PickupXp event) {
    PlayerEntity player = event.getPlayer();
    ItemStack charmStack = CharmUtil.getIfEnabled(player, ItemRegistry.CHARM_XPSTOPPER.get());
    if (!charmStack.isEmpty()) {
      event.setCanceled(true);
    }
  }

  private void tryItemHorseEnder(LivingEntity liv) {
    if (liv.getPersistentData().contains(ItemHorseEnder.NBT_KEYACTIVE)
        && liv.getPersistentData().getInt(ItemHorseEnder.NBT_KEYACTIVE) > 0) {
      // 
      if (liv.isInWater()
          && liv.canBreatheUnderwater() == false
          && liv.getAir() < liv.getMaxAir()
          && !liv.isPotionActive(Effects.WATER_BREATHING)) {
        liv.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 20 * 60, 4));
        liv.addPotionEffect(new EffectInstance(PotionRegistry.PotionEffects.swimspeed, 20 * 60, 1));
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.isBurning()
          && !liv.isPotionActive(Effects.FIRE_RESISTANCE)) {
        liv.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 20 * 60, 4));
        liv.extinguish();
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.fallDistance > 12
          && !liv.isPotionActive(Effects.SLOW_FALLING)) {
        liv.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 4));
        //        if (liv.getPassengers().size() > 0) {
        //          liv.getPassengers().get(0).addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 1));
        //        }
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.getHealth() < 6
          && !liv.isPotionActive(Effects.ABSORPTION)) {
        liv.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20 * 60, 4));
        liv.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20 * 60, 4));
        ItemHorseEnder.onSuccess(liv);
      }
    }
  }

  @SubscribeEvent
  public void onBonemealEvent(BonemealEvent event) {
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    if (world.getBlockState(pos).getBlock() == Blocks.PODZOL
        && world.isAirBlock(pos.up())) {
      world.setBlockState(pos.up(), BlockRegistry.flower_cyan.getDefaultState());
      event.setResult(Result.ALLOW);
    }
    else if (world.getBlockState(pos).getBlock() == BlockRegistry.flower_cyan) {
      event.setResult(Result.ALLOW);
      if (world.rand.nextDouble() < 0.5) {
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.flower_cyan));
      }
    }
  }

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) event.getEntity();
      if (p.getPersistentData().getBoolean("cyclic_sleeping")) { // TODO: const in sleeping mat
        event.setResult(Result.ALLOW);
      }
    }
  }

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack().isEmpty()) {
      return;
    }
    if (event.getItemStack().getItem() instanceof ItemScaffolding
        && event.getPlayer().isCrouching()) {
      scaffoldHit(event);
    }
  }

  private void scaffoldHit(RightClickBlock event) {
    ItemScaffolding item = (ItemScaffolding) event.getItemStack().getItem();
    Direction opp = event.getFace().getOpposite();
    BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, item.getBlock());
    if (event.getWorld().isAirBlock(dest)) {
      event.getWorld().setBlockState(dest, item.getBlock().getDefaultState());
      ItemStack stac = event.getPlayer().getHeldItem(event.getHand());
      UtilItemStack.shrink(event.getPlayer(), stac);
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getItemStack().getItem() instanceof ItemEntityInteractable) {
      ItemEntityInteractable item = (ItemEntityInteractable) event.getItemStack().getItem();
      item.interactWith(event);
    }
  }

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    PlayerEntity player = event.getPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held.isEmpty()) {
      return;
    }
    World world = player.getEntityWorld();
    ///////////// shape
    if (held.getItem() instanceof ShapeCard && player.isCrouching()) {
      BlockState target = world.getBlockState(event.getPos());
      ShapeCard.setBlockState(held, target);
      UtilChat.sendStatusMessage(player, target.getBlock().getTranslationKey());
    }
    ///////////////// builders
    if (held.getItem() instanceof BuilderItem) {
      if (BuilderActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      BuilderActionType.setTimeout(held);
      event.setCanceled(true);
      if (player.isCrouching()) {
        //pick out target block
        BlockState target = world.getBlockState(event.getPos());
        BuilderActionType.setBlockState(held, target);
        UtilChat.sendStatusMessage(player, target.getBlock().getTranslationKey());
      }
      else {
        //change size
        if (!world.isRemote) {
          BuilderActionType.toggle(held);
        }
        UtilSound.playSound(player, SoundRegistry.TOOL_MODE);
        UtilChat.sendStatusMessage(player, UtilChat.lang(BuilderActionType.getName(held)));
      }
    }
    if (held.getItem() instanceof AntimatterEvaporatorWandItem) {
      AntimatterEvaporatorWandItem.toggleMode(player, held);
    }
  }

  @SubscribeEvent
  public void onPlayerPickup(EntityItemPickupEvent event) {
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      ItemEntity itemEntity = event.getItem();
      ItemStack resultStack = itemEntity.getItem();
      int origCount = resultStack.getCount();
      for (Integer i : StorageBagItem.getAllBagSlots(player)) {
        ItemStack bag = player.inventory.getStackInSlot(i);
        switch (StorageBagItem.getPickupMode(bag)) {
          case EVERYTHING:
            resultStack = StorageBagItem.tryInsert(bag, resultStack);
          break;
          case FILTER:
            resultStack = StorageBagItem.tryFilteredInsert(bag, resultStack);
          break;
          case NOTHING:
          break;
        }
        if (resultStack.isEmpty()) {
          break;
        }
      }
      if (resultStack.getCount() != origCount) {
        itemEntity.setItem(resultStack);
        event.setResult(Result.ALLOW);
      }
    }
  }
}
