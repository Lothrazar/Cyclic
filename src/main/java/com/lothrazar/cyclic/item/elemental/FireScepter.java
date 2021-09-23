package com.lothrazar.cyclic.item.elemental;

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
    shootMe(worldIn, player, new FireEntity(player, worldIn), 0, ItemBase.VELOCITY_MAX);
    shootMe(worldIn, player, new FireEntity(player, worldIn), 10, ItemBase.VELOCITY_MAX);
    shootMe(worldIn, player, new FireEntity(player, worldIn), -10, ItemBase.VELOCITY_MAX);
    //    ent = new FireEntity(player, worldIn);
    //    ent.shoot(player.rotationPitch, player.rotationYaw - 0.1F, 0.0F, 1.5F, 1.0F);
    //    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 1, ent.getPosZ());
    //    worldIn.addEntity(ent);
    //    ent = new FireEntity(player, worldIn);
    //    ent.shoot(player.rotationPitch, player.rotationYaw - 0.2F, 0.0F, 1.5F, 1.0F);
    //    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 2, ent.getPosZ());
    //    worldIn.addEntity(ent);
    //    ent = new FireEntity(player, worldIn);
    //    ent.shoot(player.rotationPitch, player.rotationYaw - 0.5F, 0.0F, 1.5F, 1.0F);
    //    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 3, ent.getPosZ());
    //    worldIn.addEntity(ent);
    ItemStack stack = player.getHeldItem(handIn);
    player.getCooldownTracker().setCooldown(stack.getItem(), 16);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.FIREBALL_STAFF_LAUNCH);
    return super.onItemRightClick(worldIn, player, handIn);
  }
}
