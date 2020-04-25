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

public class FireScepter extends ItemBase {

  public FireScepter(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    ItemStack stack = player.getHeldItem(handIn);
    FireEntity ent = new FireEntity(player, worldIn);
    ent.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
    worldIn.addEntity(ent);
    ent = new FireEntity(player, worldIn);
    ent.shoot(player, player.rotationPitch, player.rotationYaw - 0.1F, 0.0F, 1.5F, 1.0F);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 1, ent.getPosZ());
    worldIn.addEntity(ent);
    ent = new FireEntity(player, worldIn);
    ent.shoot(player, player.rotationPitch, player.rotationYaw - 0.2F, 0.0F, 1.5F, 1.0F);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 2, ent.getPosZ());
    worldIn.addEntity(ent);
    ent = new FireEntity(player, worldIn);
    ent.shoot(player, player.rotationPitch, player.rotationYaw - 0.5F, 0.0F, 1.5F, 1.0F);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 3, ent.getPosZ());
    worldIn.addEntity(ent);
    player.getCooldownTracker().setCooldown(stack.getItem(), 16);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.fireball_staff_launch);
    return super.onItemRightClick(worldIn, player, handIn);
  }
}
