package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class EdibleFlightItem extends ItemBase {

  private static final int COOLDOWN = 2;
  public static IntValue TICKS;

  public EdibleFlightItem(Properties properties) {
    super(properties.rarity(Rarity.RARE).food(new Food.Builder().hunger(3).saturation(0).setAlwaysEdible().build()));
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (!(entityLiving instanceof PlayerEntity)) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    PlayerEntity player = (PlayerEntity) entityLiving;
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    datFile.flyTicks += TICKS.get();
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
