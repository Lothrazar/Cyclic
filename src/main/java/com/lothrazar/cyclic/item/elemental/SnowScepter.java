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

public class SnowScepter extends ItemBase {

  public SnowScepter(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
    ItemStack stack = player.getHeldItem(handIn);
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemRightClick(world, player, handIn);
    }
    shootMe(world, player, new SnowEntity(player, world));
    shootMe(world, player, new SnowEntity(player, world), 10);
    shootMe(world, player, new SnowEntity(player, world), -10);
    player.getCooldownTracker().setCooldown(stack.getItem(), 6);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.FROST_STAFF_LAUNCH);
    return super.onItemRightClick(world, player, handIn);
  }
}
