package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SnowScepter extends ItemBaseCyclic {

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
    shootMe(world, player, new SnowEntity(player, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    shootMe(world, player, new SnowEntity(player, world), 10, ItemBaseCyclic.VELOCITY_MAX);
    shootMe(world, player, new SnowEntity(player, world), -10, ItemBaseCyclic.VELOCITY_MAX);
    player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);
    ItemStackUtil.damageItem(player, stack);
    SoundUtil.playSound(player, SoundRegistry.FROST_STAFF_LAUNCH.get());
    return super.use(world, player, handIn);
  }
}
