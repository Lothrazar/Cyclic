package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LightningScepter extends ItemBaseCyclic {

  public LightningScepter(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack stack = player.getItemInHand(handIn);
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, player, handIn);
    }
    shootMe(worldIn, player, new LightningEntity(player, worldIn), 0, ItemBaseCyclic.VELOCITY_MAX);

    player.getCooldowns().addCooldown(stack, 20);
    ItemStackUtil.damageItem(player, stack);
    SoundUtil.playSound(player, SoundRegistry.LIGHTNING_STAFF_LAUNCH.get());
    return super.use(worldIn, player, handIn);
  }
}
