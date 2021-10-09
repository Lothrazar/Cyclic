package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FireScepter extends ItemBase {

  public FireScepter(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
    shootMe(worldIn, player, new FireEntity(player, worldIn), 0, ItemBase.VELOCITY_MAX);
    shootMe(worldIn, player, new FireEntity(player, worldIn), 10, ItemBase.VELOCITY_MAX);
    shootMe(worldIn, player, new FireEntity(player, worldIn), -10, ItemBase.VELOCITY_MAX);
    //    ent = new FireEntity(player, worldIn);
    //    ent.shoot(player.rotationPitch, player.rotationYaw - 0.1F, 0.0F, 1.5F, 1.0F);
    //    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 1, ent.getPosZ());
    //    worldIn.addEntity(ent);
    //    ent = new FireEntity(player, worldIn);
    //    ent.shoot(player.rotationPitch, player.rotationYaw - 0.2F, 0.0F, 1.5F, 1.0F);
    //    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 2, ent.getPosZ());
    //    worldIn.addEntity(ent);
    //    ent = new FireEntity(player, worldIn);
    //    ent.shoot(player.rotationPitch, player.rotationYaw - 0.5F, 0.0F, 1.5F, 1.0F);
    //    ent.forceSetPosition(ent.getPosX(), ent.getPosY() + 3, ent.getPosZ());
    //    worldIn.addEntity(ent);
    ItemStack stack = player.getItemInHand(handIn);
    player.getCooldowns().addCooldown(stack.getItem(), 16);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.FIREBALL_STAFF_LAUNCH);
    return super.use(worldIn, player, handIn);
  }
}
