package com.lothrazar.cyclic.item;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ExpItemGain extends ItemBase {

  public static final int EXP_PER_FOOD = 50;

  public ExpItemGain(Properties properties) {
    super(properties.food(new Food.Builder().setAlwaysEdible().fastToEat().build()));
  }
 
  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) entityLiving;
      p.giveExperiencePoints(EXP_PER_FOOD);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
