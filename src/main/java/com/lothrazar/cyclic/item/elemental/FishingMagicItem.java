package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FishingMagicItem extends ItemBaseCyclic {

  // from 1.10.2
  public FishingMagicItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player shooter, InteractionHand hand) {
    shootMe(world, shooter, new FishingEnderEntity(shooter, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    if (!shooter.isCreative()) {
      shooter.getItemInHand(hand).shrink(1);
    }
    shooter.getCooldowns().addCooldown(ItemRegistry.ENDER_FISHING.get(), 5);
    SoundUtil.playSound(shooter, SoundRegistry.GOODLAUNCH.get());
    return super.use(world, shooter, hand);
  }
}
