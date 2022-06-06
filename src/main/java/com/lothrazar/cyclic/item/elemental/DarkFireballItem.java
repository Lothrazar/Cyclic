package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DarkFireballItem extends ItemBaseCyclic {

  //unlike orange fireball. no hurty for holding
  public DarkFireballItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player shooter, InteractionHand hand) {
    shootMe(world, shooter, new FireEntity(shooter, world), 0, ItemBaseCyclic.VELOCITY_MAX);
    if (!shooter.isCreative()) {
      shooter.getItemInHand(hand).shrink(1);
    }
    shooter.getCooldowns().addCooldown(ItemRegistry.FIREBALL_DARK.get(), 5); // less cooldown than orange
    return super.use(world, shooter, hand);
  }
}
