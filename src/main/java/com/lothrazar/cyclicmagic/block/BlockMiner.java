package com.lothrazar.cyclicmagic.block;

import com.lothrazar.cyclicmagic.IHasRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockMiner extends Block implements IHasRecipe {

  public BlockMiner() {
    super(Material.IRON);
    //TODO: simple tile entity cave finder, breaks the block its facing
  }

  @Override
  public void addRecipe() {
    
  }
}
