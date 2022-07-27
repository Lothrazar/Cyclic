package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.filesystem.CyclicFile;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class EdibleSpecItem extends ItemBaseCyclic {

  private static final int COOLDOWN = 2;
  public static IntValue TICKS;

  public EdibleSpecItem(Properties properties) {
    super(properties.rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(3).saturationMod(0).alwaysEat().build()));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof Player == false) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    Player player = (Player) entityLiving;
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    player.getCooldowns().addCooldown(this, COOLDOWN);
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    datFile.spectatorTicks += TICKS.get();
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }
}
