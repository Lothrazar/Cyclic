package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class SnowScepter extends ItemBase {

  private static final int COOLDOWN = 6;

  public SnowScepter(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
    ItemStack stack = player.getItemInHand(handIn);
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.use(world, player, handIn);
    }
    shootMe(world, player, new SnowEntity(player, world), 0, ItemBase.VELOCITY_MAX);
    shootMe(world, player, new SnowEntity(player, world), 10, ItemBase.VELOCITY_MAX);
    shootMe(world, player, new SnowEntity(player, world), -10, ItemBase.VELOCITY_MAX);
    player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.FROST_STAFF_LAUNCH);
    return super.use(world, player, handIn);
  }
}
