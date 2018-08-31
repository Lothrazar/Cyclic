package com.lothrazar.cyclicmagic.skill;

import com.lothrazar.cyclicmagic.capability.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class StepupSkill implements ISkill {


  @Override
  public void toggle(EntityPlayer player) {
    final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
    data.setStepHeightOn(!data.isStepHeightOn());
    //^^ not enough. its not just on off . also serverside
  }

  @Override
  public boolean isEnabled(EntityPlayer player) {
    final IPlayerExtendedProperties data = CapabilityRegistry.getPlayerProperties(player);
    //
    return data.isStepHeightOn();
  }

  @Override
  public ItemStack getIcon() {
    return new ItemStack(Items.APPLE);
  }
}
