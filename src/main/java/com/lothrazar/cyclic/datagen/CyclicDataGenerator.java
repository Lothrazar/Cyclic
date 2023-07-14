package com.lothrazar.cyclic.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CyclicDataGenerator {

  public static class BlockStates extends BlockStateProvider {

    public BlockStates(PackOutput gen, String modid, ExistingFileHelper exFileHelper) {
      super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {}
  }
}
