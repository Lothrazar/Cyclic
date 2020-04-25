package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
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
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    FireEntity ent = new FireEntity(playerIn, worldIn);
    ent.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
    worldIn.addEntity(ent);
    ent.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 1, ent.getPosZ());
    worldIn.addEntity(ent);
    ent.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 2, ent.getPosZ());
    worldIn.addEntity(ent);
    ent.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 3, ent.getPosZ());
    worldIn.addEntity(ent);
    playerIn.getCooldownTracker().setCooldown(stack.getItem(), 10);
    UtilItemStack.damageItem(playerIn, stack);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
