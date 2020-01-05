package com.lothrazar.cyclic.item.tool;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.EyeOfEnderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderEyeReuse extends ItemBase {

  public EnderEyeReuse(Properties properties) {
    super(properties.maxDamage(256));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    if (!worldIn.isRemote) {
      BlockPos blockpos = worldIn.getChunkProvider().getChunkGenerator().findNearestStructure(worldIn, "Stronghold", new BlockPos(playerIn), 100, false);
      if (blockpos != null) {
        EyeOfEnderEntity eyeofenderentity = new EyeOfEnderEntity(worldIn, playerIn.posX, playerIn.posY + (double) (playerIn.getHeight() / 2.0F), playerIn.posZ);
        eyeofenderentity.func_213863_b(new ItemStack(Items.ENDER_EYE));
        eyeofenderentity.moveTowards(blockpos);
        worldIn.addEntity(eyeofenderentity);
        if (playerIn instanceof ServerPlayerEntity) {
          CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) playerIn, blockpos);
        }
        worldIn.playSound((PlayerEntity) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        worldIn.playEvent((PlayerEntity) null, 1003, new BlockPos(playerIn), 0);
        if (!playerIn.abilities.isCreativeMode) {
          //           stack.shrink(1);
          UtilItemStack.damageItem(stack);
        }
        playerIn.addStat(Stats.ITEM_USED.get(this));
        playerIn.getCooldownTracker().setCooldown(stack.getItem(), 10);
        //         return new ActionResult<>(ActionResultType.SUCCESS, stack);
      }
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
