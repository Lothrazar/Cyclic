package com.lothrazar.cyclic.block.cable;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class CableWrench extends ItemBase {

  public static final ITag.INamedTag<Item> WRENCH = ItemTags.createOptional(new ResourceLocation("forge", "tools/wrench"));

  public CableWrench(Properties properties) {
    super(properties.maxStackSize(1));
  }
}
