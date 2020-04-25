package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SnowScepter extends ItemBase {

  public SnowScepter(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    ItemStack stack = player.getHeldItem(handIn);
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemRightClick(worldIn, player, handIn);
    }
    SnowEntity ent = new SnowEntity(player, worldIn);
    ent.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 0.5F);
    worldIn.addEntity(ent);
    ent = new SnowEntity(player, worldIn);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 1, ent.getPosZ());
    ent.shoot(player, player.rotationPitch, player.rotationYaw - 0.2F, 0.0F, 1.5F, 1.0F);
    worldIn.addEntity(ent);
    ent = new SnowEntity(player, worldIn);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 2, ent.getPosZ());
    ent.shoot(player, player.rotationPitch, player.rotationYaw - 0.5F, 0.0F, 1.5F, 1.0F);
    worldIn.addEntity(ent);
    player.getCooldownTracker().setCooldown(stack.getItem(), 6);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.frost_staff_launch);
    return super.onItemRightClick(worldIn, player, handIn);
  }
}
