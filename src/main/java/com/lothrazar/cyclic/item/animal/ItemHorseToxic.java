package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.core.IEntityInteractable;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseToxic extends ItemBaseCyclic implements IEntityInteractable {

  public ItemHorseToxic(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(PlayerInteractEvent.EntityInteract event) {
    if (event.getItemStack().getItem() != this
        || !(event.getTarget() instanceof Horse horseOld)
        || !(event.getLevel() instanceof ServerLevel serverLevel)
        || event.getEntity().getCooldowns().isOnCooldown(event.getItemStack())) {
      return;
    }
    ZombieHorse zombieNew = EntityType.ZOMBIE_HORSE.spawn(serverLevel, (ItemStack) null, null, event.getPos(), EntitySpawnReason.NATURAL, false, false);
    if (zombieNew == null) {
      return;
    }
    copyAttributes(horseOld, zombieNew);
    zombieNew.setHealth(horseOld.getHealth());
    if (horseOld.isBaby()) {
      zombieNew.setBaby(true);
    }
    EntityReference<net.minecraft.world.entity.LivingEntity> ownerRef = horseOld.getOwnerReference();
    if (horseOld.isTamed() && ownerRef != null && event.getEntity().getUUID().equals(ownerRef.getUUID())) {
      zombieNew.tameWithName(event.getEntity());
    }
    if (horseOld.isSaddled()) {
      // copy the real saddle item in case its modded
      ItemStack saddle = horseOld.getInventory().getItem(0).copy();
      zombieNew.setItemSlot(EquipmentSlot.SADDLE, saddle);
    }
    ItemStack body = horseOld.getBodyArmorItem();
    if (!body.isEmpty()) {
      ItemStackUtil.drop(event.getLevel(), event.getPos(), body);
    }
    if (horseOld.hasCustomName()) {
      zombieNew.setCustomName(horseOld.getCustomName());
    }
    horseOld.remove(Entity.RemovalReason.DISCARDED);
    event.getEntity().getCooldowns().addCooldown(event.getItemStack(), 10);
    event.getItemStack().shrink(1);
    event.setCanceled(true);
    event.setCancellationResult(InteractionResult.SUCCESS);
  }

  private static void copyAttributes(AbstractHorse from, AbstractHorse to) {
    copyAttribute(from, to, Attributes.MAX_HEALTH);
    copyAttribute(from, to, Attributes.MOVEMENT_SPEED);
    copyAttribute(from, to, Attributes.JUMP_STRENGTH);
  }

  private static void copyAttribute(AbstractHorse from, AbstractHorse to, Holder<Attribute> attr) {
    AttributeInstance src = from.getAttribute(attr);
    AttributeInstance dst = to.getAttribute(attr);
    if (src != null && dst != null) {
      dst.setBaseValue(src.getBaseValue());
    }
  }
}
