package com.lothrazar.cyclic.item.endereye;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.item.EyeOfEnderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ItemEnderEyeReuse extends ItemBase {

  private static final int COOLDOWN = 10;
  private static int DUNGEONRADIUS = 64;
  private boolean USE_THREADING;

  public ItemEnderEyeReuse(Properties properties) {
    super(properties.maxDamage(256));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!worldIn.isRemote) {
      BlockPos blockpos = ((ServerWorld) worldIn).getChunkProvider().getChunkGenerator().findNearestStructure(worldIn, "Stronghold", new BlockPos(player), 100, false);
      if (blockpos != null) {
        double posX = player.getPosX();
        double posY = player.getPosY();
        double posZ = player.getPosZ();
        EyeOfEnderEntity eyeofenderentity = new EyeOfEnderEntityNodrop(worldIn, posX, posY + player.getHeight() / 2.0F, posZ);
        eyeofenderentity.moveTowards(blockpos);
        worldIn.addEntity(eyeofenderentity);
        if (player instanceof ServerPlayerEntity) {
          CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) player, blockpos);
        }
        worldIn.playSound((PlayerEntity) null, posX, posY, posZ, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F,
            0.4F / (random.nextFloat() * 0.4F + 0.8F));
        worldIn.playEvent((PlayerEntity) null, 1003, new BlockPos(player), 0);
        if (!player.abilities.isCreativeMode) {
          UtilItemStack.damageItem(stack);
        }
        player.addStat(Stats.ITEM_USED.get(this));
        player.getCooldownTracker().setCooldown(stack.getItem(), 10);
      }
    }
    return super.onItemRightClick(worldIn, player, hand);
  }
}
