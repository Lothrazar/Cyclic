package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.facade.IBlockFacade;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.enchant.MultiBowEnchant;
import com.lothrazar.cyclic.item.SleepingMatItem;
import com.lothrazar.cyclic.item.animal.ItemHorseEnder;
import com.lothrazar.cyclic.item.bauble.CharmBase;
import com.lothrazar.cyclic.item.bauble.SoulstoneCharm;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.elemental.AntimatterEvaporatorWandItem;
import com.lothrazar.cyclic.item.enderbook.EnderBookItem;
import com.lothrazar.cyclic.item.equipment.GlowingHelmetItem;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem;
import com.lothrazar.cyclic.item.food.LoftyStatureApple;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.net.BlockFacadeMessage;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.library.util.AttributesUtil;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
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
    ItemStack shield = event.getEntity().getUseItem();
    if (shield.getItem() instanceof ShieldCyclicItem shieldItem) {
      if (event.getEntity() instanceof Player playerIn) {
        if (playerIn.getCooldowns().isOnCooldown(shield.getItem())) {
          SoundUtil.playSound(playerIn, SoundEvents.SHIELD_BREAK);
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
    if (!(event.getEntity() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getEntity();
    if (player.getMainHandItem().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      EnderBookItem.cancelTeleport(player.getMainHandItem());
    }
    if (player.getOffhandItem().getItem() == ItemRegistry.ENDER_BOOK.get()) {
      EnderBookItem.cancelTeleport(player.getOffhandItem());
    }
  }

  @SubscribeEvent
  public void onCriticalHitEvent(CriticalHitEvent event) {
    if (event.getEntity() instanceof Player) {
      Player ply = event.getEntity();
      //      ply.getAttribute(Attributes.)
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_CRIT.get());
      if (!find.isEmpty()) {
        // This is by default 1.5F for ciritcal hits and 1F for normal hits . 
        event.setDamageModifier(3F);
        ItemStackUtil.damageItem(ply, find);
      }
    }
  }

  @SubscribeEvent
  public void onArrowLooseEvent(ArrowLooseEvent event) {
    //this event is only used for multishot enchantment 
    if (!MultiBowEnchant.CFG.get()) {
      return;
    }
    ItemStack stackBow = event.getBow();
    Player player = event.getEntity();
    Level worldIn = player.level();
    if (worldIn.isClientSide == false) {
      int level = EnchantRegistry.MULTIBOW.get().getCurrentLevelTool(stackBow);
      if (level <= 0) {
        return;
      }
      //use cross product to push arrows out to left and right
      Vec3 playerDirection = EntityUtil.lookVector(player.getYRot(), player.getXRot());
      Vec3 left = playerDirection.cross(new Vec3(0, 1, 0));
      Vec3 right = playerDirection.cross(new Vec3(0, -1, 0));
      MultiBowEnchant.spawnArrow(worldIn, player, stackBow, event.getCharge(), left.normalize());
      MultiBowEnchant.spawnArrow(worldIn, player, stackBow, event.getCharge(), right.normalize());
    }
  }

  @SubscribeEvent
  public void onLivingKnockBackEvent(LivingKnockBackEvent event) {
    if (event.getEntity() instanceof Player) {
      Player ply = (Player) event.getEntity();
      if (ply.isBlocking()) {
        ItemStack held = ply.getItemInHand(ply.getUsedItemHand());
        if (held.getItem() instanceof ShieldCyclicItem shieldType) {
          shieldType.onKnockback(event);
        }
      }
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_KNOCKBACK_RESIST.get());
      if (!find.isEmpty()) {
        event.setCanceled(true);
        ItemStackUtil.damageItem(ply, find);
      }
    }
  }

  @SubscribeEvent
  public void onProjectileImpactEvent(ProjectileImpactEvent event) {
    Projectile arrow = event.getProjectile();
    if (arrow == null || event.getRayTraceResult() == null) {
      return;
    }
    Level world = arrow.level();
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
        ItemStackUtil.damageItem(ply, find);
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
          ItemStackUtil.damageItem(ply, find);
        }
      }
    }
  }

  @SubscribeEvent
  public void onPotionAddedEvent(MobEffectEvent.Added event) {
    if (event.getEntity() instanceof Player) {
      Player ply = (Player) event.getEntity();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_ANTIPOTION.get());
      if (!find.isEmpty()) {
        event.getEffectInstance().duration = 0;
        ItemStackUtil.damageItem(ply, find);
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_STEALTHPOTION.get());
      if (!find.isEmpty()) {
        if (event.getOldEffectInstance() != null) {
          event.getOldEffectInstance().visible = false;
        }
        event.getEffectInstance().visible = false;
      }
      find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_BOOSTPOTION.get());
      if (!find.isEmpty()) {
        int boost = event.getEffectInstance().duration / 2;
        event.getEffectInstance().duration += boost;
        ItemStackUtil.damageItem(ply, find);
      }
    }
  }

  @SubscribeEvent
  public void onEntityDamage(LivingDamageEvent event) {
    DamageSource src = event.getSource();
    if (event.getEntity() instanceof Player player) {
      if (src.is(DamageTypes.EXPLOSION)) {
        //explosion thingy
        this.damageFinder(event, player, ItemRegistry.CHARM_CREEPER.get(), 0);
      }
      if (src.is(DamageTypes.FALL) || src.is(DamageTypes.CACTUS) || src.is(DamageTypes.SWEET_BERRY_BUSH)) {
        this.damageFinder(event, player, ItemRegistry.CHARM_LONGFALL.get(), 0);
      }
      else if (src.is(DamageTypes.FLY_INTO_WALL) || src.is(DamageTypes.IN_WALL)) {
        //stone lung
        this.damageFinder(event, player, ItemRegistry.CHARM_STONE.get(), 0);
      }
      else if (src.is(DamageTypes.MAGIC) || src.is(DamageTypes.DRAGON_BREATH)) {
        this.damageFinder(event, player, ItemRegistry.CHARM_MAGICDEF.get(), 0.5F);
      }
      else if (src.is(DamageTypes.STARVE)) {
        if (this.damageFinder(event, player, ItemRegistry.CHARM_STARVATION.get(), 0)) {
          player.getFoodData().eat(0, 0.2F);
        }
      }
      else if (src.is(DamageTypes.DROWN)) {
        if (this.damageFinder(event, player, ItemRegistry.CHARM_WATER.get(), 0)) {
          //and a holdover bonus
          MobEffectInstance eff = new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 10, 1);
          eff.visible = false;
          eff.showIcon = false;
          player.addEffect(eff);
        }
      }
      else if (src.is(DamageTypes.LAVA) || src.is(DamageTypes.IN_FIRE) || src.is(DamageTypes.ON_FIRE)) {
        this.damageFinder(event, player, ItemRegistry.CHARM_FIRE.get(), 0);
        this.damageFinder(event, player, ItemRegistry.CHARM_ULTIMATE.get(), 0);
      }
    }
    else if (src.getEntity() instanceof Player) {
      //player DEALING damage
      Player ply = (Player) src.getEntity();
      ItemStack find = CharmUtil.getIfEnabled(ply, ItemRegistry.CHARM_VENOM.get());
      if (!find.isEmpty() && ply.level().random.nextDouble() < 0.25F) {
        int seconds = 2 + ply.level().random.nextInt(4);
        event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 20 * seconds, 0));
        ItemStackUtil.damageItem(ply, find);
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
      ItemStackUtil.damageItem(player, find);
      return true;
    }
    return false;
  }

  @SubscribeEvent
  public void onPlayerDeath(LivingDeathEvent event) {
    //
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();
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
        event.getEntity().getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthModifier);
      }
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingTickEvent event) {
    LivingEntity liv = event.getEntity();
    tryItemHorseEnder(liv);
    if (liv instanceof Player player) {
      CharmBase.onEntityUpdate(player);
      //step
      LoftyStatureApple.onUpdate(player);
      GlowingHelmetItem.onEntityUpdate(event);
    }
  }

  @SubscribeEvent
  public void onXpPickup(PlayerXpEvent.PickupXp event) {
    Player player = event.getEntity();
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
          && liv.canDrownInFluidType(ForgeMod.WATER_TYPE.get()) == false
          && liv.getAirSupply() < liv.getMaxAirSupply()
          && !liv.hasEffect(MobEffects.WATER_BREATHING)) {
        liv.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 4));
        liv.addEffect(new MobEffectInstance(PotionEffectRegistry.SWIMSPEED.get(), 20 * 60, 1));
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
    Level world = event.getLevel();
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
        ItemStackUtil.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_CYAN.get()));
      }
    }
    else if (state.getBlock() == BlockRegistry.FLOWER_PURPLE_TULIP.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.25) {
        ItemStackUtil.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_PURPLE_TULIP.get()));
      }
    }
    else if (state.getBlock() == BlockRegistry.FLOWER_ABSALON_TULIP.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.25) {
        ItemStackUtil.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_ABSALON_TULIP.get()));
      }
    }
    else if (state.getBlock() == BlockRegistry.FLOWER_LIME_CARNATION.get()) {
      event.setResult(Result.ALLOW);
      if (world.random.nextDouble() < 0.25) {
        ItemStackUtil.drop(world, pos, new ItemStack(BlockRegistry.FLOWER_LIME_CARNATION.get()));
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
    Player player = event.getEntity();
    if (event.getItemStack().getItem() instanceof ItemScaffolding && player.isCrouching()) {
      scaffoldHit(event);
    }
    if (player.isCrouching() && event.getItemStack().is(DataTags.WRENCH)) {
      if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof CableBase) {
        //cyclic cable
        //test? maybe config disable? 
        player.swing(event.getHand());
        CableBase.crouchClick(event, event.getLevel().getBlockState(event.getPos()));
        event.setCanceled(true);
        SoundUtil.playSound(player, SoundRegistry.THUNK.get(), 0.2F, 1F);
      }
    }
  }

  private void scaffoldHit(RightClickBlock event) {
    ItemScaffolding item = (ItemScaffolding) event.getItemStack().getItem();
    Direction opp = event.getFace().getOpposite();
    BlockPos dest = LevelWorldUtil.nextReplaceableInDirection(event.getLevel(), event.getPos(), opp, 16, item.getBlock());
    if (event.getLevel().isEmptyBlock(dest)) {
      event.getLevel().setBlockAndUpdate(dest, item.getBlock().defaultBlockState());
      ItemStack stac = event.getEntity().getItemInHand(event.getHand());
      ItemStackUtil.shrink(event.getEntity(), stac);
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
    Player player = event.getEntity();
    ItemStack held = player.getItemInHand(event.getHand());
    Level world = player.getCommandSenderWorld();
    BlockState target = world.getBlockState(event.getPos());
    if (player.isCrouching()
        && target.getBlock() instanceof IBlockFacade) {
      //
      onHitFacadeHandler(event, player, held, target);
      //
    }
    //    if (held.isEmpty()) {
    //      return;
    //    }
    ///////////// shape
    if (held.getItem() instanceof ShapeCard && player.isCrouching()) {
      ShapeCard.setBlockState(held, target);
      ChatUtil.sendStatusMessage(player, target.getBlock().getDescriptionId());
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
        BuilderActionType.setBlockState(held, target);
        ChatUtil.sendStatusMessage(player, target.getBlock().getDescriptionId());
        event.setCanceled(true);
        SoundUtil.playSound(player, SoundRegistry.DCOIN.get(), 0.3F, 1F);
      }
      else {
        //change size
        if (!world.isClientSide) {
          BuilderActionType.toggle(held);
        }
        SoundUtil.playSound(player, SoundRegistry.TOOL_MODE.get());
        ChatUtil.sendStatusMessage(player, ChatUtil.lang(BuilderActionType.getName(held)));
        event.setCanceled(true);
      }
    }
    if (held.getItem() instanceof AntimatterEvaporatorWandItem) {
      AntimatterEvaporatorWandItem.toggleMode(player, held);
    }
  }

  private void onHitFacadeHandler(PlayerInteractEvent.LeftClickBlock event, Player player, ItemStack held, BlockState target) {
    if (held.isEmpty() && event.getLevel().isClientSide()) {
      PacketRegistry.INSTANCE.sendToServer(new BlockFacadeMessage(event.getPos(), true));
    }
    else {
      Block block = Block.byItem(held.getItem());
      if (block == null || block == Blocks.AIR) {
        return;
      }
      if (target.getBlock() instanceof CableBase) {
        if (!ConfigRegistry.CABLE_FACADES.get()) {
          return;
        }
      }
      if (!ConfigRegistry.isFacadeAllowed(held)) {
        ModCyclic.LOGGER.info("not allowed to use this item as a facade from config: " + held.getItem());
        return;
      }
      if (event.getLevel().isClientSide()) {
        onHitFacadeClient(event, player, held, block);
      }
    }
    //cancel the event so creative players will not break it
    event.setCanceled(true);
  }

  @OnlyIn(Dist.CLIENT)
  private void onHitFacadeClient(PlayerInteractEvent.LeftClickBlock event, Player player, ItemStack held, Block block) {
    //pick the block, write to tags, and send to server
    boolean pickFluids = false;
    BlockHitResult bhr = (BlockHitResult) player.pick(player.getBlockReach(), 1, pickFluids);
    BlockPlaceContext context = new BlockPlaceContext(player, event.getHand(), held, bhr);
    BlockState facadeState = block.getStateForPlacement(context);
    CompoundTag tags = (facadeState == null) ? null : NbtUtils.writeBlockState(facadeState);
    PacketRegistry.INSTANCE.sendToServer(new BlockFacadeMessage(event.getPos(), tags));
  }

  @SubscribeEvent
  public void onPlayerPickup(EntityItemPickupEvent event) {
    if (event.getEntity() instanceof Player) {
      Player player = event.getEntity();
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
