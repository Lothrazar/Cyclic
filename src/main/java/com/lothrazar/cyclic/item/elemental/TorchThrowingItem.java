package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.torchthrow.EntityTorchBolt;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TorchThrowingItem extends ItemBaseCyclic {

  // from 1.10.2
  public TorchThrowingItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player shooter, InteractionHand hand) {
    shootMe(world, shooter, new EntityTorchBolt(shooter, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    if (!shooter.isCreative()) {
      shooter.getItemInHand(hand).shrink(1);
    }
    if (!shooter.getCooldowns().isOnCooldown(this)) {
      shooter.getCooldowns().addCooldown(ItemRegistry.ENDER_TORCH.get(), 5);
      SoundUtil.playSound(shooter, SoundEvents.EGG_THROW);
    }
    return super.use(world, shooter, hand);
  }
}
