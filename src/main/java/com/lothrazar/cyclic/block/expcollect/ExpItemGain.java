package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExpItemGain extends ItemBaseCyclic {

  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
  public static final int EXP_PER_FOOD = 50;

  public ExpItemGain(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && handIn == InteractionHand.MAIN_HAND) {
      playerIn.giveExperiencePoints(EXP_PER_FOOD);
      playerIn.getMainHandItem().shrink(1);
      SoundUtil.playSound(playerIn, SoundEvents.EXPERIENCE_ORB_PICKUP);
    }
    return super.use(worldIn, playerIn, handIn);
  }
}
