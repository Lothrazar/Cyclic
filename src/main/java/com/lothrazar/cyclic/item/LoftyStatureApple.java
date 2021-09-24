package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.util.UtilStepHeight;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

public class LoftyStatureApple extends ItemBase {

  public LoftyStatureApple(Properties properties) {
    super(properties);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity == false) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    PlayerEntity player = (PlayerEntity) entityLiving;
    if (!worldIn.isRemote) {
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      datFile.toggleStepHeight();
      ModCyclic.LOGGER.info("enabled step height to file " + datFile);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }

  public static void onUpdate(PlayerEntity player) {
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    if (datFile.stepHeight) {
      UtilStepHeight.enableStepHeight(player);
    }
    else {
      // do we force it off?
      if (datFile.stepHeightForceOff) {
        UtilStepHeight.disableStepHeightForced(player);
      }
    }
  }
}
