package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.enchant.Multishot;
import com.lothrazar.cyclic.item.AntimatterEvaporatorWandItem;
import com.lothrazar.cyclic.item.SleepingMatItem;
import com.lothrazar.cyclic.item.animal.ItemHorseEnder;
import com.lothrazar.cyclic.item.bauble.CharmBase;
import com.lothrazar.cyclic.item.bauble.SoulstoneCharm;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.enderbook.EnderBookItem;
import com.lothrazar.cyclic.item.equipment.GlowingHelmetItem;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem;
import com.lothrazar.cyclic.item.food.LoftyStatureApple;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.AttributesUtil;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
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
  public void onShieldBlock(ShieldBlockEvent event) {
    ItemStack shield = event.getEntityLiving().getUseItem();
    if (shield.getItem() instanceof ShieldCyclicItem shieldItem) {
      if (event.getEntityLiving() instanceof Player playerIn) {
        if (playerIn.getCooldowns().isOnCooldown(shield.getItem())) {
          UtilSound.playSound(playerIn, SoundEvents.SHIELD_BREAK);
          event.setCanceled(true);
          return;
        }
        shieldItem.onShieldBlock(event, playerIn);
      }
      else {
        shieldItem.onShieldBlock(event, null);
      }
    }
  }

  @SubscribeEvent
  public void onLivingJumpEvent(LivingJumpEvent event) {
    if (!(event.getEntityLiving() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getEntityLiving();
    if (player.getMainHandItem().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      EnderBookItem.cancelTeleport(player.getMainHandItem());
    }
    if (player.getOffhandItem().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      EnderBookItem.cancelTeleport(player.getOffhandItem());
    }
  }

  @SubscribeEvent
  public void onCriticalHitEvent(CriticalHitEvent event) {
    if (event.getEntityLiving() instanceof Player) {
      Player ply = (Player) event.getEntityLiving();
      //      ply.getAttribute(Attributes.)
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_CRIT.get());
      if (!find.isEmpty()) {
        // This is by default 1.5F for ciritcal hits and 1F for normal hits . 
        event.setDamageModifier(3F);
        UtilItemStack.damageItem(ply, find);
      }
    }
  }

  @SubscribeEvent
  public void onArrowLooseEvent(ArrowLooseEvent event) {
    //this event is only used for multishot enchantment 
    if (!Multishot.CFG.get()) {
      return;
    }
    ItemStack stackBow = event.getBow();
    Player player = event.getPlayer();
    Level worldIn = player.level;
    if (worldIn.isClientSide == false) {
      int level = EnchantRegistry.MULTIBOW.getCurrentLevelTool(stackBow);
      if (level <= 0) {
        return;
      }
      //use cross product to push arrows out to left and right
      Vec3 playerDirection = UtilEntity.lookVector(player.getYRot(), player.getXRot());
      Vec3 left = playerDirection.cross(new Vec3(0, 1, 0));
      Vec3 right = playerDirection.cross(new Vec3(0, -1, 0));
      Multishot.spawnArrow(worldIn, player, stackBow, event.getCharge(), left.normalize());
      Multishot.spawnArrow(worldIn, player, stackBow, event.getCharge(), right.normalize());
    }
  }

  @SubscribeEvent
  public void onLivingKnockBackEvent(LivingKnockBackEvent event) {
    if (event.getEntityLiving() instanceof Player) {
      Player ply = (Player) event.getEntityLiving();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_KNOCKBACK_RESIST.get());
      if (!find.isEmpty()) {
        event.setCanceled(true);
        UtilItemStack.damageItem(ply, find);
      }
    }
  }

  @SubscribeEvent
  public void onProjectileImpactEvent(ProjectileImpactEvent event) {
    Projectile arrow = event.getProjectile();
    if (arrow == null || event.getRayTraceResult() == null) {
      return;
    }
    Level world = arrow.level;
    Type hit = event.getRayTraceResult().getType();
    Entity shooter = arrow.getOwner(); // getShooter
    if (shooter instanceof Player) {
      Player ply = (Player) shooter;
      //      ply.isSprinting()
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.QUIVER_DMG.get());
      if (!find.isEmpty() && arrow instanceof AbstractArrow) {
        //        ModCyclic.LOGGER.info("before " + event.getArrow().getDamage());
        AbstractArrow arroww = (AbstractArrow) arrow;
        double boost = arroww.getBaseDamage() / 2;
        arroww.setBaseDamage(arroww.getBaseDamage() + boost);
        UtilItemStack.damageItem(ply, find);
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.QUIVER_LIT.get());
      if (!find.isEmpty() && world.random.nextDouble() < 0.25) {
        if (hit == HitResult.Type.ENTITY && ((EntityHitResult) event.getRayTraceResult()).getEntity() instanceof LivingEntity) {
          LivingEntity target = (LivingEntity) ((EntityHitResult) event.getRayTraceResult()).getEntity();
          target.setGlowingTag(true);
          //          ModCyclic.LOGGER.info(event.getEntity() + " eeeee" + event.getArrow().getDamage());
          BlockPos p = target.blockPosition();
          // lightning? 
          LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(world);
          lightningboltentity.moveTo(p.getX(), p.getY(), p.getZ());
          world.addFreshEntity(lightningboltentity);
          UtilItemStack.damageItem(ply, find);
        }
      }
    }
  }

  @SubscribeEvent
  public void onPotionAddedEvent(PotionAddedEvent event) {
    if (event.getEntityLiving() instanceof Player) {
      Player ply = (Player) event.getEntityLiving();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_ANTIPOTION.get());
      if (!find.isEmpty()) {
        event.getPotionEffect().duration = 0;
        UtilItemStack.damageItem(ply, find);
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_STEALTHPOTION.get());
      if (!find.isEmpty()) {
        if (event.getOldPotionEffect() != null) {
          event.getOldPotionEffect().visible = false;
        }
        event.getPotionEffect().visible = false;
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_BOOSTPOTION.get());
      if (!find.isEmpty()) {
        int boost = event.getPotionEffect().duration / 2;
        event.getPotionEffect().duration += boost;
        UtilItemStack.damageItem(ply, find);
      }
    }
  }

  @SubscribeEvent
  public void onEntityDamage(LivingDamageEvent event) {
    DamageSource src = event.getSource();
    if (event.getEntityLiving() instanceof Player) {
      Player player = (Player) event.getEntityLiving();
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
          player.getFoodData().eat(0, 0.2F);
        }
      }
      else if (src == DamageSource.DROWN) {
        if (this.damageFinder(event, player, ItemRegistry.CHARM_WATER.get(), 0)) {
          //and a holdover bonus
          MobEffectInstance eff = new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 10, 1);
          eff.visible = false;
          eff.showIcon = false;
          player.addEffect(eff);
        }
      }
      else if (src == DamageSource.LAVA || src == DamageSource.IN_FIRE || src == DamageSource.ON_FIRE) {
        this.damageFinder(event, player, ItemRegistry.CHARM_FIRE.get(), 0);
      }
    }
    else if (src.getEntity() instanceof Player) {
      //player DEALING damage
      Player ply = (Player) src.getEntity();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_VENOM.get());
      if (!find.isEmpty() && ply.level.random.nextDouble() < 0.25F) {
        int seconds = 2 + ply.level.random.nextInt(4);
        event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.POISON, 20 * seconds, 0));
        UtilItemStack.damageItem(ply, find);
      }
      if (ply.getUsedItemHand() != null && ply.getItemInHand(ply.getUsedItemHand()).isEmpty()) {
        //            ModCyclic.LOGGER.info("EMPTY hand damage");
      }
    }
  }

  private boolean damageFinder(LivingDamageEvent event, Player player, Item item, float factor) {
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
  public void onPlayerDeath(LivingDeathEvent event) {
    //
    if (event.getEntityLiving() instanceof Player) {
      Player player = (Player) event.getEntityLiving();
      //      Items.TOTEM_OF_UNDYING
      ItemStack charmStack = CharmUtil.getIfEnabled(player, ItemRegistry.SOULSTONE.get());
      if (SoulstoneCharm.checkTotemDeathProtection(event.getSource(), player, charmStack)) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public void onPlayerCloneDeath(PlayerEvent.Clone event) {
    AttributeInstance original = event.getOriginal().getAttribute(Attributes.MAX_HEALTH);
    if (original != null) {
      AttributeModifier healthModifier = original.getModifier(AttributesUtil.DEFAULT_ID);
      if (healthModifier != null) {
        event.getPlayer().getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthModifier);
      }
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity liv = event.getEntityLiving();
    tryItemHorseEnder(liv);
    if (liv instanceof Player) {
      Player player = (Player) liv;
      CharmBase.charmSpeed(player);
      CharmBase.charmLuck(player);
      CharmBase.charmAttackSpeed(player);
      CharmBase.charmExpSpeed(player);
      //step
      LoftyStatureApple.onUpdate(player);
      GlowingHelmetItem.onEntityUpdate(event);
    }
  }

  @SubscribeEvent
  public void onXpPickup(PlayerXpEvent.PickupXp event) {
    Player player = event.getPlayer();
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
          && liv.getAirSupply() < liv.getMaxAirSupply()
          && !liv.hasEffect(MobEffects.WATER_BREATHING)) {
        liv.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 4));
        liv.addEffect(new MobEffectInstance(PotionRegistry.PotionEffects.SWIMSPEED, 20 * 60, 1));
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.isOnFire()
          && !liv.hasEffect(MobEffects.FIRE_RESISTANCE)) {
        liv.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 60, 4));
        liv.clearFire();
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.fallDistance > 12
          && !liv.hasEffect(MobEffects.SLOW_FALLING)) {
        liv.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 60, 4));
        //        if (liv.getPassengers().size() > 0) {
        //          liv.getPassengers().get(0).addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 1));
        //        }
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.getHealth() < 6
          && !liv.hasEffect(MobEffects.ABSORPTION)) {
        liv.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60, 4));
        liv.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60, 4));
        ItemHorseEnder.onSuccess(liv);
      }
    }
  }

  @SubscribeEvent
  public void onBonemealEvent(BonemealEvent event) {
    Level world = event.getWorld();
    BlockPos pos = event.getPos();
    BlockState state = world.getBlockState(pos);
    if (ConfigRegistry.CYAN_PODZOL_LEGACY.get()) {
      //legacy feature, i meant to remove it in minecraft 1.16.2ish but forgot so now its a config
      if (state.getBlock() == Blocks.PODZOL && world.isEmptyBlock(pos.above())) {
        event.setResult(Result.ALLOW);
        world.setBlockAndUpdate(pos.above(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState());
      }
    }
    if (state.getBlock() == BlockRegistry.FLOWER_CYAN.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.5) {
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_CYAN.get()));
      }
    }
    else if (state.getBlock() == BlockRegistry.FLOWER_PURPLE_TULIP.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.25) {
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_PURPLE_TULIP.get()));
      }
    }
    else if (state.getBlock() == BlockRegistry.FLOWER_ABSALON_TULIP.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.25) {
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_ABSALON_TULIP.get()));
      }
    }
    else if (state.getBlock() == BlockRegistry.FLOWER_LIME_CARNATION.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.25) {
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_LIME_CARNATION.get()));
      }
    }
  }

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent event) {
    if (event.getEntity() instanceof Player) {
      Player p = (Player) event.getEntity();
      if (p.getPersistentData().getBoolean(SleepingMatItem.CYCLIC_SLEEPING)) {
        event.setResult(Result.ALLOW);
      }
    }
  }

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack().isEmpty()) {
      return;
    }
    Player player = event.getPlayer();
    if (event.getItemStack().getItem() instanceof ItemScaffolding && player.isCrouching()) {
      scaffoldHit(event);
    }
    if (player.isCrouching() && event.getItemStack().is(DataTags.WRENCH)) {
      if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof CableBase) {
        //cyclic cable
        //test? maybe config disable? 
        player.swing(event.getHand());
        event.getWorld().destroyBlock(event.getPos(), true);
        event.setCanceled(true);
      }
    }
  }

  private void scaffoldHit(RightClickBlock event) {
    ItemScaffolding item = (ItemScaffolding) event.getItemStack().getItem();
    Direction opp = event.getFace().getOpposite();
    BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, item.getBlock());
    if (event.getWorld().isEmptyBlock(dest)) {
      event.getWorld().setBlockAndUpdate(dest, item.getBlock().defaultBlockState());
      ItemStack stac = event.getPlayer().getItemInHand(event.getHand());
      UtilItemStack.shrink(event.getPlayer(), stac);
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getItemStack().getItem() instanceof IEntityInteractable) {
      IEntityInteractable item = (IEntityInteractable) event.getItemStack().getItem();
      item.interactWith(event);
    }
  }

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    Player player = event.getPlayer();
    ItemStack held = player.getItemInHand(event.getHand());
    if (held.isEmpty()) {
      return;
    }
    Level world = player.getCommandSenderWorld();
    ///////////// shape
    if (held.getItem() instanceof ShapeCard && player.isCrouching()) {
      BlockState target = world.getBlockState(event.getPos());
      ShapeCard.setBlockState(held, target);
      UtilChat.sendStatusMessage(player, target.getBlock().getDescriptionId());
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
        UtilChat.sendStatusMessage(player, target.getBlock().getDescriptionId());
        event.setCanceled(true);
        UtilSound.playSound(player, SoundRegistry.DCOIN, 0.3F, 1F);
      }
      else {
        //change size
        if (!world.isClientSide) {
          BuilderActionType.toggle(held);
        }
        UtilSound.playSound(player, SoundRegistry.TOOL_MODE);
        UtilChat.sendStatusMessage(player, UtilChat.lang(BuilderActionType.getName(held)));
        event.setCanceled(true);
      }
    }
    if (held.getItem() instanceof AntimatterEvaporatorWandItem) {
      AntimatterEvaporatorWandItem.toggleMode(player, held);
    }
  }

  @SubscribeEvent
  public void onPlayerPickup(EntityItemPickupEvent event) {
    if (event.getEntityLiving() instanceof Player) {
      Player player = (Player) event.getEntityLiving();
      ItemEntity itemEntity = event.getItem();
      ItemStack resultStack = itemEntity.getItem();
      int origCount = resultStack.getCount();
      for (Integer i : ItemStorageBag.getAllBagSlots(player)) {
        ItemStack bag = player.getInventory().getItem(i);
        switch (ItemStorageBag.getPickupMode(bag)) {
          case EVERYTHING:
            resultStack = ItemStorageBag.tryInsert(bag, resultStack);
          break;
          case FILTER:
            resultStack = ItemStorageBag.tryFilteredInsert(bag, resultStack);
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
