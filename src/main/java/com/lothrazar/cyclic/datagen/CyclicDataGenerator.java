package com.lothrazar.cyclic.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CyclicDataGenerator {

  public static class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
      super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
      //      VariantBlockStateBuilder builder = getVariantBuilder(BlockRegistry.CONVEYOR);
      //      builder.forAllStates((state -> {
      //        ConveyorType type = state.getValue(BlockConveyor.TYPE);
      //        ConveyorSpeed speed = state.getValue(BlockConveyor.SPEED);
      //        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
      //        int y;
      //        switch (facing) {
      //          case EAST:
      //            y = 90;
      //          break;
      //          case SOUTH:
      //            y = 180;
      //          break;
      //          case WEST:
      //            y = 270;
      //          break;
      //          default:
      //            y = 0;
      //        }
      //        String location = String.format("block/conveyor/conveyor_%s_%s", speed.getSerializedName(), type.getSerializedName());
      //        ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(new ResourceLocation(ModCyclic.MODID, location));
      //        return ConfiguredModel.builder()
      //            .rotationX(0)
      //            .rotationY(y)
      //            .modelFile(model)
      //            .build();
      //      }));
    }
  }
}
