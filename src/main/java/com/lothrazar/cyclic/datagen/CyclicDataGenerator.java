package com.lothrazar.cyclic.datagen;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = ModCyclic.MODID)
public class CyclicDataGenerator {

  public static class BlockStates extends BlockStateProvider {

    public BlockStates(PackOutput gen, String modid, ExistingFileHelper exFileHelper) {
      super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {}
  }
}
