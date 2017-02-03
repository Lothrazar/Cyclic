package com.lothrazar.cyclicmagic.block;

import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockScaffoldingFragile extends BlockScaffolding  implements IHasRecipe{
  public BlockScaffoldingFragile(){
    super();
    this.dropBlock = false;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 12), "sss", "   ", "s s", 's', new ItemStack(Items.STICK));
  }
}
