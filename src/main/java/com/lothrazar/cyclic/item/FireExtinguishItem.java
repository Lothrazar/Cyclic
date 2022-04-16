package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FireExtinguishItem extends ItemBaseCyclic {

  private static final int TICKS_USING = 99000;

  public FireExtinguishItem(Properties properties) {
    super(properties.defaultDurability(1024 * 4));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return TICKS_USING; //bow has 72000
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    playerIn.startUsingItem(handIn);
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int chargeTimer) {
    int charge = this.getUseDuration(stack) - chargeTimer;
    float percentageCharged = BowItem.getPowerForTime(charge); //never zero, its from [0.03,1];
    if (percentageCharged < 0.1) {
      return; //not enough force to go with any realistic path
    }
    final int maxRadius = 32;
    int rad = (int) percentageCharged * maxRadius;
    for (BlockPos pos : UtilWorld.findBlocksByTag(world, entity.blockPosition(), BlockTags.FIRE, rad + 1)) {
      world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      UtilParticle.spawnParticle(world, ParticleTypes.SNOWFLAKE, pos, 6);
    }
    for (BlockPos pos : UtilWorld.findBlocksByTag(world, entity.blockPosition(), BlockTags.CAMPFIRES, rad + 1)) {
      BlockState cFire = world.getBlockState(pos);
      if (cFire.hasProperty(CampfireBlock.LIT)) {
        world.setBlock(pos, cFire.setValue(CampfireBlock.LIT, false), 3);
        UtilParticle.spawnParticle(world, ParticleTypes.SNOWFLAKE, pos, 6);
      }
    }
    UtilItemStack.damageItem(entity, stack);
  }
}
