package com.lothrazar.cyclic.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class CyclicDataGenerator {

  public static class BlockStates extends BlockStateProvider {

    public BlockStates(PackOutput gen, String modid, ExistingFileHelper exFileHelper) {
      super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {}
  }
}
