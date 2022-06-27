package com.lothrazar.cyclic.block;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;

public class MetalBarsBlock extends IronBarsBlock {

  public MetalBarsBlock(Properties prop) {
    super(prop.sound(SoundType.METAL).noOcclusion());
  }
  //  @Override
  //   public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
  //    tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
  //  }
}
