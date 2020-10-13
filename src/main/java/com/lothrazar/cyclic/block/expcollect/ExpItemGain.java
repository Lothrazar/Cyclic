package com.lothrazar.cyclic.block.expcollect;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ExpItemGain extends ItemBase {

  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int FLUID_PER_EXP = 20;
  public static final int EXP_PER_FOOD = 50;

  public ExpItemGain(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
      playerIn.giveExperiencePoints(EXP_PER_FOOD);
      playerIn.getHeldItemMainhand().shrink(1);
      UtilSound.playSound(playerIn, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
