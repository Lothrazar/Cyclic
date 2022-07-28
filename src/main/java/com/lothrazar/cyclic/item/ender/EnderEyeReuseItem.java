package com.lothrazar.cyclic.item.ender;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnderEyeReuseItem extends ItemBaseCyclic {

  private static final int MAX_RANGE = 100;

  public EnderEyeReuseItem(Properties properties) {
    super(properties.durability(256));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (!worldIn.isClientSide && worldIn instanceof ServerLevel) {
      ServerLevel sw = (ServerLevel) worldIn;
      BlockPos closestBlockPos = sw.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, player.blockPosition(), MAX_RANGE, false);
      if (closestBlockPos != null) {
        double posX = player.getX();
        double posY = player.getY();
        double posZ = player.getZ();
        EyeOfEnderEntityNodrop eyeofenderentity = new EyeOfEnderEntityNodrop(worldIn, posX, posY + player.getBbHeight() / 2.0F, posZ);
        eyeofenderentity.signalTo(closestBlockPos);
        worldIn.addFreshEntity(eyeofenderentity);
        if (player instanceof ServerPlayer) {
          CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) player, closestBlockPos);
        }
        worldIn.playSound((Player) null, posX, posY, posZ, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F,
            0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        worldIn.levelEvent((Player) null, 1003, new BlockPos(player.blockPosition()), 0);
        ItemStackUtil.damageItem(player, stack);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(stack.getItem(), 10);
        return InteractionResultHolder.success(player.getItemInHand(hand));
      }
    }
    return super.use(worldIn, player, hand);
  }
}
