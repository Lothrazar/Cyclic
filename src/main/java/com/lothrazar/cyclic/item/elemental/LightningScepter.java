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

public class LightningScepter extends ItemBase {

  public LightningScepter(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    ItemStack stack = player.getHeldItem(handIn);
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemRightClick(worldIn, player, handIn);
    }
    shootMe(worldIn, player, new LightningEntity(player, worldIn));
    //    ent.shoot(player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
    //    worldIn.addEntity(ent);
    player.getCooldownTracker().setCooldown(stack.getItem(), 20);
    UtilItemStack.damageItem(player, stack);
    UtilSound.playSound(player, SoundRegistry.lightning_staff_launch);
    return super.onItemRightClick(worldIn, player, handIn);
  }
}
