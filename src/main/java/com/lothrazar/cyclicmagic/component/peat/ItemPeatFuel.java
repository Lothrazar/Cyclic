package com.lothrazar.cyclicmagic.component.peat;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import net.minecraft.item.crafting.IRecipe;

public class ItemPeatFuel extends BaseItem implements IHasRecipe{
  boolean isBaked = false;
  public ItemPeatFuel(boolean baked) {
    this.isBaked = baked;
  }
  @Override
  public IRecipe addRecipe() {
    // TODO Auto-generated method stub
    return null;
  }
}
