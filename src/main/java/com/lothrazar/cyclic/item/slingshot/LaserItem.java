package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class LaserItem extends ItemBaseCyclic {

  public LaserItem(Properties properties) {
    super(properties);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.SPEAR;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    //    playerIn.startUsingItem(handIn);
    shootMe(world, player, new StoneEntity(player, world), 0, ItemBaseCyclic.VELOCITY_MAX * 2);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }
}
