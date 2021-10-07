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

public class LightningScepter extends ItemBase {

  public LightningScepter(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack stack = player.getItemInHand(handIn);
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.use(worldIn, player, handIn);
    }
    shootMe(worldIn, player, new LightningEntity(player, worldIn), 0, ItemBase.VELOCITY_MAX);
    //    ent.shoot(player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
    //    worldIn.addEntity(ent);
    player.getCooldowns().addCooldown(stack.getItem(), 20);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.LIGHTNING_STAFF_LAUNCH);
    return super.use(worldIn, player, handIn);
  }
}
