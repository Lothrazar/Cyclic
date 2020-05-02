package com.lothrazar.cyclic.block.cable;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CableWrench extends ItemBase {

  public CableWrench(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    WrenchActionType.tickTimeout(stack);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    String msg = TextFormatting.GREEN + UtilChat.lang(WrenchActionType.getName(stack));
    tooltip.add(new TranslationTextComponent(msg));
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
