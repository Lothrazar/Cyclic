package com.lothrazar.cyclic.item.endereye;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

public class ItemEnderEyeReuse extends ItemBase {

  public ItemEnderEyeReuse(Properties properties) {
    super(properties.maxDamage(256));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!worldIn.isRemote && worldIn instanceof ServerWorld) {
      ServerWorld sw = (ServerWorld) worldIn;
      ChunkGenerator chunkGenerator = sw.getChunkProvider().getChunkGenerator();
      //      chunkGenerator.func_235956_a_(p_235956_1_, p_235956_2_, p_235956_3_, p_235956_4_, p_235956_5_)
      //findNearestStructure
      BlockPos blockpos = chunkGenerator.func_235956_a_(sw, Structure.field_236375_k_, new BlockPos(player.getPosition()), 100, false);
      if (blockpos != null) {
        double posX = player.getPosX();
        double posY = player.getPosY();
        double posZ = player.getPosZ();
        EyeOfEnderEntityNodrop eyeofenderentity = new EyeOfEnderEntityNodrop(worldIn, posX, posY + player.getHeight() / 2.0F, posZ);
        eyeofenderentity.moveTowards(blockpos);
        worldIn.addEntity(eyeofenderentity);
        if (player instanceof ServerPlayerEntity) {
          CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) player, blockpos);
        }
        worldIn.playSound((PlayerEntity) null, posX, posY, posZ, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F,
            0.4F / (random.nextFloat() * 0.4F + 0.8F));
        worldIn.playEvent((PlayerEntity) null, 1003, new BlockPos(player.getPosition()), 0);
        UtilItemStack.damageItem(player, stack);
        player.addStat(Stats.ITEM_USED.get(this));
        player.getCooldownTracker().setCooldown(stack.getItem(), 10);
      }
    }
    return super.onItemRightClick(worldIn, player, hand);
  }
}
