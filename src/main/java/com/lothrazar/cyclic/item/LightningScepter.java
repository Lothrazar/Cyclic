package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class LightningScepter extends ItemBase {

  public LightningScepter(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    LightningEntity ent = new LightningEntity(playerIn, worldIn);
    ent.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
    worldIn.addEntity(ent);
    playerIn.getCooldownTracker().setCooldown(stack.getItem(), 10);
    UtilItemStack.damageItem(stack);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
